package tools.sessions;

import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tools.Config;

/**
 * A class to store all sessions in the cache and manage them
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class SessionPool {

	// ----- Attributes -----


	/** Cache writer instance */
	private CacheManager manager;

	/** Thread of the cache writer */
	private Thread managerThread;

	/** Instance of the session pool (singleton) */
	private static SessionPool instance = null;


	// ----- Constructors -----


	/**
	 * Construct a new SessionPool with the wanted max sessions
	 * 
	 * @param maxSessions
	 */
	private SessionPool() {
		this.manager = new CacheManager();
		this.managerThread = new Thread(this.manager);
		this.managerThread.start();
	}

	/**
	 * Get the unique instance of SessionPool
	 * 
	 * @return The SessionPool
	 */
	public static SessionPool getInstance() {
		if(SessionPool.instance == null) {
			SessionPool.instance = new SessionPool();
		}
		return SessionPool.instance;
	}


	// ----- Class methods -----


	/**
	 * Reset the session file
	 */
	public void reset() {
		this.manager.resetSessions();
	}

	/**
	 * Get a stored session and verify if it's not expired and execute the action if you want
	 * 
	 * @param sessionId The session ID you want to get
	 * @param autoAction If you want to execute the action automatically
	 * @return The session if it exists and is valid null else
	 */
	public SessionModel getSession(String sessionId, boolean doAction) {
		// Get the session from the cache manager
		SessionModel res = this.manager.getSession(sessionId);

		// Verify the session validity
		if(res != null) {
			if(res.isExpired()) {
				this.removeSession(res.getSessionId());
				res = null;
			} else {
				if(doAction) {
					res.action();
					this.putSession(res);
				}
			}
		}

		// Return the result
		return res;
	}
	
	/**
	 * Get the session from the request cookies, a shortcut
	 * 
	 * @param req The request
	 * @param resp The response to update the cookie on the client
	 * @param doAction Do an action on the session
	 * @return The session or null if it does not exists
	 */
	public SessionModel getSession(HttpServletRequest req, HttpServletResponse resp, boolean doAction) {
		String sessionId = this.getSessionIdFromRequest(req);
		SessionModel res = null;

		// If the session is in the cookies get it
		if(sessionId != null) {
			res = this.getSession(sessionId, doAction);
		}

		// If the session exists update the clie,t
		if(res != null) {
			Cookie sessionCookie = new Cookie("annySessionId", res.getSessionId());
			sessionCookie.setMaxAge((int) Config.getSessionTimeToLive());
			sessionCookie.setPath("/");
			resp.addCookie(sessionCookie);
		}

		// Return the result
		return res;
	}

	/**
	 * Put a session in the sessio cache
	 * 
	 * @param session The session to update
	 */
	public void putSession(SessionModel session) {
		this.manager.addSessionsAddBuffer(session);
	}

	/**
	 * Put a new sessio in the cache and in the response
	 * 
	 * @param session The session tu put
	 * @param resp The response to put the sesison in
	 */
	public void putSession(SessionModel session, HttpServletResponse resp) {
		this.putSession(session);
		Cookie sessionCookie = new Cookie("annySessionId", session.getSessionId());
		sessionCookie.setMaxAge((int) Config.getSessionTimeToLive());
		sessionCookie.setPath("/");
		resp.addCookie(sessionCookie);
	}

	/**
	 * Remove the session from the session pool
	 * 
	 * @param sessionId The session ID
	 */
	public void removeSession(String sessionId) {
		this.manager.addSessionsRemoveBuffer(sessionId);
	}

	/**
	 * Remove a session from the cache and from the client
	 * 
	 * @param sessionId The session ID
	 * @param resp The response to the client
	 */
	public void removeSession(String sessionId, HttpServletResponse resp) {
		this.removeSession(sessionId);
		Cookie sessionCookie = new Cookie("annySessionId", sessionId);
		sessionCookie.setMaxAge(0);
		sessionCookie.setPath("/");
		resp.addCookie(sessionCookie);
	}

	/**
	 * Generate and return a random session ID which is a String of 32 character
	 * 
	 * @return The session id
	 */
	public String generateSessionId() {
		// Define needed variables
		String possibleChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		Random random = new Random();
		StringBuilder res = new StringBuilder();

		// Generate the session ID
		SessionModel testSession = null;

		do {
			for (int i = 0; i < 32; i++) {
				int selectedChar = random.nextInt(possibleChars.length());
				res.append(possibleChars.charAt(selectedChar));
			}
			testSession = this.getSession(res.toString(), false);
		} while (testSession != null);


		// Return the generated ID
		return res.toString();
	}

	/**
	 * Get the session ID from the HTTP request
	 * 
	 * @param req The request
	 * @return The session ID or null if it does not exists
	 */
	public String getSessionIdFromRequest(HttpServletRequest req) {
		String res = null;

		// Get the session id from the cookies
		Cookie[] cookies = req.getCookies();
		
		if(cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if(cookie.getName().equals("annySessionId")) {
					res = cookie.getValue();
				}
			}
		}
		
		// Return the result
		return res;
	}

}
