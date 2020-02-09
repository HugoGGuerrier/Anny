package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is the login servlet.
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */

@WebServlet("/Login")
public class Login extends HttpServlet {
	
	// ----- Attributes -----
	
	
	private static final long serialVersionUID = 1L;

	
	// ----- Constructors -----
	
	
	public Login() {
		super();
	}
	
	
	// ----- HTTP methods -----
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO : Récupérer les arguments de POST et les nettoyer pour les passer au service de log
	}

}
