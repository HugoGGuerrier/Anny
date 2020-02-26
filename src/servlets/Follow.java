package servlets;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.follow.CreateFollow;
import services.follow.DeleteFollow;
import services.follow.SearchFollow;
import tools.Handler;
import tools.Logger;
import tools.Security;

/**
 * The follow servlet to manage following links
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
	
	/** Service to create a following link */
	private CreateFollow createFollow;
	
	/** Service to delete a following link */
	private DeleteFollow deleteFollow;
	
	/** Service to create a following link */
	private SearchFollow searchFollow;
	
	
	// ----- Constructors -----


	public Follow() {
		super();
		
		// Get instances
		this.logger = Logger.getInstance();
		this.handler = Handler.getInstance();
		this.security = Security.getInstance();
		this.createFollow = CreateFollow.getInstance();
		this.deleteFollow = DeleteFollow.getInstance();
		this.searchFollow = SearchFollow.getInstance();
	}


	// ----- HTTP methods -----

	
	/**
	 * Get following links with parameters
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO : Récupérer les follows avec les paramètres
		resp.getWriter().append("GET : Not implemented...");
	}

	/**
	 * Create a new following link in the database
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Get the follows param
		String followedUserId = req.getParameter("followedUserId");
		String followingUserId = req.getParameter("followingUserId");
		Date date = Date.valueOf(req.getParameter("followDate"));
		// TODO : la suite.
		resp.getWriter().append("POST : Not implemented...");
	}

	/**
	 * Delete a following link in the database
	 */
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO : Supprimer le follow à l'aide des paramètres
		resp.getWriter().append("DELETE : Not implemented...");
	}

}
