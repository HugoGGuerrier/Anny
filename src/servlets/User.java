package servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

/**
 * This is the servlet to manage users
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
@WebServlet("/user")
public class User extends HttpServlet {	

	// ----- Attributes -----
	
	
	/** Serial version number */
	private static final long serialVersionUID = 928019971282204835L;
	
	/** The logger */
	private Logger logger;
	
	/** The handler */
	private Handler handler;
	
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
        
        // Get the instances
        this.logger = Logger.getInstance();
        this.handler = Handler.getInstance();
        this.createUser = CreateUser.getInstance();
        this.deleteUser = DeleteUser.getInstance();
        this.modifyUser = ModifyUser.getInstance();
        this.searchUser = SearchUser.getInstance();
    }
	
	
	// ----- Http methods -----
	

	/**
	 * Get users with parameters
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO : Gérer la méthode pour récupérer un ou plusieurs utilisateurs
		resp.getWriter().append("GET : Not implemented...");
	}

	/**
	 * Create a new user in the database
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Get the user param
		String id = req.getParameter("userId");
		String pseudo = req.getParameter("userPseudo");
		String name = req.getParameter("userName");
		String surname = req.getParameter("userSurname");
		String email = req.getParameter("userEmail");
		String password = req.getParameter("userPassword");
		Boolean admin = Boolean.parseBoolean(req.getParameter("userAdmin"));
		
		// Hash the password to avoid memory leak
		Security security = Security.getInstance();
		password = security.hashString(password);
		
		// Create the new user to insert
		UserModel newUser = new UserModel();
		newUser.setUserId(id);
		newUser.setUserPseudo(pseudo);
		newUser.setUserName(name);
		newUser.setUserSurname(surname);
		newUser.setUserEmail(email);
		newUser.setUserPassword(password);
		newUser.setUserAdmin(admin);
		
		// Create the response object
		JSONObject res = new JSONObject();
		
		try {
			
			this.createUser.createUser(newUser);
			
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
		/**
		 * SELECT * FROM USER WHERE userId = userId.....
		 */
		resp.getWriter().append("PUT : Not implemented...");
	}
	
	/**
	 * Delete an user in the database
	 */
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO : Gérer ka méthode delete pour supprimer un utilisateur
		resp.getWriter().append("DELETE : Not implemented...");
	}

}
