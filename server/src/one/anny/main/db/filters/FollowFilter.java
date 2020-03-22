package one.anny.main.db.filters;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * A filter to make query on the FOLLOW table
 *
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class FollowFilter extends AbstractFilter {

	// ----- Attributes -----
	
	
	/** All followed user id to look for */
	private Set<String> followedUserIdSet;
	
	/** The following user id to search */
	private Set<String> followingUserIdSet;
	
	/** Dates that you want to look for */
	private Set<Date> followDateSet;
	
	
	// ----- Constructors -----
	
	
	/**
	 * Construct a new filter with default void value
	 */
	public FollowFilter() {
		this.followedUserIdSet = new HashSet<String>();
		this.followingUserIdSet = new HashSet<String>();
		this.followDateSet = new HashSet<Date>();
	}
	
	
	// ----- Getters -----
	
	
	public Set<String> getFollowedUserIdSet() {
		return this.followedUserIdSet;
	}
	
	public Set<String> getFollowingUserIdSet() {
		return this.followingUserIdSet;
	}
	
	public Set<Date> getFollowDateSet() {
		return this.followDateSet;
	}
	
	
	// ----- Setters -----
	
	
	// --- Followed user id
	
	public void setFollowedUserIdSet(Set<String> followedUserIdSet) {
		this.followedUserIdSet = followedUserIdSet;
	}
	
	public void addFollowedUserId(String followedUserId) {
		this.followedUserIdSet.add(followedUserId);
	}
	
	// --- Following user id
	
	public void setFollowingUserIdSet(Set<String> followingUserIdSet) {
		this.followingUserIdSet = followingUserIdSet;
	}
	
	public void addFollowingUserId(String followingUserId) {
		this.followingUserIdSet.add(followingUserId);
	}
	
	// --- Follow date
	
	public void setFollowDateSet(Set<Date> followDateSet) {
		this.followDateSet = followDateSet;
	}
	
	public void addFollowDate(Date followDate) {
		this.followDateSet.add(followDate);
	}
	
}
