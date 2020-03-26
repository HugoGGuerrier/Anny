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
	
	/** The HTML index file */
	private final String htmlIndexFile;
	
	
	// ----- Constructors -----
	
	
	public Index() {
		super();
		
		String tmp;
		
		// Get the index HTML file
		try {
			
			// Open the index file and read it
			FileReader fileReader = new FileReader(Paths.get(Config.getBasePath() + StdVar.INDEX_HTML).toFile());
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuilder extractedHtml = new StringBuilder();
			
			String line = "";
			while(line != null) {
				extractedHtml.append(line);
				line = bufferedReader.readLine();
			}
			
			// Close all readers
			bufferedReader.close();
			fileReader.close();
			
			tmp = extractedHtml.toString();
			
		} catch (IOException e) {

			tmp = null;
			
		}
		
		this.htmlIndexFile = tmp;
	}


	// ----- HTTP methods -----


	/**
	 * The get method of the index route
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding(StdVar.APP_ENCODING);
		resp.setContentType(StdVar.HTML_CONTENT_TYPE);
		resp.getWriter().append(this.htmlIndexFile);
	}

}
