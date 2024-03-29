package one.anny.main.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import one.anny.main.db.filters.UserFilter;
import one.anny.main.services.UserServices;
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
 * This is the login servlet.
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
@WebServlet("/login/*")
public class Login extends HttpServlet {

	// ----- Attributes -----


	/** Serial version number */
	private static final long serialVersionUID = -8116808647814082234L;

	/** The session pool */
	private SessionPool sessionPool;


	// ----- Constructors -----


	public Login() {
		super();
		this.sessionPool = SessionPool.getInstance();
	}


	// ----- HTTP methods -----


	/** 
	 * Get if the user is currently login
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Prepare the JSON result
		JSONObject res = Handler.getDefaultResponse();
		
		// Get the current session
		SessionModel currentSession = this.sessionPool.getSession(req, resp, true);
		
		// Test if the session exists and is not anonymous
		if(currentSession == null || currentSession.isAnonymous()) {
			res = Handler.handleNotConnected();
		}

		// Send the result
		resp.setCharacterEncoding(StdVar.APP_ENCODING);
		resp.setContentType(StdVar.JSON_CONTENT_TYPE);
		resp.getWriter().append(res.toJSONString());
	}

	/**
	 * Create a new session with a login and password
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Prepare the JSON result
		JSONObject res = Handler.getDefaultResponse();

		// Try to get the session
		SessionModel currentSession = this.sessionPool.getSession(req, resp, false);

		// If the session does not exists or is anonymous create it
		if(currentSession == null || currentSession.isAnonymous()) {

			// Get the user attributes
			String userId = req.getParameter("userId");
			String userPassword = req.getParameter("userPassword");

			// Hash the password to avoid memory leak
			userPassword = Security.hashString(userPassword);

			// Prepare the user filter
			UserFilter userFilter = new UserFilter();
			userFilter.addUserId(userId);
			userFilter.addUserPassword(userPassword);

			try {

				JSONArray users = UserServices.searchUser(userFilter, false, true);

				if(users.size() == 1) {

					// Get the user from the JSON result
					JSONObject userJson = (JSONObject) users.get(0);
					UserModel user = new UserModel();
					user.setUserAdmin((Boolean) userJson.get("userAdmin"));
					user.setUserId((String) userJson.get("userId"));

					// Create or update the session in the cache and put it to the client
					if(currentSession == null) {

						String sessionId = this.sessionPool.generateSessionId();
						SessionModel newSession = new SessionModel(sessionId, user);
						newSession.setAdmin(user.isUserAdmin());
						this.sessionPool.putSession(newSession, resp);

					} else {

						currentSession.setUserId(user.getUserId());
						currentSession.setAdmin(user.isUserAdmin());
						this.sessionPool.putSession(currentSession);

					}

				} else {

					Logger.log("Error during user identification", Logger.WARNING);
					res = Handler.handleException(new UserException("Invalid user id or password"), Handler.WEB_ERROR);

				}

			} catch (SQLException e) {

				Logger.log("Error during the user getting", Logger.ERROR);
				Logger.log(e, Logger.ERROR);
				res = Handler.handleException(e, Handler.SQL_ERROR);

			}

		} else {

			res = Handler.handleException(new SessionException("Session already exists"), Handler.WEB_ERROR);

		}

		// Send the result
		resp.setCharacterEncoding(StdVar.APP_ENCODING);
		resp.setContentType(StdVar.JSON_CONTENT_TYPE);
		resp.getWriter().append(res.toJSONString());
	}

	/**
	 * Delete the session with the wanted id
	 */
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Prepare the JSON result
		JSONObject res = Handler.getDefaultResponse();

		// Get the session ID
		String sessionId = this.sessionPool.getSessionIdFromRequest(req);

		if(sessionId != null) {

			// Remove the session from the cache and client
			this.sessionPool.removeSession(sessionId, resp);

		} else {

			res = Handler.handleException(new SessionException("Session does not exists"), Handler.WEB_ERROR);

		}

		// Send the result
		resp.setCharacterEncoding(StdVar.APP_ENCODING);
		resp.setContentType(StdVar.JSON_CONTENT_TYPE);
		resp.getWriter().append(res.toJSONString());
	}



}
