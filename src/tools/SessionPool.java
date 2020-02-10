package tools;

import java.util.HashMap;
import java.util.Random;

import tools.exceptions.SessionException;
import tools.models.User;

/**
 * A class to store all sessions and manage them.
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class SessionPool {
	
	// ----- Attributes -----
	
	/** The max number of allowed session in the same time */
	private long maxSessions;
	
	/** The number of current sessions */
	private long currentSessions;
	
	/** The stored sessions */
	private HashMap<String, Session> sessions;
	
	/** Instance of the session pool (singleton) */
	private static SessionPool instance = null;
	
	
	// ----- Constructors -----
	
	
	/**
	 * Construct a new SessionPool with the wanted max sessions
	 * 
	 * @param maxSessions
	 */
	private SessionPool() {
		this.currentSessions = 0;
		this.sessions = new HashMap<String, Session>();
		
		// Init the configurable attributes
		this.init();
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
	
	/**
	 * Init the session pool by getting the needed configuration
	 */
	public void init() {
		this.maxSessions = Config.getMaxSessions();
	}
	
	
	// ----- Getters -----
	
	
	public long getMaxSessions() {
		return this.maxSessions;
	}
	
	public long getCurrentSessions() {
		return this.currentSessions;
	}
	
	
	// ----- Class methods -----
	
	
	/**
	 * Get a stored session and verify if it's not expired
	 * 
	 * @param sessionId The session ID you want to get
	 * @return The session if it exists, null else
	 */
	public Session getSession(String sessionId) {
		Session res = this.sessions.getOrDefault(sessionId, null);
		
		// Test if the session is expired
		if(res instanceof Session) {
			if(res.isExpired()) {
				// Remove the session if it's expired
				this.removeSession(sessionId);
				res = null;
			} else {
				// Else do the action to reset its lifetime
				res.action();
			}
		}
		
		return res;
	}
	
	/**
	 * Add a session to the session pool from its ID and an user if there is enough place
	 * 
	 * @param sessionId The session ID
	 * @param user The user liked to the session
	 */
	public void addSession(String sessionId, User user) throws SessionException {
		if(this.currentSessions >= this.maxSessions) {
			// Try to clean expired sessions
			this.cleanSessions();
		}
		
		if(this.currentSessions < this.maxSessions){
			// Test if the session ID is already stored
			Session testSession = this.getSession(sessionId);
			if(testSession == null) {
				Session session = new Session(sessionId, user);
				this.sessions.put(sessionId, session);
				this.currentSessions++;
			} else {
				throw new SessionException("Session " + sessionId + " already exists !");
			}
		} else {
			throw new SessionException("Too many sessions in the same time !");
		}
	}
	
	/**
	 * Remove the session from the session pool
	 * 
	 * @param sessionId The session ID
	 */
	public void removeSession(String sessionId) {
		Session testSession = this.sessions.remove(sessionId);
		
		// Test if the session were stored
		if(testSession != null) {
			this.currentSessions--;
		}
	}
	
	/**
	 * Remove all the expired sessions
	 */
	public void cleanSessions() {
		for(String sessionId : this.sessions.keySet()) {
			Session session = this.sessions.get(sessionId);
			
			// Test if the session is expired
			if(session.isExpired()) {
				this.removeSession(sessionId);
			}
		}
	}
	
	/**
	 * Generate and return a random session ID which is a String of 32 character
	 * 
	 * @return The session id
	 */
	public String generateSessionId() {
		// Define needed variables
		String possibleChars = "abcdefghijklmnopqrstuvwxyz1234567890";
		Random random = new Random();
		StringBuilder res = new StringBuilder();
		
		// Generate the session ID
		Session testSession = null;
		
		do {
			for (int i = 0; i < 32; i++) {
				int selectedChar = random.nextInt(possibleChars.length());
				res.append(possibleChars.charAt(selectedChar));
			}
			testSession = this.getSession(res.toString());
		} while (testSession != null);
		
		
		// Return the generated token
		return res.toString();
	}

}
