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

@WebServlet("/login")
public class Login extends HttpServlet {

	// ----- Attributes -----
	
	
	private static final long serialVersionUID = -8116808647814082234L;

	
	// ----- Constructors -----
	
	
	public Login() {
		super();
	}
	
	
	// ----- HTTP methods -----
	

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO : Récupérer les arguments de POST et les nettoyer pour les passer au service de log
		resp.getWriter().append("POST : Not implemented...");
	}

}
