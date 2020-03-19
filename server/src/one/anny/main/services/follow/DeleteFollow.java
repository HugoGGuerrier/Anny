package one.anny.main.services.follow;

import java.sql.SQLException;

import one.anny.main.db.managers.FollowDatabaseManager;
import one.anny.main.tools.Security;
import one.anny.main.tools.exceptions.FollowException;
import one.anny.main.tools.models.FollowModel;

public class DeleteFollow {

	// ----- Class methods -----


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

}
