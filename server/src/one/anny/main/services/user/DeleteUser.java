package one.anny.main.services.user;

import java.sql.SQLException;

import one.anny.main.db.managers.UserDatabaseManager;
import one.anny.main.tools.Security;
import one.anny.main.tools.exceptions.UserException;
import one.anny.main.tools.models.UserModel;

public class DeleteUser {

	// ----- Class methods -----
	
	
	/**
	 * Delete an user from the database (or more if the user match with multiple users)
	 * 
	 * @param user The user model to look for
	 * @throws UserException If there were no user deleted
	 * @throws SQLException If there is a problem with the database
	 */
	public static void deleteUser(UserModel user) throws UserException, SQLException {
		// Verify the user Id
		boolean valid = true;
		StringBuilder message = new StringBuilder();
		
		if(user.getUserId() == null || !Security.isValidUserId(user.getUserId())) {
			valid = false;
			message.append(" - Invalid user id : " + user.getUserId());
		}
		
		// If there is an error, throw an exception
		if(!valid) {
			
			throw new UserException(message.toString());
			
		} else {
			
			// Call the user database manager to delete an user
			UserDatabaseManager.deleteUser(user);
			
		}
	}

}
