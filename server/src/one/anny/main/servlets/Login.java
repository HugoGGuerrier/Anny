package one.anny.main.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import one.anny.main.db.managers.UserDatabaseManager;
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


	/** Logger */
	private Logger logger;

	/** The handler */
	private Handler handler;

	/** Security tool */
	private Security security;

	/** The session pool */
	private SessionPool sessionPool;

	/** The search user */
	private UserDatabaseManager userDatabaseManager;

	/** Serial version number */
	private static final long serialVersionUID = -8116808647814082234L;


	// ----- Constructors -----


	public Login() {
		super();

		// Get instances
		this.logger = Logger.getInstance();
		this.handler = Handler.getInstance();
		this.security = Security.getInstance();
		this.sessionPool = SessionPool.getInstance();
		this.userDatabaseManager = UserDatabaseManager.getInstance();
	}


	// ----- HTTP methods -----


	/**
	 * Create a new session with a login and password
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Prepare the JSON result
		JSONObject res = this.handler.getDefaultResponse();

		// Try to get the session
		SessionModel currentSession = this.sessionPool.getSession(req, resp, false);

		// If the session does not exists or is anonymous create it
		if(currentSession == null || currentSession.isAnonymous()) {

			// Get the user attributes
			String userId = req.getParameter("userId");
			String userPassword = req.getParameter("userPassword");

			// Hash the password to avoid memory leak
			userPassword = this.security.hashString(userPassword);

			// Prepare the user filter
			UserModel userFilter = new UserModel();
			userFilter.setUserId(userId);
			userFilter.setUserPassword(userPassword);

			try {

				List<UserModel> users = this.userDatabaseManager.getUsers(userFilter, false);

				if(users.size() == 1) {

					// Create or update the session in the cache and put it to the client
					UserModel user = users.get(0);
					
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

					this.logger.log("Error during user identification", Logger.WARNING);
					res = this.handler.handleException(new UserException("Invalid user id or password"), Handler.WEB_ERROR);

				}

			} catch (SQLException e) {

				this.logger.log("Error during the user getting", Logger.ERROR);
				this.logger.log(e, Logger.ERROR);
				res = this.handler.handleException(e, Handler.SQL_ERROR);

			}

		} else {

			res = this.handler.handleException(new SessionException("Session already exists"), Handler.WEB_ERROR);

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
		JSONObject res = this.handler.getDefaultResponse();
		
		// Get the session ID
		String sessionId = this.sessionPool.getSessionIdFromRequest(req);
		
		if(sessionId != null) {
			
			// Remove the session from the cache and client
			this.sessionPool.removeSession(sessionId, resp);
			
		} else {
			
			res = this.handler.handleException(new SessionException("Session does not exists"), Handler.WEB_ERROR);
			
		}

		// Send the result
		resp.setCharacterEncoding(StdVar.APP_ENCODING);
		resp.setContentType(StdVar.JSON_CONTENT_TYPE);
		resp.getWriter().append(res.toJSONString());
	}



}