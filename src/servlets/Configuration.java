package servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tools.Config;

/**
 * This servlet is used to initialize the web application. It is mainly use by the administrator.
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
@WebServlet("/configuration")
public class Configuration extends HttpServlet {
	
	// ----- Attributes -----
	
	
	private static final long serialVersionUID = 1L;
	
	
	// ----- Class methods -----
	
	
	/**
	 * This methods initialize the application
	 */
	public void init() {
		// Get the configuration file
		ServletContext context = this.getServletContext();
		InputStream in = context.getResourceAsStream("/WEB-INF/config.json");
		Reader configReader = new InputStreamReader(in);
		
		// Initialize the application configuration
		Config.init(configReader);
		
		// DEBUG SECTION
		System.out.println(Config.display());
	}

	/**
	 * Method to get the current configuration
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO : Send the current configuration in JSON format
		
		this.init();
	}

	/**
	 * Method to put a new configuration
	 */
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO : Set the new configuration
		
		// Reload the configuration
		this.init();
	}

}