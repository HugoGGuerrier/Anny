package servlets;

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

import services.user.CreateUser;
import services.user.DeleteUser;
import services.user.ModifyUser;
import services.user.SearchUser;
import tools.Handler;
import tools.Logger;
import tools.Security;
import tools.StdVar;
import tools.exceptions.UserException;
import tools.models.UserModel;
import tools.sessions.SessionPool;

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

		// Test if the query is ID formed
		String[] splitedUrl = req.getRequestURI().split("/");
		if(splitedUrl.length >= 4) {	

			// Get the user id and make the request with it
			String userId = splitedUrl[3];
			if(this.security.isValidUserId(userId)) {

				UserModel filter = new UserModel();
				filter.setUserId(userId);

				try {
					
					// Get the user list
					JSONArray users = this.searchUser.searchUser(filter, false);
					res.put("result", users);

				} catch (SQLException e) {

					this.logger.log("Error during the user getting", Logger.ERROR);
					this.logger.log(e, Logger.ERROR);
					res = this.handler.handleException(e, Handler.SQL_ERROR);

				}


			} else {

				res = this.handler.handleException(new UserException("Invalid user ID"), Handler.WEB_ERROR);

			}

		} else {

			String id = req.getParameter("userId");
			String pseudo = req.getParameter("userPseudo");
			String name = req.getParameter("userName");
			String surname = req.getParameter("userSurname");
			String email = req.getParameter("userEmail");
			String date = req.getParameter("userDate");
			Boolean isLike = Boolean.parseBoolean(req.getParameter("isLike"));
			
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
			
			try {
				
				// Try to get the users from the database
				JSONArray users = this.searchUser.searchUser(filter, isLike);
				res.put("result", users);
				
			} catch (SQLException e) {

				this.logger.log("Error during the user getting", Logger.ERROR);
				this.logger.log(e, Logger.ERROR);
				res = this.handler.handleException(e, Handler.SQL_ERROR);
				
			}
			
		}

		// Send the result
		resp.setCharacterEncoding(StdVar.APP_ENCODING);
		resp.setContentType(StdVar.JSON_CONTENT_TYPE);
		resp.getWriter().append(res.toJSONString());
	}

	/**
	 * Create a new user in the database, this method is only for admin users
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO : Vérifier que la session est administrateur
		
		// Get the user param
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

		// Create the response object
		JSONObject res = new JSONObject();

		try {

			// Try to insert the user in the database
			this.createUser.createUser(newUser);
			res.put("result", "SUCCESS");

		} catch (UserException e) {

			this.logger.log("Error with the user informations", Logger.WARNING);
			this.logger.log(e, Logger.WARNING);
			res = this.handler.handleException(e, Handler.WEB_ERROR);

		} catch (SQLException e) {

			this.logger.log("Error during the user insertion in database", Logger.ERROR);
			this.logger.log(e, Logger.ERROR);
			res = this.handler.handleException(e, Handler.SQL_ERROR);

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
		// TODO : Gérer la méthode put pour modifier un utilisateur
		resp.getWriter().append("PUT : Not implemented...");
	}

	/**
	 * Delete an user in the database
	 */
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO : Gérer la méthode delete pour supprimer un utilisateur
		resp.getWriter().append("DELETE : Not implemented...");
	}

}
