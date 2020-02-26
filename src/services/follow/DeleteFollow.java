package services.follow;

import java.sql.SQLException;

import db.managers.FollowDatabaseManager;
import tools.Security;
import tools.exceptions.FollowException;
import tools.models.FollowModel;

public class DeleteFollow {

	// ----- Attributes -----

	
	/**	The user database manager unique instance */
	private FollowDatabaseManager followDatabaseManager;
	
	/** Security tool */
	private Security security;
	
	/** The service unique instance (singleton) */
	private static DeleteFollow instance = null;


	// ----- Constructors -----


	private DeleteFollow() {

	}

	public static DeleteFollow getInstance() {
		if(DeleteFollow.instance ==  null) {
			DeleteFollow.instance = new DeleteFollow();
		}
		return DeleteFollow.instance;
	}


	// ----- Class methods -----


	/**
	 * Try to delete a follow from the database
	 * 
	 * @param follow The follow to delete
	 * @throws FollowException If there were an error during the service
	 * @throws SQLException 
	 */
	public void deleteFollow(FollowModel follow) throws FollowException, SQLException {
		// Verify the follow Id (followingUserId + followedUserId)
		boolean valid = true;
		StringBuilder message = new StringBuilder();
		
		// TODO : verifications
		
		if(!valid) {
			
			throw new FollowException(message.toString());
			
		} else {
			
			// Call the follow database manager to delete a following link
			this.followDatabaseManager.deleteFollow(follow);
			
		}
	}

}
