package tools.sessions;

import java.util.Random;

import tools.exceptions.SessionException;

/**
 * A class to store all sessions in the cache and manage them
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class SessionPool {

	// ----- Attributes -----


	/** Cache writter instance */
	private CacheManager manager;

	/** Thread of the cache writter */
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
	 * @return The session if it exists and is valid
	 * @throws SessionException If the wanted session not exists
	 */
	public Session getSession(String sessionId, boolean doAction) throws SessionException {
		// Get the session from the cache manager
		Session res = this.manager.getSession(sessionId);

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
		
		if(res == null) {
			throw new SessionException("Session " + sessionId + " does not exists");
		}

		// Return the result
		return res;
	}

	/**
	 * Put a session in the sessio cache
	 * 
	 * @param session The session to update
	 */
	public void putSession(Session session) {
		this.manager.addSessionsAddBuffer(session);
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
		Session testSession = null;

		do {
			for (int i = 0; i < 32; i++) {
				int selectedChar = random.nextInt(possibleChars.length());
				res.append(possibleChars.charAt(selectedChar));
			}
			try {
				testSession = this.getSession(res.toString(), false);
			} catch (SessionException e) {
				testSession = null;
			}
		} while (testSession != null);


		// Return the generated ID
		return res.toString();
	}

}
