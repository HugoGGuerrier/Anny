package one.anny.main.servlets;

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

import one.anny.main.services.MessageServices;
import one.anny.main.tools.Handler;
import one.anny.main.tools.Logger;
import one.anny.main.tools.StdVar;
import one.anny.main.tools.exceptions.MessageException;
import one.anny.main.tools.exceptions.MongoException;
import one.anny.main.tools.exceptions.SessionException;
import one.anny.main.tools.exceptions.UserException;
import one.anny.main.tools.models.MessageModel;
import one.anny.main.tools.sessions.SessionModel;
import one.anny.main.tools.sessions.SessionPool;


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
	
	/** Session pool */
	private SessionPool sessionPool;


	// ----- Constructors -----


	public Message() {
		super();
		this.sessionPool = SessionPool.getInstance();
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

			// Get the message id and make the request with it
			String id = splitedUrl[3];

			// Create the filter to get the message
			MessageModel filter = new MessageModel();
			filter.setMessageId(id);

			// Get the message list
			JSONArray messages = MessageServices.searchMessage(filter, false);
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

			// Try to get the messages from the database
			JSONArray messages = MessageServices.searchMessage(filter, isLike);
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
		JSONObject res = Handler.getDefaultResponse();

		// Get the current session
		SessionModel currentSession = this.sessionPool.getSession(req, resp, true);

		if(currentSession != null && !currentSession.isAnonymous()) {

			try {

				// Get the message parameters
				String text = req.getParameter("messageText");
				String boardName = req.getParameter("messageBoardName");
				String parentId = req.getParameter("messageParentId");
				String posterId = currentSession.getUserId();
				Date date = new Date(new java.util.Date().getTime());

				// Create the message model
				MessageModel newMessage = new MessageModel();
				newMessage.setMessageText(text);
				newMessage.setMessageBoardName(boardName);
				newMessage.setMessagePosterId(posterId);
				newMessage.setMessageDate(date);

				// Create the new message
				MessageServices.createMessage(newMessage, parentId);

			} catch (MessageException e) {

				Logger.log("Message data error during the message insertion", Logger.WARNING);
				Logger.log(e, Logger.WARNING);
				res = Handler.handleException(e, Handler.WEB_ERROR);

			} catch (MongoException e) {

				Logger.log("MongoDB error during the message insertion", Logger.ERROR);
				Logger.log(e, Logger.ERROR);
				res = Handler.handleException(e, Handler.MONGO_ERROR);

			} catch (SQLException e) {

				Logger.log("SQL error during the message insertion", Logger.ERROR);
				Logger.log(e, Logger.ERROR);
				res = Handler.handleException(e, Handler.SQL_ERROR);

			} catch (NullPointerException e) {

				Logger.log("Java error during the message insertion", Logger.ERROR);
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
	 * Modify a message text
	 */
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Prepare the JSON result
		JSONObject res = Handler.getDefaultResponse();

		// Get the current session
		SessionModel currentSession = this.sessionPool.getSession(req, resp, true);

		if(currentSession != null && !currentSession.isAnonymous()) {

			try {

				// Get the message parameter
				String id = req.getParameter("messageId");
				String text = req.getParameter("messageText");
				String posterId = currentSession.getUserId();

				// Create the message model
				MessageModel message = new MessageModel();
				message.setMessageId(id);
				message.setMessageText(text);
				message.setMessagePosterId(posterId);

				MessageServices.modifyMessage(message);

			} catch (MessageException e) {

				Logger.log("Message data error during the message updating", Logger.WARNING);
				Logger.log(e, Logger.WARNING);
				res = Handler.handleException(e, Handler.WEB_ERROR);

			} catch (MongoException e) {

				Logger.log("MongoDB error during the message updating", Logger.ERROR);
				Logger.log(e, Logger.ERROR);
				res = Handler.handleException(e, Handler.MONGO_ERROR);

			} catch (NullPointerException e) {
				
				Logger.log("Java error during the message updating", Logger.ERROR);
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
	 * Delete a message by its id 
	 */
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Prepare the JSON result
		JSONObject res = Handler.getDefaultResponse();

		// Get the current session
		SessionModel currentSession = this.sessionPool.getSession(req, resp, true);

		if(currentSession != null && !currentSession.isAnonymous()) {
			
			// Parse URL to get the message ID
			String[] splitedUrl = req.getRequestURI().split("/");
			if(splitedUrl.length >= 4) {

				String id = splitedUrl[3];

				MessageModel filter = new MessageModel();
				filter.setMessageId(id);
				if(!currentSession.isAdmin()) {
					filter.setMessagePosterId(currentSession.getUserId());
				}

				try {

					MessageServices.deleteMessage(filter);

				} catch (MessageException e) {

					Logger.log("Message data error during the message deletion", Logger.WARNING);
					Logger.log(e, Logger.WARNING);
					res = Handler.handleException(e, Handler.WEB_ERROR);

				} catch (MongoException e) {

					Logger.log("MongoDB error during the message deletion", Logger.ERROR);
					Logger.log(e, Logger.ERROR);
					res = Handler.handleException(e, Handler.MONGO_ERROR);

				} catch (SQLException e) {

					Logger.log("SQL error during the message deletion", Logger.ERROR);
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
