package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class User
 */
@WebServlet("/user")
public class User extends HttpServlet {
	
	// ----- Attributes -----
	
	
	private static final long serialVersionUID = 1L;

	
	// ----- Constructors -----
	
	
	public User() {
        super();
    }
	
	
	// ----- Http methods -----
	

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO : Gérer la méthode pour récupérer un ou plusieurs utilisateurs
		resp.getWriter().append("GET : Not implemented...");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO : Gérer la méthode post pour créer un utilisateur
		resp.getWriter().append("POST : Not implemented...");
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO : Gérer la méthode put pour modifier un utilisateur
		resp.getWriter().append("PUT : Not implemented...");
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO : Gérer ka méthode delete pour supprimer un utilisateur
		resp.getWriter().append("DELETE : Not implemented...");
	}

}
