package tools.models;

import java.sql.Date;

import org.json.simple.JSONObject;

/**
 * Class that represents a follow on the application
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class FollowModel {
	
	// ----- Attributes -----
	
	
	/** Followed user (DB key) */
	private String followedUserId;
	
	/** Following user (DB key) */
	private String followingUserId;
	
	/** Creation date of the follow */
	private Date followDate;
	
	
	// ----- Constructors -----
	
	
	/**
	 * Create a new follow from a JSON object
	 * 
	 * @param followJSON The follow JSON
	 */
	public FollowModel(JSONObject followJSON) {
		// TODO : Créer un follow en suivant le JSON
	}
	
	/**
	 * Create a new empty follow model
	 */
	public FollowModel() {
		this.followedUserId = null;
		this.followingUserId = null;
		this.followDate = null;
	}
	
	
	// ----- Getters -----
	
	
	public String getFollowedUserId() {
		return this.followedUserId;
	}

	public String getFollowingUserId() {
		return this.followingUserId;
	}
	
	public Date getFollowDate() {
		return this.followDate;
	}
	
	
	// ----- Setters -----
	
	
	public void setFollowedUserId(String userId) {
		this.followedUserId = userId;
	}
	
	public void setFollowingUserId(String userId) {
		this.followingUserId = userId;
	}
	
	public void setFollowDate(Date date) {
		this.followDate = date;
	}
	
	
	// ----- Standards methods -----
	
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof FollowModel)) {
			return false;
		} else {
			FollowModel f = (FollowModel) obj;
			return this.followedUserId.equals(f.followedUserId) && this.followingUserId.equals(f.followingUserId);
		}
	}
	

}
