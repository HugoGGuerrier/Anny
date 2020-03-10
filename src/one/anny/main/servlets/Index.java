package one.anny.main.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import one.anny.main.tools.Config;

/**
 * This is the index servlet, the default web page.
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */

@WebServlet("/index/*")
public class Index extends HttpServlet {	

	// ----- Attributes -----
	
	
	private static final long serialVersionUID = 966304929303826141L;

	
	// ----- Class methods -----


	/**
	 * The get method of the index route
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO : Envoyer le code de l'application cliente
		
		response.getWriter().append("Welcome on Anny ^^ , current version is ").append(Config.getVersion());
	}

}
