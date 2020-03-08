package servlets;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import services.message.CreateMessage;
import services.message.DeleteMessage;
import services.message.ModifyMessage;
import services.message.SearchMessage;
import tools.Handler;
import tools.Logger;
import tools.StdVar;
import tools.exceptions.MessageException;
import tools.exceptions.MongoException;
import tools.exceptions.SessionException;
import tools.exceptions.UserException;
import tools.models.MessageModel;
import tools.sessions.SessionModel;
import tools.sessions.SessionPool;


/**
 * Servlet that manage messages in the database
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
@WebServlet("/message/*")
public class Message extends HttpServlet {

	// ----- Attributes -----


	/** The serial version number */
	private static final long serialVersionUID = -3129064805234167755L;

	/** The logger */
	private Logger logger;

	/** The handler */
	private Handler handler;

	/** Session pool */
	private SessionPool sessionPool;

	/** Message creation service */
	private CreateMessage createMessage;

	/** Message deletion service */
	private DeleteMessage deleteMessage;

	/** Message modifying service */
	private ModifyMessage modifyMessage;

	/** Message searching service */
	private SearchMessage searchMessage;


	// ----- Constructors -----


	public Message() {
		super();

		// Get instances
		this.logger = Logger.getInstance();
		this.handler = Handler.getInstance();
		this.sessionPool = SessionPool.getInstance();
		this.createMessage = CreateMessage.getInstance();
		this.deleteMessage = DeleteMessage.getInstance();
		this.modifyMessage = ModifyMessage.getInstance();
		this.searchMessage = SearchMessage.getInstance();
	}


	// ----- HTTP Methods -----


	/**
	 * Get messages with the wanted parameters
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Prepare the response JSON
		JSONObject res = new JSONObject();

		// Test if the query is ID formed
		String[] splitedUrl = req.getRequestURI().split("/");
		if(splitedUrl.length >= 4) {

			// Get the user id and make the request with it
			String id = splitedUrl[3];

			// Create the filter to get the user
			MessageModel filter = new MessageModel();
			filter.setMessageId(id);

			// Get the message list
			JSONArray messages = this.searchMessage.searchMessage(filter, false);
			res.put("result", messages);

		} else {

			// Get the message request parameter
			String id = req.getParameter("messageId");
			String text = req.getParameter("messageText");
			String boardName = req.getParameter("messageBoardName");
			String posterId = req.getParameter("messagePosterId");
			String date = req.getParameter("messageDate");
			Boolean isLike = Boolean.parseBoolean(req.getParameter("isLike"));

			// Prepare the filter
			MessageModel filter = new MessageModel();
			filter.setMessageId(id);
			filter.setMessageText(text);
			filter.setMessageBoardName(boardName);
			filter.setMessagePosterId(posterId);
			try {
				filter.setMessageDate(new Date(Long.parseLong(date)));
			} catch (IllegalArgumentException e) {
				filter.setMessageDate(null);
			}

			// Try to get the users from the database
			JSONArray messages = this.searchMessage.searchMessage(filter, isLike);
			res.put("result", messages);

		}

		// Send the result
		resp.setCharacterEncoding(StdVar.APP_ENCODING);
		resp.setContentType(StdVar.JSON_CONTENT_TYPE);
		resp.getWriter().append(res.toJSONString());
	}

	/**
	 * Insert a new message in the database
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Prepare the JSON result
		JSONObject res = this.handler.getDefaultResponse();

		// Get the current session
		SessionModel currentSession = this.sessionPool.getSession(req, resp, true);

		if(currentSession != null && !currentSession.isAnonymous()) {

			try {

				// Get the message parameters
				String text = req.getParameter("messageText");
				String boardName = req.getParameter("messageBoardName");
				String parentId = req.getParameter("messageParentId");

				// Create the message model
				MessageModel newMessage = new MessageModel();
				newMessage.setMessageText(text);
				newMessage.setMessageBoardName(boardName);
				newMessage.setMessagePosterId(currentSession.getUserId());
				newMessage.setMessageDate(new Date(new java.util.Date().getTime()));

				// Create the new message
				this.createMessage.createMessage(newMessage, parentId);

			} catch (MessageException e) {

				this.logger.log("Error during the message insertion", Logger.WARNING);
				this.logger.log(e, Logger.WARNING);
				res = this.handler.handleException(e, Handler.WEB_ERROR);

			} catch (MongoException e) {

				this.logger.log("Error during the message insertion", Logger.ERROR);
				this.logger.log(e, Logger.ERROR);
				res = this.handler.handleException(e, Handler.MONGO_ERROR);

			} catch (SQLException e) {

				this.logger.log("Error during the message insertion", Logger.ERROR);
				this.logger.log(e, Logger.ERROR);
				res = this.handler.handleException(e, Handler.SQL_ERROR);

			} catch (NullPointerException e) {

				this.logger.log(e, Logger.ERROR);
				res = this.handler.handleException(e, Handler.JAVA_ERROR);

			}

		} else {

			res = this.handler.handleException(new SessionException("User not identified"), Handler.WEB_ERROR);

		}

		// Send the result
		resp.setCharacterEncoding(StdVar.APP_ENCODING);
		resp.setContentType(StdVar.JSON_CONTENT_TYPE);
		resp.getWriter().append(res.toJSONString());
	}

	/**
	 * Modify a message text
	 */
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Prepare the JSON result
		JSONObject res = this.handler.getDefaultResponse();

		// Get the current session
		SessionModel currentSession = this.sessionPool.getSession(req,  resp, true);

		if(currentSession != null && !currentSession.isAnonymous()) {

			// Get the message parameter
			String id = req.getParameter("messageId");
			String text = req.getParameter("messageText");
			
			// Create the message model
			MessageModel message = new MessageModel();
			message.setMessageId(id);
			message.setMessageText(text);
			message.setMessagePosterId(currentSession.getUserId());
			
			try {
				
				this.modifyMessage.modifyMessage(message);
				
			} catch (MessageException e) {

				this.logger.log("Error during the message updating", Logger.WARNING);
				this.logger.log(e, Logger.WARNING);
				res = this.handler.handleException(e, Handler.WEB_ERROR);
				
			} catch (MongoException e) {

				this.logger.log("Error during the message deletion", Logger.ERROR);
				this.logger.log(e, Logger.ERROR);
				res = this.handler.handleException(e, Handler.MONGO_ERROR);
				
			}

		} else {

			res = this.handler.handleException(new SessionException("User not identified"), Handler.WEB_ERROR);

		}

		// Send the result
		resp.setCharacterEncoding(StdVar.APP_ENCODING);
		resp.setContentType(StdVar.JSON_CONTENT_TYPE);
		resp.getWriter().append(res.toJSONString());
	}

	/**
	 * Delete a message by its id 
	 */
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Prepare the JSON result
		JSONObject res = this.handler.getDefaultResponse();

		// Get the current session
		SessionModel currentSession = this.sessionPool.getSession(req, resp, true);

		if(currentSession != null && !currentSession.isAnonymous()) {
			// Get if the session is admin
			boolean isSessionAdmin = Boolean.parseBoolean(currentSession.getAttribute("adminSession"));

			// Parse URL to get the message ID
			String[] splitedUrl = req.getRequestURI().split("/");
			if(splitedUrl.length >= 4) {

				String id = splitedUrl[3];

				MessageModel filter = new MessageModel();
				filter.setMessageId(id);
				if(!isSessionAdmin) {
					filter.setMessagePosterId(currentSession.getUserId());
				}

				try {

					this.deleteMessage.deleteMessage(filter);

				} catch (MessageException e) {

					this.logger.log("Error during the message deletion", Logger.WARNING);
					this.logger.log(e, Logger.WARNING);
					res = this.handler.handleException(e, Handler.WEB_ERROR);

				} catch (MongoException e) {

					this.logger.log("Error during the message deletion", Logger.ERROR);
					this.logger.log(e, Logger.ERROR);
					res = this.handler.handleException(e, Handler.MONGO_ERROR);

				} catch (SQLException e) {

					this.logger.log("Error during the message deletion", Logger.ERROR);
					this.logger.log(e, Logger.ERROR);
					res = this.handler.handleException(e, Handler.SQL_ERROR);

				}

			} else {

				res = this.handler.handleException(new UserException("Invalid input"), Handler.WEB_ERROR);

			}

		} else {

			res = this.handler.handleException(new SessionException("User not identified"), Handler.WEB_ERROR);

		}

		// Send the result
		resp.setCharacterEncoding(StdVar.APP_ENCODING);
		resp.setContentType(StdVar.JSON_CONTENT_TYPE);
		resp.getWriter().append(res.toJSONString());
	}

}
