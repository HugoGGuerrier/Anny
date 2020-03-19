package one.anny.main.services.follow;

import java.sql.SQLException;

import one.anny.main.db.managers.FollowDatabaseManager;
import one.anny.main.tools.Security;
import one.anny.main.tools.exceptions.FollowException;
import one.anny.main.tools.models.FollowModel;

public class CreateFollow {

	// ----- Class methods -----


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

}
