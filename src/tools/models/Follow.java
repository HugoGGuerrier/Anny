package tools.models;

import java.util.Date;

/**
 * Class that represents a follow on the application
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class Follow {
	
	// ----- Attributes -----
	
	
	/** Followed user */
	private User followedUser;
	
	/** Following user */
	private User followingUser;
	
	/** Creation date of the follow */
	private Date followDate = null;
	
	
	// ----- Constructors -----
	
	
	/**
	 * Create a follow with the wanted users
	 * 
	 * @param followed The user that follows
	 * @param following The user that is followed
	 */
	public Follow(User followed, User following) {
		this.followedUser = followed;
		this.followingUser = following;
	}
	
	/**
	 * Create a new follow with default value null
	 */
	public Follow() {
		this(null, null);
	}
	
	
	// ----- Getters -----
	
	
	public User getFollowedUser() {
		return this.followedUser;
	}

	public User getFollowingUser() {
		return this.followingUser;
	}
	
	public Date getFollowDate() {
		return this.followDate;
	}
	
	
	// ----- Setters -----
	
	
	public void setFollowedUser(User followedUser) {
		this.followedUser = followedUser;
	}
	
	public void setFollowingUser(User followingUser) {
		this.followingUser = followingUser;
	}
	
	public void setFollowDate(Date date) {
		this.followDate = date;
	}
	
	
	// ----- Standards methods -----
	
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Follow)) {
			return false;
		} else {
			Follow follow = (Follow) obj;
			
			return this.followedUser.equals(follow.followedUser) && this.followingUser.equals(follow.followingUser);
		}
	}
	

}
