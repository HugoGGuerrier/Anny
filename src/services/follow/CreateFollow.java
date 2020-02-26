package services.follow;

import java.sql.SQLException;

import db.managers.FollowDatabaseManager;
import tools.Security;
import tools.exceptions.FollowException;
import tools.models.FollowModel;

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
		// TODO : Insérer le follow dans la base de donnée
		// Verify the follow parameters
		boolean valid = true;
		StringBuilder message = new StringBuilder();
		
		// ...
		
		
		// If there is an error, throw an exception
		if(!valid) {
			
			throw new FollowException(message.toString());
			
		} else {
			
			// Call the follow database manager to insert a new following link
			this.followDatabaseManager.insertFollow(follow);
			
		}
	}

}
