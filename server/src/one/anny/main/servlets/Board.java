package one.anny.main.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import one.anny.main.services.BoardServices;
import one.anny.main.tools.Handler;
import one.anny.main.tools.Logger;
import one.anny.main.tools.StdVar;
import one.anny.main.tools.exceptions.BoardException;
import one.anny.main.tools.exceptions.SessionException;
import one.anny.main.tools.exceptions.UserException;
import one.anny.main.tools.models.BoardModel;
import one.anny.main.tools.sessions.SessionModel;
import one.anny.main.tools.sessions.SessionPool;

@WebServlet("/board/*")
public class Board extends HttpServlet {

	// ----- Attributes -----


	/** Serial version number */
	private static final long serialVersionUID = -646457406075452434L;
	
	/** The session pool */
	private SessionPool sessionPool;


	// ----- Constructors -----


	public Board() {
		super();
		this.sessionPool = SessionPool.getInstance();
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
				JSONArray boards = BoardServices.searchBoard(filter, false);
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
				JSONArray boards = BoardServices.searchBoard(filter, isLike);
				res.put("result", boards);

			}

		} catch (SQLException e) {

			Logger.log("Error during the board getting", Logger.ERROR);
			Logger.log(e, Logger.ERROR);
			res = Handler.handleException(e, Handler.SQL_ERROR);

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
		// Prepare the JSON result
		JSONObject res = Handler.getDefaultResponse();

		// Get the current session
		SessionModel currentSession = this.sessionPool.getSession(req, resp, true);

		if(currentSession != null && !currentSession.isAnonymous()) {

			try {

				// Get the board parameters
				String name = req.getParameter("boardName");
				String description = req.getParameter("boardDescription");
				String creatorId = currentSession.getUserId();

				// Create the board model
				BoardModel newBoard = new BoardModel();
				newBoard.setBoardName(name);
				newBoard.setBoardDescription(description);
				newBoard.setBoardCreatorId(creatorId);

				// Create the new board
				BoardServices.createBoard(newBoard);

			} catch (BoardException e) {

				Logger.log("Board data error during the board insertion", Logger.WARNING);
				Logger.log(e, Logger.WARNING);
				res = Handler.handleException(e, Handler.WEB_ERROR);

			} catch (SQLException e) {

				Logger.log("SQL error during the board insertion", Logger.ERROR);
				Logger.log(e, Logger.ERROR);
				res = Handler.handleException(e, Handler.SQL_ERROR);

			} catch (Exception e) {

				Logger.log("Java error during the board insertion", Logger.ERROR);
				Logger.log(e, Logger.ERROR);
				res = Handler.handleException(e, Handler.JAVA_ERROR);

			}

		} else {

			res = Handler.handleException(new SessionException("User not identified"), Handler.WEB_ERROR);

		}

		// Send the result
		resp.setCharacterEncoding(StdVar.APP_ENCODING);
		resp.setContentType(StdVar.JSON_CONTENT_TYPE);
		resp.getWriter().append(res.toJSONString());
	}

	/**
	 * Modify a board in the database
	 */
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Prepare the JSON result
		JSONObject res = Handler.getDefaultResponse();

		// Get the current session
		SessionModel currentSession = this.sessionPool.getSession(req, resp, true);

		if(currentSession != null && !currentSession.isAnonymous()) {

			try {

				// Get the board parameter
				String name = req.getParameter("boardName");
				String description = req.getParameter("boardDescription");
				String creatorId = currentSession.getUserId();

				// Create the board model
				BoardModel board = new BoardModel();
				board.setBoardName(name);
				board.setBoardDescription(description);
				board.setBoardCreatorId(creatorId);

				BoardServices.modifyBoard(board);

			} catch (BoardException e) {

				Logger.log("Error during the board updating", Logger.WARNING);
				Logger.log(e, Logger.WARNING);
				res = Handler.handleException(e, Handler.WEB_ERROR);

			} catch (SQLException e) {

				Logger.log("Error during the board updating", Logger.ERROR);
				Logger.log(e, Logger.ERROR);
				res = Handler.handleException(e, Handler.SQL_ERROR);

			} catch (NullPointerException e) {

				Logger.log("Java error during the board updating", Logger.ERROR);
				Logger.log(e, Logger.ERROR);
				res = Handler.handleException(e, Handler.JAVA_ERROR);

			}

		} else {

			res = Handler.handleException(new SessionException("User not identified"), Handler.WEB_ERROR);

		}

		// Send the result
		resp.setCharacterEncoding(StdVar.APP_ENCODING);
		resp.setContentType(StdVar.JSON_CONTENT_TYPE);
		resp.getWriter().append(res.toJSONString());
	}

	/**
	 * Delete a board in the database
	 */
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Prepare the JSON result
		JSONObject res = Handler.getDefaultResponse();

		// Get the current session
		SessionModel currentSession = this.sessionPool.getSession(req, resp, true);

		if(currentSession != null && !currentSession.isAnonymous()) {

			// Parse URL to get the message ID
			String[] splitedUrl = req.getRequestURI().split("/");
			if(splitedUrl.length >= 4) {

				// Get the board name
				String name = splitedUrl[3];

				// Prepare the board filter 
				BoardModel filter = new BoardModel();
				filter.setBoardName(name);
				if(!currentSession.isAdmin()) {
					filter.setBoardCreatorId(currentSession.getUserId());
				}

				try {

					BoardServices.deleteBoard(filter);

				} catch (BoardException e) {

					Logger.log("Board data error during the board deletion", Logger.WARNING);
					Logger.log(e, Logger.WARNING);
					res = Handler.handleException(e, Handler.WEB_ERROR);

				} catch (SQLException e) {

					Logger.log("SQL error during the board deletion", Logger.ERROR);
					Logger.log(e, Logger.ERROR);
					res = Handler.handleException(e, Handler.SQL_ERROR);

				}

			} else {

				res = Handler.handleException(new UserException("Invalid request"), Handler.WEB_ERROR);

			}

		} else {

			res = Handler.handleException(new SessionException("User not identified"), Handler.WEB_ERROR);

		}

		// Send the result
		resp.setCharacterEncoding(StdVar.APP_ENCODING);
		resp.setContentType(StdVar.JSON_CONTENT_TYPE);
		resp.getWriter().append(res.toJSONString());
	}

}
