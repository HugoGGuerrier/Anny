package tools.models;

import java.util.Date;

/**
 * Class that represents a follow on the application
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class FollowModel {
	
	// ----- Attributes -----
	
	
	/** Followed user */
	private UserModel followedUser;
	
	/** Following user */
	private UserModel followingUser;
	
	/** Creation date of the follow */
	private Date followDate = null;
	
	
	// ----- Constructors -----
	
	
	/**
	 * Create a follow with the wanted users
	 * 
	 * @param followed The user that follows
	 * @param following The user that is followed
	 */
	public FollowModel(UserModel followed, UserModel following) {
		this.followedUser = followed;
		this.followingUser = following;
	}
	
	/**
	 * Create a new follow with default value null
	 */
	public FollowModel() {
		this(null, null);
	}
	
	
	// ----- Getters -----
	
	
	public UserModel getFollowedUser() {
		return this.followedUser;
	}

	public UserModel getFollowingUser() {
		return this.followingUser;
	}
	
	public Date getFollowDate() {
		return this.followDate;
	}
	
	
	// ----- Setters -----
	
	
	public void setFollowedUser(UserModel followedUser) {
		this.followedUser = followedUser;
	}
	
	public void setFollowingUser(UserModel followingUser) {
		this.followingUser = followingUser;
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
			FollowModel follow = (FollowModel) obj;
			
			return this.followedUser.equals(follow.followedUser) && this.followingUser.equals(follow.followingUser);
		}
	}
	

}
