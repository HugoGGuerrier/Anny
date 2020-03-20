package one.anny.main.services;

import java.sql.SQLException;
import java.util.List;

import org.json.simple.JSONArray;

import one.anny.main.db.managers.FollowDatabaseManager;
import one.anny.main.tools.Security;
import one.anny.main.tools.exceptions.FollowException;
import one.anny.main.tools.models.FollowModel;

/**
 * This class contains all follow services
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class FollowServices {
	
	// ----- Services methods -----
	
	/**
	 * Try to insert the follow in the database
	 * 
	 * @param follow The follow to create in database
	 * @throws FollowException If there were an error during the service
	 * @throws SQLException 
	 */
	public static void createFollow(FollowModel follow) throws FollowException, SQLException {
		// Verify the follow parameters
		boolean valid = true;
		StringBuilder message = new StringBuilder();
		
		if(follow.getFollowedUserId() == null || !Security.isValidUserId(follow.getFollowedUserId())) {
			valid = false;
			message.append(" - Invalid followed user id : " + follow.getFollowedUserId());
		}
		if(follow.getFollowingUserId() == null || !Security.isValidUserId(follow.getFollowingUserId()) ) {
			valid = false;
			message.append(" - Invalid following user id : " + follow.getFollowingUserId());;
		}
		if(follow.getFollowDate() == null) {
			valid = false;
			message.append(" - Invalid follow date : null");
		}
		
		// If there is an error, throw an exception
		if(!valid) {
			
			throw new FollowException(message.toString());
			
		} else {
			
			// Call the follow database manager to insert a new following link
			FollowDatabaseManager.insertFollow(follow);
			
		}
	}
	
	/**
	 * Try to delete a follow from the database
	 * 
	 * @param follow The follow to delete
	 * @throws FollowException If there were an error during the service
	 * @throws SQLException 
	 */
	public static void deleteFollow(FollowModel follow) throws FollowException, SQLException {
		// Verify the follow Id (followingUserId + followedUserId)
		boolean valid = true;
		StringBuilder message = new StringBuilder();
		
		if(follow.getFollowedUserId() == null || !Security.isValidUserId(follow.getFollowedUserId())) {
			valid = false;
			message.append(" - Invalid followed id " + follow.getFollowedUserId());
		}
		if(follow.getFollowingUserId() == null || !Security.isValidUserId(follow.getFollowingUserId())) {
			valid = false;
			message.append(" - Invalid following id : " + follow.getFollowingUserId());
		}
		
		if(!valid) {
			
			throw new FollowException(message.toString());
			
		} else {
			
			// Call the follow database manager to delete a following link
			FollowDatabaseManager.deleteFollow(follow);
			
		}
	}
	
	/**
	 * Get all the follows from the specified model
	 * 
	 * @param follow The follow model
	 * @param isLike If we want a ISLIKE research (true) or a equals research (false)
	 * @return the list of follow that correspond to the model
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public static JSONArray searchFollow(FollowModel follow, boolean isLike) throws SQLException {
		// Call the follow database manager to search following links
		List<FollowModel> follows = FollowDatabaseManager.getFollows(follow, isLike);
		
		// Put all follows in a JSONArray
		JSONArray res = new JSONArray();
		
		for (FollowModel followModel : follows) {
			res.add(followModel.getJSON());
		}
		
		return res;
	}

}
