package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet to register a new user
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
@WebServlet("/register")
public class Register extends HttpServlet {
	
	// ----- Attributes -----
	
	
	/** Serial version number */
	private static final long serialVersionUID = 6127736970206786786L;

	
	// ----- Constructors -----
	
	
	public Register() {
        super();
    }
	
	
	// ----- HTTP methods -----
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO : Créer une session anonyme et mettre le token CSRF en place pour éviter le flood
		resp.getWriter().append("GET : Not implemented...");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO LATER : Une vérification par mail de l'utilisateur pour l'obliger à confirmer son mail
		
		// TODO : Faire l'enregistrement du nouvel utilisateur et créer sa session
		resp.getWriter().append("POST : Not implemented...");
	}

}
