package services.user;

import java.sql.SQLException;

import db.managers.UserDatabaseManager;
import tools.Security;
import tools.exceptions.UserException;
import tools.models.UserModel;

public class DeleteUser {

	// ----- Attributes -----
	
	/**	The user database manager unique instance */
	private UserDatabaseManager userDatabaseManager;
	
	/** Security tool */
	private Security security;
	
	/** The service unique instance (singleton) */
	private static DeleteUser instance = null;


	// ----- Constructors -----

	
	/**
	 * Construct a new service
	 */
	private DeleteUser() {
		this.userDatabaseManager = UserDatabaseManager.getInstance();
		this.security = Security.getInstance();
	}
	
	/**
	 * Get the service unique instance
	 * 
	 * @return The instance
	 */
	public static DeleteUser getInstance() {
		if(DeleteUser.instance ==  null) {
			DeleteUser.instance = new DeleteUser();
		}
		return DeleteUser.instance;
	}


	// ----- Class methods -----
	
	
	/**
	 * Delete an user from the database (or more if the user match with multiple users)
	 * 
	 * @param user The user model to look for
	 * @throws UserException If there were no user deleted
	 * @throws SQLException If there is a problem with the database
	 */
	public void deleteUser(UserModel user) throws UserException, SQLException {
		// Verify the user Id
		boolean valid = true;
		StringBuilder message = new StringBuilder();
		
		if(user.getUserId() == null || !this.security.isValidUserId(user.getUserId())) {
			valid = false;
			message.append(" - Invalid user id : " + user.getUserId());
		}
		
		// If there is an error, throw an exception
		if(!valid) {
			
			throw new UserException(message.toString());
			
		} else {
			
			// Call the user database manager to delete an user
			this.userDatabaseManager.deleteUser(user);
			
		}
	}

}
