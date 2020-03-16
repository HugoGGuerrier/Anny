package one.anny.main.servlets;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import one.anny.main.services.user.CreateUser;
import one.anny.main.services.user.DeleteUser;
import one.anny.main.services.user.ModifyUser;
import one.anny.main.services.user.SearchUser;
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
 * This is the servlet to manage users
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
@WebServlet("/user/*")
public class User extends HttpServlet {	

	// ----- Attributes -----


	/** Serial version number */
	private static final long serialVersionUID = 928019971282204835L;

	/** The logger */
	private Logger logger;

	/** The handler */
	private Handler handler;

	/** Security tool */
	private Security security;

	/** Session pool */
	private SessionPool sessionPool;

	/**	Service to create an user */
	private CreateUser createUser;

	/** Service to delete an user */
	private DeleteUser deleteUser;

	/** Service to modify an user */
	private ModifyUser modifyUser;

	/** Service to search users */
	private SearchUser searchUser;


	// ----- Constructors -----


	public User() {
		super();

		// Get instances
		this.logger = Logger.getInstance();
		this.handler = Handler.getInstance();
		this.security = Security.getInstance();
		this.sessionPool = SessionPool.getInstance();
		this.createUser = CreateUser.getInstance();
		this.deleteUser = DeleteUser.getInstance();
		this.modifyUser = ModifyUser.getInstance();
		this.searchUser = SearchUser.getInstance();
	}


	// ----- HTTP methods -----


	/**
	 * Get users with parameters
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Prepare the response JSON
		JSONObject res = new JSONObject();

		try {

			// Test if the query is ID formed
			String[] splitedUrl = req.getRequestURI().split("/");
			if(splitedUrl.length >= 4) {	

				// Get the user id and make the request with it
				String id = splitedUrl[3];

				// Create the filter to get the user
				UserModel filter = new UserModel();
				filter.setUserId(id);

				// Get the user list
				JSONArray users = this.searchUser.searchUser(filter, false);
				res.put("result", users);

			} else {

				// Get the user parameters
				String id = req.getParameter("userId");
				String pseudo = req.getParameter("userPseudo");
				String name = req.getParameter("userName");
				String surname = req.getParameter("userSurname");
				String email = req.getParameter("userEmail");
				String date = req.getParameter("userDate");
				Boolean isLike = Boolean.parseBoolean(req.getParameter("isLike"));

				// Prepare the user search filter
				UserModel filter = new UserModel();
				filter.setUserId(id);
				filter.setUserPseudo(pseudo);
				filter.setUserName(name);
				filter.setUserSurname(surname);
				filter.setUserEmail(email);
				try {
					filter.setUserDate(Date.valueOf(date));
				} catch (IllegalArgumentException e) {
					filter.setUserDate(null);
				}

				// Try to get the users from the database
				JSONArray users = this.searchUser.searchUser(filter, isLike);
				res.put("result", users);

			}

		} catch (SQLException e) {

			this.logger.log("Error during the user getting", Logger.ERROR);
			this.logger.log(e, Logger.ERROR);
			res = this.handler.handleException(e, Handler.SQL_ERROR);

		}

		// Send the result
		resp.setCharacterEncoding(StdVar.APP_ENCODING);
		resp.setContentType(StdVar.JSON_CONTENT_TYPE);
		resp.getWriter().append(res.toJSONString());
	}

	/**
	 * Create a new user in the database, this method is only for admin users
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Create the response object
		JSONObject res = this.handler.getDefaultResponse();

		// Get the current session to verify that it's an admin session
		SessionModel currentSession = this.sessionPool.getSession(req, resp, true);

		if(currentSession != null && currentSession.isAdmin()) {

			try {

				// Get the user parameters
				String id = req.getParameter("userId");
				String pseudo = req.getParameter("userPseudo");
				String name = req.getParameter("userName");
				String surname = req.getParameter("userSurname");
				String email = req.getParameter("userEmail");
				String password = req.getParameter("userPassword");
				Date date = new Date(new java.util.Date().getTime());
				Boolean admin = Boolean.parseBoolean(req.getParameter("userAdmin"));

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

			} catch (UserException e) {

				this.logger.log("Error during the user insertion", Logger.WARNING);
				this.logger.log(e, Logger.WARNING);
				res = this.handler.handleException(e, Handler.WEB_ERROR);

			} catch (SQLException e) {

				this.logger.log("Error during the user insertion", Logger.ERROR);
				this.logger.log(e, Logger.ERROR);
				res = this.handler.handleException(e, Handler.SQL_ERROR);

			} catch (Exception e) {

				this.logger.log("Java error during the user insertion", Logger.ERROR);
				this.logger.log(e, Logger.ERROR);
				res = this.handler.handleException(e, Handler.JAVA_ERROR);

			}

		} else {

			res = this.handler.handleException(new SessionException("Authorization denied"), Handler.WEB_ERROR);

		}

		// Send the result
		resp.setCharacterEncoding(StdVar.APP_ENCODING);
		resp.setContentType(StdVar.JSON_CONTENT_TYPE);
		resp.getWriter().append(res.toJSONString());
	}

	/**
	 * Modify an user in the database
	 */
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Create the response object
		JSONObject res = this.handler.getDefaultResponse();

		// Get the current session
		SessionModel currentSession = this.sessionPool.getSession(req, resp, true);

		if(currentSession != null) {

			try {
				// Get the user parameters
				String id = req.getParameter("userId");
				String pseudo = req.getParameter("userPseudo");
				String name = req.getParameter("userName");
				String surname = req.getParameter("userSurname");
				String email = req.getParameter("userEmail");
				String password = req.getParameter("userPassword");
				Boolean admin = Boolean.parseBoolean(req.getParameter("userAdmin"));

				// Hash the password to update it
				password = this.security.hashString(password);

				// Create the user to modify it
				UserModel modifiedUser = new UserModel();
				modifiedUser.setUserId(id);
				modifiedUser.setUserPseudo(pseudo);
				modifiedUser.setUserName(name);
				modifiedUser.setUserSurname(surname);
				modifiedUser.setUserEmail(email);
				modifiedUser.setUserPassword(password);
				modifiedUser.setUserAdmin(admin);

				// Try to insert the user in the database
				if(id.equals(currentSession.getUserId()) || currentSession.isAdmin()) {

					this.modifyUser.modifyUser(modifiedUser);

				} else {

					res = this.handler.handleException(new SessionException("Authorization denied"), Handler.WEB_ERROR);

				}

			} catch (UserException e) {

				this.logger.log("User data error during the user updating", Logger.WARNING);
				this.logger.log(e, Logger.WARNING);
				res = this.handler.handleException(e, Handler.WEB_ERROR);

			} catch (SQLException e) {

				this.logger.log("SQL error during the user updating", Logger.ERROR);
				this.logger.log(e, Logger.ERROR);
				res = this.handler.handleException(e, Handler.SQL_ERROR);

			} catch (Exception e) {

				this.logger.log("Java error during the user updating", Logger.ERROR);
				this.logger.log(e, Logger.ERROR);
				res = this.handler.handleException(e, Handler.JAVA_ERROR);

			}

		} else {

			res = this.handler.handleException(new SessionException("User not identified"), Handler.WEB_ERROR);

		}

		// Send the result
		resp.setCharacterEncoding(StdVar.APP_ENCODING);
		resp.setContentType(StdVar.JSON_CONTENT_TYPE);
		resp.getWriter().append(res.toJSONString());
	}

	/**
	 * Delete an user in the database
	 */
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Prepare the response JSON
		JSONObject res = this.handler.getDefaultResponse();

		// Get the session
		SessionModel currentSession = this.sessionPool.getSession(req, resp, false);

		if(currentSession != null) {

			// Parse the URL to get the user ID
			String[] splitedUrl = req.getRequestURI().split("/");
			if(splitedUrl.length >= 4) {

				// Get the user id and make the request with it
				String id = splitedUrl[3];

				// Prepare the filter
				UserModel filter = new UserModel();
				filter.setUserId(id);

				if(id.equals(currentSession.getUserId()) || currentSession.isAdmin()) {
					
					try {

						// Delete the user and destroy the session if the user is not an admin
						this.deleteUser.deleteUser(filter);
						if(!currentSession.isAdmin()) {
							
							this.sessionPool.removeSession(currentSession.getSessionId(), resp);

						} else {

							res = this.handler.handleException(new SessionException("Authorization denied"), Handler.WEB_ERROR);

						}

					} catch (UserException e) {

						this.logger.log("Input error during the user deletion", Logger.WARNING);
						this.logger.log(e, Logger.WARNING);
						res = this.handler.handleException(e, Handler.WEB_ERROR);

					} catch (SQLException e) {

						this.logger.log("SQL error during the user deletion", Logger.ERROR);
						this.logger.log(e, Logger.ERROR);
						res = this.handler.handleException(e, Handler.SQL_ERROR);

					} catch (Exception e) {

						this.logger.log("Java error during the user deletion", Logger.ERROR);
						this.logger.log(e, Logger.ERROR);
						res = this.handler.handleException(e, Handler.JAVA_ERROR);

					}
					
				} else {
					
					res = this.handler.handleException(new UserException("Authorization denied"), Handler.WEB_ERROR);
					
				}

			} else {

				res = this.handler.handleException(new UserException("Invalid request"), Handler.WEB_ERROR);

			}

		} else {

			res = this.handler.handleException(new SessionException("User not identified"), Handler.WEB_ERROR);

		}

		// Send the result
		resp.setCharacterEncoding(StdVar.APP_ENCODING);
		resp.setContentType(StdVar.JSON_CONTENT_TYPE);
		resp.getWriter().append(res.toJSONString());
	}

}
