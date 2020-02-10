package tools.models;

public class Follow {
	
	// ----- Attributes -----
	
	
	private User followedUser;
	
	private User followingUser;
	
	
	// ----- Constructors -----
	
	
	public Follow() {
		this.followedUser = null;
		this.followingUser = null;
	}
	
	
	// ----- Getters -----
	
	
	public User getFollowedUser() {
		return this.followedUser;
	}

	public User getFollowingUser() {
		return this.followingUser;
	}
	
	
	// ----- Setters -----
	
	
	public void setFollowedUser(User followedUser) {
		this.followedUser = followedUser;
	}
	
	public void setFollowingUser(User followingUser) {
		this.followingUser = followingUser;
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
