package one.anny.main.servlets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import one.anny.main.tools.Config;
import one.anny.main.tools.StdVar;

/**
 * This is the index servlet, the default web page.
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */

@WebServlet("/index/*")
public class Index extends HttpServlet {

	// ----- Attributes -----
	
	
	/** The serial version number */
	private static final long serialVersionUID = 966304929303826141L;

	
	// ----- HTTP methods -----


	/**
	 * The get method of the index route
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Read the index HTML file
		FileReader fileReader = new FileReader(Paths.get(Config.getBasePath() + StdVar.INDEX_HTML).toFile());
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		StringBuilder extractedHtml = new StringBuilder();
		
		String line = "";
		while(line != null) {
			extractedHtml.append(line);
			line = bufferedReader.readLine();
		}
		
		// Split the index HTML file
		String[] splitedHtml = extractedHtml.toString().split("<head>");
		
		// Create the configuration var
		
	}

}
