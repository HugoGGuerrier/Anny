package one.anny.main.services.follow;

import java.sql.SQLException;

import one.anny.main.db.managers.FollowDatabaseManager;
import one.anny.main.tools.Security;
import one.anny.main.tools.exceptions.FollowException;
import one.anny.main.tools.models.FollowModel;

public class CreateFollow {

	// ----- Attributes -----


	/**	The user database manager unique instance */
	private FollowDatabaseManager followDatabaseManager;
	
	/** Security tool */
	private Security security;
	
	/** The service unique instance (singleton) */
	private static CreateFollow instance = null;


	// ----- Constructors -----

	
	/**
	 * Construct a new service
	 */
	private CreateFollow() {
		this.followDatabaseManager = FollowDatabaseManager.getInstance();
		this.security = Security.getInstance();
	}

	
	/**
	 * Get the service unique instance
	 * 
	 * @return The instance
	 */
	public static CreateFollow getInstance() {
		if(CreateFollow.instance ==  null) {
			CreateFollow.instance = new CreateFollow();
		}
		return CreateFollow.instance;
	}


	// ----- Class methods -----


	/**
	 * Try to insert the follow in the database
	 * 
	 * @param follow The follow to create in database
	 * @throws FollowException If there were an error during the service
	 * @throws SQLException 
	 */
	public void createFollow(FollowModel follow) throws FollowException, SQLException {
		// Verify the follow parameters
		boolean valid = true;
		StringBuilder message = new StringBuilder();
		
		if(follow.getFollowedUserId() == null || !this.security.isValidUserId(follow.getFollowedUserId())) {
			valid = false;
			message.append(" - Invalid followed user id : " + follow.getFollowedUserId());
		}
		if(follow.getFollowingUserId() == null || !this.security.isValidUserId(follow.getFollowingUserId()) ) {
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
			this.followDatabaseManager.insertFollow(follow);
			
		}
	}

}
