package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.user.CreateUser;
import services.user.DeleteUser;
import services.user.ModifyUser;
import services.user.SearchUser;
import tools.Handler;
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
		String userId = req.getParameter("userId");
		String pseudo = req.getParameter("userPseudo");
		String name = req.getParameter("userName");
		String surname = req.getParameter("userSurname");
		String email = req.getParameter("userEmail");
		String password = req.getParameter("userPassword");
		boolean admin = Boolean.parseBoolean(req.getParameter("userAdmin"));
		
		// Verify and sanitize input data
		
		
		// Create the new user to insert
		UserModel newUser = new UserModel(userId);
		newUser.setPseudo(pseudo);
		newUser.setName(name);
		newUser.setSurname(surname);
		newUser.setEmail(email);
		newUser.setPassword(password);
		newUser.setAdmin(admin);
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
		// TODO : Gérer ka méthode delete pour supprimer un utilisateur
		resp.getWriter().append("DELETE : Not implemented...");
	}

}
