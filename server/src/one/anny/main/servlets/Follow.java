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

import one.anny.main.db.filters.FollowFilter;
import one.anny.main.services.FollowServices;
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

	/** The session pool */
	private SessionPool sessionPool;

	// ----- Constructors -----


	public Follow() {
		super();
		this.sessionPool = SessionPool.getInstance();
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

				FollowFilter filter = new FollowFilter();
				filter.addFollowingUserId(followingId);

				JSONArray follows = FollowServices.searchFollow(filter, false);
				res.put("result", follows);
				
			} else {

				// Get the follow parameters
				String[] followedIds = req.getParameterValues("followedUserId") != null ? req.getParameterValues("followedUserId") : new String[0];
				String[] followingIds = req.getParameterValues("followingUserId") != null ? req.getParameterValues("followingUserId") : new String[0];
				String[] followDates = req.getParameterValues("followDate") != null ? req.getParameterValues("followDate") : new String[0];
				
				Boolean isLike = Boolean.parseBoolean(req.getParameter("isLike"));
				String orderColumn = req.getParameter("orderColumn");

				// Create the follow filter
				FollowFilter filter = new FollowFilter();
				for(String followedId : followedIds) {
					filter.addFollowedUserId(followedId);
				}
				for(String followingId : followingIds) {
					filter.addFollowingUserId(followingId);
				}
				for(String date : followDates) {
					try {
						filter.addFollowDate(Date.valueOf(date));
					} catch (IllegalArgumentException e) {
						// Do nothing...
					}
				}
				
				// Set the order column
				filter.setOrderColumn(orderColumn);

				JSONArray follows = FollowServices.searchFollow(filter, isLike);
				res.put("result", follows);

			}

		} catch (SQLException e) {

			Logger.log("SQL error during the follow getting", Logger.ERROR);
			Logger.log(e, Logger.ERROR);
			res = Handler.handleException(e, Handler.SQL_ERROR);

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
		JSONObject res = Handler.getDefaultResponse();

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
				FollowServices.createFollow(newFollow);

			} catch (FollowException e) {

				Logger.log("Follow data error during the follow insertion", Logger.WARNING);
				Logger.log(e, Logger.WARNING);
				res = Handler.handleException(e, Handler.WEB_ERROR);

			} catch (SQLException e) {

				Logger.log("SQL error during the follow insertion", Logger.ERROR);
				Logger.log(e, Logger.ERROR);
				res = Handler.handleException(e, Handler.SQL_ERROR);

			} catch (Exception e) {

				Logger.log("Java error during the follow insertion", Logger.ERROR);
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
	 * Delete a following link in the database
	 */
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Prepare the JSON result
		JSONObject res = Handler.getDefaultResponse();

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

					FollowServices.deleteFollow(filter);

				} catch (FollowException e) {

					Logger.log("Follow data error during the follow deletion", Logger.WARNING);
					Logger.log(e, Logger.WARNING);
					res = Handler.handleException(e, Handler.WEB_ERROR);

				} catch (SQLException e) {

					Logger.log("SQL error during the follow deletion", Logger.ERROR);
					Logger.log(e, Logger.ERROR);
					res = Handler.handleException(e, Handler.WEB_ERROR);

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
