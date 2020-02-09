package tools;

import java.util.Date;
import java.util.HashMap;

import tools.models.User;

public class Session {
	
	// ----- Attributes -----
	
	
	/** The sessions's unique id */
	private String sessionId;
	
	/** The user the the session is related to */
	private User user;
	
	/** The time that the session will still alive (seconds) */
	private long timeToLive;
	
	/** The date when the session were created (seconds) */
	private long creationDate;
	
	/** The session's storing array */
	private HashMap<String, String> sessionArray;
	
	
	// ----- Constructors -----
	
	
	/**
	 * Construct a new Session with its ID and its user
	 * 
	 * @param sessionId The session's ID
	 * @param user The user related to the session
	 */
	public Session(String sessionId, User user) {
		this.sessionId = sessionId;
		this.user = user;
		this.timeToLive = Config.getSessionsTimeToLive();
		this.creationDate = new Date().getTime() / 1000;
		
		this.sessionArray = new HashMap<String, String>();
	}
	
	
	// ----- Getters -----
	
	
	public String getSessionId() {
		return this.sessionId;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public long getTimeToLive() {
		return this.timeToLive;
	}
	
	public long getCreationDate() {
		return this.creationDate;
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
	 * Get if the session is expired at the current date
	 */
	public boolean isExpired() {
		long currentDate = new Date().getTime() / 1000;
		return currentDate - this.creationDate > this.timeToLive;
	}

}
