package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tools.Handler;
import tools.Logger;
import tools.Security;

/**
 * The follow servlet to manage them
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
@WebServlet("/follow")
public class Follow extends HttpServlet {

	// ----- Attributes -----


	/** Serial version number */
	private static final long serialVersionUID = 4169235877331702798L;
	
	/** The logger */
	private Logger logger;
	
	/** The handler */
	private Handler handler;
	
	/** The security tool */
	private Security security;
	

	// ----- Constructors -----


	public Follow() {
		super();
		
		// Get instances
		this.logger = Logger.getInstance();
		this.handler = Handler.getInstance();
		this.security = Security.getInstance();
	}


	// ----- HTTP methods -----

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO : Récupérer les follows avec les paramètres
		resp.getWriter().append("GET : Not implemented...");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO : Ajouter un follow
		resp.getWriter().append("POST : Not implemented...");
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO : Supprimer le follow à l'aide des paramètres
		resp.getWriter().append("DELETE : Not implemented...");
	}

}
