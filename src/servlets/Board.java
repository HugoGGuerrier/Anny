package servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import services.board.CreateBoard;
import services.board.DeleteBoard;
import services.board.ModifyBoard;
import services.board.SearchBoard;
import tools.Handler;
import tools.Logger;
import tools.StdVar;
import tools.models.BoardModel;
import tools.sessions.SessionPool;

@WebServlet("/board/*")
public class Board extends HttpServlet {

	// ----- Attributes -----


	/** Serial version number */
	private static final long serialVersionUID = -646457406075452434L;

	/** Logger tool */
	private Logger logger;

	/** Handler tool */
	private Handler handler;

	/** The session pool */
	private SessionPool sessionPool;

	/** Service to create a board */
	private CreateBoard createBoard;

	/** Board deletion service */
	private DeleteBoard deleteBoard;

	/** Service to modify a board */
	private ModifyBoard modifyBoard;

	/** The searching board service */
	private SearchBoard searchBoard;


	// ----- Constructors -----


	public Board() {
		super();

		// Get instances
		this.logger = Logger.getInstance();
		this.handler = Handler.getInstance();
		this.sessionPool = SessionPool.getInstance();
		this.createBoard = CreateBoard.getInstance();
		this.deleteBoard = DeleteBoard.getInstance();
		this.modifyBoard = ModifyBoard.getInstance();
		this.searchBoard = SearchBoard.getInstance();
	}


	// ----- HTTP methods -----


	/**
	 * Get a board list from the database
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Prepare the JSON result
		JSONObject res = new JSONObject();

		try {
			
			// Test if the query is ID formed
			String[] splitedUrl = req.getRequestURI().split("/");
			if(splitedUrl.length >= 4) {

				// Get the user id and make the request with it
				String name = splitedUrl[3];

				// Create the filter to get the board
				BoardModel filter = new BoardModel();
				filter.setBoardName(name);

				// Get the message list
				JSONArray boards = this.searchBoard.searchBoard(filter, false);
				res.put("result", boards);

			} else {

				// Get the message request parameter
				String name = req.getParameter("boardName");
				String description = req.getParameter("boardDescription");
				String creatorId = req.getParameter("boardCreatorId");
				Boolean isLike = Boolean.parseBoolean(req.getParameter("isLike"));

				// Prepare the filter
				BoardModel filter = new BoardModel();
				filter.setBoardName(name);
				filter.setBoardDescription(description);
				filter.setBoardCreatorId(creatorId);

				// Try to get the boards from the database
				JSONArray boards = this.searchBoard.searchBoard(filter, isLike);
				res.put("result", boards);

			}
			
		} catch (SQLException e) {
			
			this.logger.log("Error during the board getting", Logger.ERROR);
			this.logger.log(e, Logger.ERROR);
			res = this.handler.handleException(e, Handler.SQL_ERROR);
			
		}

		// Send the result
		resp.setCharacterEncoding(StdVar.APP_ENCODING);
		resp.setContentType(StdVar.JSON_CONTENT_TYPE);
		resp.getWriter().append(res.toJSONString());
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
