package tools.sessions;

import java.util.Date;
import java.util.HashMap;

import tools.Config;
import tools.models.UserModel;

/**
 * This class represent an HTTP session with params and the user related to it
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class Session {
	
	// ----- Attributes -----
	
	
	/** The session's unique id */
	private String sessionId;
	
	/** The user the the session is related to */
	private UserModel user;
	
	/** The time that the session will still alive (seconds) */
	private long timeToLive;
	
	/** The date when the session were created (seconds) */
	private long lastActionDate;
	
	/** The session's storing array */
	private HashMap<String, String> sessionArray;
	
	
	// ----- Constructors -----
	
	
	/**
	 * Construct a new Session with its ID and its user
	 * 
	 * @param sessionId The session's ID
	 * @param user The user related to the session
	 */
	public Session(String sessionId, UserModel user) {
		this.sessionId = sessionId;
		this.user = user;
		this.timeToLive = Config.getSessionTimeToLive();
		this.lastActionDate = new Date().getTime() / 1000;
		
		this.sessionArray = new HashMap<String, String>();
	}
	
	
	// ----- Getters -----
	
	
	public String getSessionId() {
		return this.sessionId;
	}
	
	public UserModel getUser() {
		return this.user;
	}
	
	public long getTimeToLive() {
		return this.timeToLive;
	}
	
	public long getCreationDate() {
		return this.lastActionDate;
	}
	
	
	// ----- Class methods -----
	
	
	/**
	 * Get a value from the session array
	 * 
	 * @param key The key of the value to get
	 * @return The value if it exists, null else
	 */
	public String getValue(String key) {
		return this.sessionArray.getOrDefault(key, null);
	}
	
	/**
	 * Put a value in the session array
	 * 
	 * @param key The key
	 * @param value The value
	 */
	public void putValue(String key, String value) {
		this.sessionArray.put(key, value);
	}
	
	/**
	 * Remove a value from the session array
	 * 
	 * @param key The key of the value to remove
	 */
	public void removeValue(String key) {
		this.sessionArray.remove(key);
	}
	
	/**
	 * Update the last action date to reset the lifetime
	 */
	public void action() {
		this.lastActionDate = new Date().getTime() / 1000;
	}
	
	/**
	 * Get if the session is expired at the current date
	 */
	public boolean isExpired() {
		long currentDate = new Date().getTime() / 1000;
		return currentDate - this.lastActionDate > this.timeToLive;
	}

}
