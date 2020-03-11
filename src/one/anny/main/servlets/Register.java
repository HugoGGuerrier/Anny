package one.anny.main.servlets;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import one.anny.main.services.user.CreateUser;
import one.anny.main.tools.Config;
import one.anny.main.tools.Handler;
import one.anny.main.tools.Logger;
import one.anny.main.tools.Security;
import one.anny.main.tools.StdVar;
import one.anny.main.tools.exceptions.SessionException;
import one.anny.main.tools.exceptions.UserException;
import one.anny.main.tools.models.UserModel;
import one.anny.main.tools.sessions.SessionModel;
import one.anny.main.tools.sessions.SessionPool;

/**
 * Servlet to register a new user
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
@WebServlet("/register/*")
public class Register extends HttpServlet {

	// ----- Attributes -----


	/** The logger */
	private Logger logger;

	/** The handler tool */
	private Handler handler;

	/** Security tool */
	private Security security;

	/** The session pool */
	private SessionPool sessionPool;

	/** The user creation service */
	private CreateUser createUser;

	/** Serial version number */
	private static final long serialVersionUID = 6127736970206786786L;


	// ----- Constructors -----


	public Register() {
		super();

		// Get instances
		this.logger = Logger.getInstance();
		this.handler = Handler.getInstance();
		this.security = Security.getInstance();
		this.sessionPool = SessionPool.getInstance();
		this.createUser = CreateUser.getInstance();
	}


	// ----- HTTP methods -----


	/**
	 * Put a CSRF token to the client to avoid multiple register
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Prepare the JSON result
		JSONObject res = this.handler.getDefaultResponse();

		// Try to get the session
		SessionModel currentSession = this.sessionPool.getSession(req, resp, true);

		if(currentSession == null) {

			// Generate a CSRF token
			String token = this.security.generateCSRFToken();

			// Create an anonymous session
			String sessionId = this.sessionPool.generateSessionId();
			SessionModel anonSession = new SessionModel(sessionId);
			anonSession.putAttribute("adminSession", "false");
			anonSession.putAttribute("csrfToken", token);
			this.sessionPool.putSession(anonSession, resp);

			// Put the CSRF token in the cookies
			Cookie csrfCookie = new Cookie("annyCsrfToken", token);
			csrfCookie.setMaxAge((int) Config.getSessionTimeToLive());
			resp.addCookie(csrfCookie);

		} else {

			// If the user is already logged in
			res = this.handler.handleException(new SessionException("Cannot register with an existing session"), Handler.WEB_ERROR);

		}

		// Send the result
		resp.setCharacterEncoding(StdVar.APP_ENCODING);
		resp.setContentType(StdVar.JSON_CONTENT_TYPE);
		resp.getWriter().append(res.toJSONString());
	}

	/**
	 * Register a new user with the CSRF verification
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO LATER : Une vérification par mail de l'utilisateur pour l'obliger à confirmer son mail

		// Prepare the JSON result
		JSONObject res = this.handler.getDefaultResponse();

		// Get the current session
		SessionModel currentSession = this.sessionPool.getSession(req, resp, false);

		if(currentSession != null && currentSession.isAnonymous()) {

			// Get the CSRF tokens from the session and the cookie
			String sessionCsrf = currentSession.getAttribute("csrfToken");
			String cookieCsrf = null;
			Cookie[] cookies = req.getCookies();
			for(int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if(cookie.getName().equals("annyCsrfToken")) {
					cookieCsrf = cookie.getValue();
				}
			}

			if(sessionCsrf != null && sessionCsrf.equals(cookieCsrf)) {

				try {

					// Get the user parameters
					String id = req.getParameter("userId");
					String pseudo = req.getParameter("userPseudo");
					String name = req.getParameter("userName");
					String surname = req.getParameter("userSurname");
					String email = req.getParameter("userEmail");
					String password = req.getParameter("userPassword");
					Date date = new Date(new java.util.Date().getTime());
					Boolean admin = false;

					// Hash the password to avoid memory leak
					password = this.security.hashString(password);

					// Create the new user to insert
					UserModel newUser = new UserModel();
					newUser.setUserId(id);
					newUser.setUserPseudo(pseudo);
					newUser.setUserName(name);
					newUser.setUserSurname(surname);
					newUser.setUserEmail(email);
					newUser.setUserPassword(password);
					newUser.setUserDate(date);
					newUser.setUserAdmin(admin);

					// Try to insert the user in the database
					this.createUser.createUser(newUser);
					
					// Set the session to the registered user
					currentSession.setUserId(id);
					currentSession.putAttribute("sessionAdmin", "false");
					currentSession.removeAttribute("csrfToken");
					this.sessionPool.putSession(currentSession, resp);
					
					// Remove the CSRF cookie
					Cookie csrfCookie = new Cookie("annyCsrfToken", "");
					csrfCookie.setMaxAge(0);
					resp.addCookie(csrfCookie);

				} catch (UserException e) {

					this.logger.log("User data error during the user registration", Logger.WARNING);
					this.logger.log(e, Logger.WARNING);
					res = this.handler.handleException(e, Handler.WEB_ERROR);

				} catch (SQLException e) {

					this.logger.log("SQL error during the user registration", Logger.ERROR);
					this.logger.log(e, Logger.ERROR);
					res = this.handler.handleException(e, Handler.SQL_ERROR);

				} catch (Exception e) {

					this.logger.log("Java error during the user registration", Logger.ERROR);
					this.logger.log(e, Logger.ERROR);
					res = this.handler.handleException(e, Handler.JAVA_ERROR);
					
				}

			} else {

				this.logger.log("User try to register with an invalid CSRF token - IP = " + req.getRemoteAddr(), Logger.WARNING);
				res = this.handler.handleException(new SessionException("Invalid CSRF token"), Handler.WEB_ERROR);

			}

		} else {

			res = this.handler.handleException(new SessionException("Invalid session"), Handler.WEB_ERROR);

		}

		// Send the result
		resp.setCharacterEncoding(StdVar.APP_ENCODING);
		resp.setContentType(StdVar.JSON_CONTENT_TYPE);
		resp.getWriter().append(res.toJSONString());
	}

}
