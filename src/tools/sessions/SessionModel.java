package tools.sessions;

import java.util.Date;

import org.json.simple.JSONObject;

import tools.Config;
import tools.exceptions.SessionException;
import tools.models.UserModel;

/**
 * This class represent an HTTP session with params and the user related to it
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class SessionModel {

	// ----- Attributes -----


	/** The session's unique id */
	private String sessionId;

	/** User's ID related to the session */
	private String userId;

	/** The time that the session will still alive (seconds) */
	private Long timeToLive;

	/** The date when the session were created (seconds) */
	private Long lastActionDate;

	/** The session's storing array */
	private JSONObject sessionAttriutes;


	// ----- Constructors -----


	/**
	 * Construct a new Session with its ID and its user with the las action date to now
	 * 
	 * @param sessionId The session ID
	 * @param user The user related to the session
	 */
	public SessionModel(String sessionId, UserModel user) {
		this.sessionId = sessionId;
		this.userId = user.getUserId();
		this.timeToLive = Config.getSessionTimeToLive();
		this.sessionAttriutes = new JSONObject();

		this.action();
	}
	
	/**
	 * Construct an anonymous session without user linked to it
	 * 
	 * @param sessionId The session ID
	 */
	public SessionModel(String sessionId) {
		this(sessionId, new UserModel());
	}

	/**
	 * Construct a new session from a JSON
	 * 
	 * @param sessionJson The JSON of the session you want to create
	 */
	public SessionModel(JSONObject sessionJson) throws SessionException {
		this.sessionId = (String) sessionJson.get("sessionId");
		this.userId = (String) sessionJson.get("userId");
		this.timeToLive = (Long) sessionJson.get("timeToLive");
		this.lastActionDate = (Long) sessionJson.get("lastActionDate");
		this.sessionAttriutes = (JSONObject) sessionJson.get("sessionAttriutes");
		
		if(this.sessionId == null || this.timeToLive == null || this.lastActionDate == null || this.sessionAttriutes == null) {
			throw new SessionException("Cannot create a session from a corrupted json");
		}
	}


	// ----- Getters -----


	public String getSessionId() {
		return this.sessionId;
	}


	public String getUserId() {
		return this.userId;
	}

	public long getTimeToLive() {
		return this.timeToLive;
	}

	public long getLastActionDate() {
		return this.lastActionDate;
	}
	
	/**
	 * Get if the session is anonymous
	 * 
	 * @return True if the session is anonymous
	 */
	public boolean isAnonymous() {
		return this.userId == null;
	}
	
	/**
	 * Get if the session is admin
	 * 
	 * @return True if the session is an admin session
	 */
	public boolean isAdmin() {
		return Boolean.parseBoolean(this.getAttribute("adminSession"));
	}
	
	
	// ----- Setters -----
	
	
	public void setUserId(String id) {
		this.userId = id;
	}
	
	public void setAdmin(boolean isAdmin) {
		this.putAttribute("adminSession", String.valueOf(isAdmin));
	}


	// ----- Class methods -----


	/**
	 * Get the session representation in a JSON object
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getJSON() {
		// Prepare the result object
		JSONObject res = new JSONObject();

		// Set the result
		res.put("sessionId", this.sessionId);
		res.put("userId", this.userId);
		res.put("timeToLive", this.timeToLive);
		res.put("lastActionDate", this.lastActionDate);
		res.put("sessionAttriutes", this.sessionAttriutes);

		// Returning the result
		return res;
	}

	/**
	 * Get a value from the session array
	 * 
	 * @param key The key of the value to get
	 * @return The value if it exists, null else
	 */
	public String getAttribute(String key) {
		return (String) this.sessionAttriutes.get(key);
	}

	/**
	 * Put a value in the session array
	 * 
	 * @param key The key
	 * @param value The value
	 */
	@SuppressWarnings("unchecked")
	public void putAttribute(String key, String value) {
		this.sessionAttriutes.put(key, value);
	}

	/**
	 * Remove a value from the session array
	 * 
	 * @param key The key of the value to remove
	 */
	public void removeAttribute(String key) {
		this.sessionAttriutes.remove(key);
	}

	/**
	 * Update the last action to now
	 */
	public void action() {
		this.lastActionDate = new Date().getTime() / 1000;
	}

	/**
	 * Get if the session is expired at the current date
	 */
	public boolean isExpired() {
		if(this.lastActionDate > 0) {
			long currentDate = new Date().getTime() / 1000;
			return currentDate - this.lastActionDate > this.timeToLive;
		}
		return true;
	}

}
