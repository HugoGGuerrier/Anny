package one.anny.main.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class NotFound
 */
@WebServlet("/404")
public class NotFound extends HttpServlet {
	
	// ----- Attributes -----


	private static final long serialVersionUID = -5682588955795150952L;
	
	
	// ----- Constructors -----


	public NotFound() {
        super();
    }
	
	
	// ----- HTTP methods -----
	

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setStatus(404);
		resp.getWriter().append(
				"<html><head>"
				+ "<title>404</title>"
				+ "</head>"
				+ "<body>"
				+ "<h1>Error 404 - Page not found</h1>"
				+ "<p>Sorry but you're lost :(</p>"
				+ "</body>"
				+ "</html>");
	}

}
