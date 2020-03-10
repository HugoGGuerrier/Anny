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

import one.anny.main.services.follow.CreateFollow;
import one.anny.main.services.follow.DeleteFollow;
import one.anny.main.services.follow.SearchFollow;
import one.anny.main.tools.Handler;
import one.anny.main.tools.Logger;
import one.anny.main.tools.StdVar;
import one.anny.main.tools.exceptions.FollowException;
import one.anny.main.tools.exceptions.SessionException;
import one.anny.main.tools.exceptions.UserException;
import one.anny.main.tools.models.FollowModel;
import one.anny.main.tools.sessions.SessionModel;
import one.anny.main.tools.sessions.SessionPool;

/**
 * The follow servlet to manage following links
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
@WebServlet("/follow/*")
public class Follow extends HttpServlet {

	// ----- Attributes -----


	/** Serial version number */
	private static final long serialVersionUID = 4169235877331702798L;

	/** The logger */
	private Logger logger;

	/** The handler */
	private Handler handler;

	/** The session pool */
	private SessionPool sessionPool;

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
		this.sessionPool = SessionPool.getInstance();
		this.createFollow = CreateFollow.getInstance();
		this.deleteFollow = DeleteFollow.getInstance();
		this.searchFollow = SearchFollow.getInstance();
	}


	// ----- HTTP methods -----


	/**
	 * Get following links with parameters
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Prepare the JSON response
		JSONObject res = new JSONObject();

		try {

			// Parse the URL
			String[] splitedUrl = req.getRequestURI().split("/");
			if(splitedUrl.length >= 4) {

				String followingId = splitedUrl[3];

				FollowModel filter = new FollowModel();
				filter.setFollowingUserId(followingId);

				JSONArray follows = this.searchFollow.searchFollow(filter, false);
				res.put("result", follows);

			} else {

				// Get the follow parameters
				String followedId = req.getParameter("followedUserId");
				String followingId = req.getParameter("followingUserId");
				String followDate = req.getParameter("followDate");
				Boolean isLike = Boolean.parseBoolean(req.getParameter("isLike"));

				// Create the follow filter
				FollowModel filter = new FollowModel();
				filter.setFollowedUserId(followedId);
				filter.setFollowingUserId(followingId);
				try {
					filter.setFollowDate(Date.valueOf(followDate));
				} catch (IllegalArgumentException e) {
					filter.setFollowDate(null);
				}

				JSONArray follows = this.searchFollow.searchFollow(filter, isLike);
				res.put("result", follows);

			}

		} catch (SQLException e) {

			this.logger.log("Error during the follow getting", Logger.ERROR);
			this.logger.log(e, Logger.ERROR);
			res = this.handler.handleException(e, Handler.SQL_ERROR);

		}

		// Send the result
		resp.setCharacterEncoding(StdVar.APP_ENCODING);
		resp.setContentType(StdVar.JSON_CONTENT_TYPE);
		resp.getWriter().append(res.toJSONString());
	}

	/**
	 * Create a new following link in the database
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Prepare the JSON result
		JSONObject res = this.handler.getDefaultResponse();

		// Get the current session
		SessionModel currentSession = this.sessionPool.getSession(req, resp, true);

		if(currentSession != null && !currentSession.isAnonymous()) {

			try {

				// Get the follow parameters
				String followedId = req.getParameter("followedUserId");
				String followingId = currentSession.getUserId();
				Date followDate = new Date(new java.util.Date().getTime());

				// Create the follow model
				FollowModel newFollow = new FollowModel();
				newFollow.setFollowedUserId(followedId);
				newFollow.setFollowingUserId(followingId);
				newFollow.setFollowDate(followDate);

				// Insert the new follow in the database
				this.createFollow.createFollow(newFollow);

			} catch (FollowException e) {

				this.logger.log("Error during the follow insertion", Logger.WARNING);
				this.logger.log(e, Logger.WARNING);
				res = this.handler.handleException(e, Handler.WEB_ERROR);

			} catch (SQLException e) {

				this.logger.log("Error during the follow insertion", Logger.ERROR);
				this.logger.log(e, Logger.ERROR);
				res = this.handler.handleException(e, Handler.SQL_ERROR);

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
	 * Delete a following link in the database
	 */
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Prepare the JSON result
		JSONObject res = this.handler.getDefaultResponse();

		// Get the current session
		SessionModel currentSession = this.sessionPool.getSession(req, resp, true);

		if(currentSession != null && !currentSession.isAnonymous()) {

			// Get the deletion parameter
			String[] splitedUrl = req.getRequestURI().split("/");
			if(splitedUrl.length >= 4) {

				// Get the follow parameters
				String followedId = splitedUrl[3];
				String followingId = currentSession.getUserId();

				// Create the follow filter
				FollowModel filter = new FollowModel();
				filter.setFollowedUserId(followedId);
				filter.setFollowingUserId(followingId);

				try {

					this.deleteFollow.deleteFollow(filter);

				} catch (FollowException e) {

					this.logger.log("Error during the follow deletion", Logger.WARNING);
					this.logger.log(e, Logger.WARNING);
					res = this.handler.handleException(e, Handler.WEB_ERROR);

				} catch (SQLException e) {

					this.logger.log("Error during the follow deletion", Logger.ERROR);
					this.logger.log(e, Logger.ERROR);
					res = this.handler.handleException(e, Handler.WEB_ERROR);

				}

			} else {

				res = this.handler.handleException(new UserException("Invalid request"), Handler.WEB_ERROR);

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
