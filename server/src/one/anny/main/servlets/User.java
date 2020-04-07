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

	/** Session pool */
	private SessionPool sessionPool;


	// ----- Constructors -----


	public User() {
		super();
		this.sessionPool = SessionPool.getInstance();
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
		
		// Get the current session
		SessionModel currentSession = this.sessionPool.getSession(req, resp, true);

		try {

			// Test if the query is ID formed
			String[] splitedUrl = req.getRequestURI().split("/");
			if(splitedUrl.length >= 4) {	

				// Get the user id and make the request with it
				String id = splitedUrl[3];

				// Create the filter to get the user
				UserFilter filter = new UserFilter();
				filter.addUserId(id);

				// Get the user list
				JSONArray users = UserServices.searchUser(filter, false, currentSession == null ? false : currentSession.isAdmin());
				res.put("result", users);

			} else {

				// Get the user parameters
				String[] ids = req.getParameterValues("userId") != null ? req.getParameterValues("userId") :  new String[0];
				String[] pseudos = req.getParameterValues("userPseudo") != null ? req.getParameterValues("userPseudo") :  new String[0];
				String[] emails = req.getParameterValues("userEmail") != null ? req.getParameterValues("userEmail") :  new String[0];
				String[] dates = req.getParameterValues("userDate") != null ? req.getParameterValues("userDate") :  new String[0];
				
				Boolean isLike = Boolean.parseBoolean(req.getParameter("isLike"));
				String orderColumn = req.getParameter("orderColumn");

				// Prepare the user search filter
				UserFilter filter = new UserFilter();
				
				for(String id : ids) {
					filter.addUserId(id);
				}
				for(String pseudo : pseudos) {
					filter.addUserPseudo(pseudo);
				}
				for(String email : emails) {
					filter.addUserEmail(email);
				}
				for(String date : dates) {
					try {
						filter.addUserDate(Date.valueOf(date));
					} catch (IllegalArgumentException e) {
						// Do nothing...
					}
				}
				
				// Set the order column
				filter.setOrderColumn(orderColumn);

				// Try to get the users from the database
				JSONArray users = UserServices.searchUser(filter, isLike, currentSession == null ? false : currentSession.isAdmin());
				res.put("result", users);

			}

		} catch (SQLException e) {

			Logger.log("Error during the user getting", Logger.ERROR);
			Logger.log(e, Logger.ERROR);
			res = Handler.handleException(e, Handler.SQL_ERROR);

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
		JSONObject res = Handler.getDefaultResponse();

		// Get the current session to verify that it's an admin session
		SessionModel currentSession = this.sessionPool.getSession(req, resp, true);

		if(currentSession != null && currentSession.isAdmin()) {

			try {

				// Get the user parameters
				String id = req.getParameter("userId");
				String pseudo = req.getParameter("userPseudo");
				String email = req.getParameter("userEmail");
				String password = req.getParameter("userPassword");
				Date date = new Date(new java.util.Date().getTime());
				Boolean admin = Boolean.parseBoolean(req.getParameter("userAdmin"));

				// Hash the password to avoid memory leak
				password = Security.hashString(password);

				// Create the new user to insert
				UserModel newUser = new UserModel();
				newUser.setUserId(id);
				newUser.setUserPseudo(pseudo);
				newUser.setUserEmail(email);
				newUser.setUserPassword(password);
				newUser.setUserDate(date);
				newUser.setUserAdmin(admin);

				// Try to insert the user in the database
				UserServices.createUser(newUser);

			} catch (UserException e) {

				Logger.log("Error during the user insertion", Logger.WARNING);
				Logger.log(e, Logger.WARNING);
				res = Handler.handleException(e, Handler.WEB_ERROR);

			} catch (SQLException e) {

				Logger.log("Error during the user insertion", Logger.ERROR);
				Logger.log(e, Logger.ERROR);
				res = Handler.handleException(e, Handler.SQL_ERROR);

			} catch (Exception e) {

				Logger.log("Java error during the user insertion", Logger.ERROR);
				Logger.log(e, Logger.ERROR);
				res = Handler.handleException(e, Handler.JAVA_ERROR);

			}

		} else {

			res = Handler.handleNotConnected();

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
		JSONObject res = Handler.getDefaultResponse();

		// Get the current session
		SessionModel currentSession = this.sessionPool.getSession(req, resp, true);

		if(currentSession != null) {

			try {
				// Get the user parameters
				String id = req.getParameter("userId");
				String pseudo = req.getParameter("userPseudo");
				String email = req.getParameter("userEmail");
				String password = req.getParameter("userPassword");
				Boolean admin = Boolean.parseBoolean(req.getParameter("userAdmin"));

				// Hash the password to update it
				password = Security.hashString(password);

				// Create the user to modify it
				UserModel modifiedUser = new UserModel();
				modifiedUser.setUserId(id);
				modifiedUser.setUserPseudo(pseudo);
				modifiedUser.setUserEmail(email);
				modifiedUser.setUserPassword(password);
				modifiedUser.setUserAdmin(admin);

				// Try to insert the user in the database
				if(id.equals(currentSession.getUserId()) || currentSession.isAdmin()) {

					UserServices.modifyUser(modifiedUser);

				} else {

					res = Handler.handleException(new SessionException("Authorization denied"), Handler.WEB_ERROR);

				}

			} catch (UserException e) {

				Logger.log("User data error during the user updating", Logger.WARNING);
				Logger.log(e, Logger.WARNING);
				res = Handler.handleException(e, Handler.WEB_ERROR);

			} catch (SQLException e) {

				Logger.log("SQL error during the user updating", Logger.ERROR);
				Logger.log(e, Logger.ERROR);
				res = Handler.handleException(e, Handler.SQL_ERROR);

			} catch (Exception e) {

				Logger.log("Java error during the user updating", Logger.ERROR);
				Logger.log(e, Logger.ERROR);
				res = Handler.handleException(e, Handler.JAVA_ERROR);

			}

		} else {

			res = Handler.handleNotConnected();

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
		JSONObject res = Handler.getDefaultResponse();

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
						UserServices.deleteUser(filter);
						if(!currentSession.isAdmin()) {
							
							this.sessionPool.removeSession(currentSession.getSessionId(), resp);

						} else {

							res = Handler.handleException(new SessionException("Authorization denied"), Handler.WEB_ERROR);

						}

					} catch (UserException e) {

						Logger.log("Input error during the user deletion", Logger.WARNING);
						Logger.log(e, Logger.WARNING);
						res = Handler.handleException(e, Handler.WEB_ERROR);

					} catch (SQLException e) {

						Logger.log("SQL error during the user deletion", Logger.ERROR);
						Logger.log(e, Logger.ERROR);
						res = Handler.handleException(e, Handler.SQL_ERROR);

					} catch (Exception e) {

						Logger.log("Java error during the user deletion", Logger.ERROR);
						Logger.log(e, Logger.ERROR);
						res = Handler.handleException(e, Handler.JAVA_ERROR);

					}
					
				} else {
					
					res = Handler.handleException(new UserException("Authorization denied"), Handler.WEB_ERROR);
					
				}

			} else {

				res = Handler.handleException(new UserException("Invalid request"), Handler.WEB_ERROR);

			}

		} else {

			res = Handler.handleNotConnected();

		}

		// Send the result
		resp.setCharacterEncoding(StdVar.APP_ENCODING);
		resp.setContentType(StdVar.JSON_CONTENT_TYPE);
		resp.getWriter().append(res.toJSONString());
	}

}
