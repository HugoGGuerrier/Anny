package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/board/*")
public class Board extends HttpServlet {
	
	// ----- Attributes -----
	
	
	/** Serial version number */
	private static final long serialVersionUID = -646457406075452434L;

	
	// ----- Constructors -----
	
	
    public Board() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
    // ----- HTTP methods -----

    
    /**
     * Get a board list from the database
     */
    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO : Faire la récupération des boards
    	resp.getWriter().append("GET : Not implemented...");
	}

    /**
     * Create a new board in the database
     */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO : Faire un ajout d'un board
		resp.getWriter().append("POST : Not implemented...");
	}
	
	/**
	 * Modify a board in the database
	 */
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO : Gérer la méthode put pour modifier un utilisateur
		resp.getWriter().append("PUT : Not implemented...");
	}
	
	/**
	 * Delete a board in the database
	 */
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO : Gérer ka méthode delete pour supprimer un utilisateur
		resp.getWriter().append("DELETE : Not implemented...");
	}

}
