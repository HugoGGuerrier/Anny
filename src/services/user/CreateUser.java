package services.user;

import java.sql.SQLException;

import db.managers.UserDatabaseManager;
import tools.Security;
import tools.exceptions.UserException;
import tools.models.UserModel;

public class CreateUser {
	
	// ----- Attributes -----

	
	/**	The user database manager unique instance */
	private UserDatabaseManager userDatabaseManager;
	
	/** Security tool */
	private Security security;
	
	/** The service unique instance (singleton) */
	private static CreateUser instance = null;
	
	
	// ----- Constructors -----
	
	
	/**
	 * Construct a new service
	 */
	private CreateUser() {
		this.userDatabaseManager = UserDatabaseManager.getInstance();
		this.security = Security.getInstance();
	}
	
	/**
	 * Get the service unique instance
	 * 
	 * @return The instance
	 */
	public static CreateUser getInstance() {
		if(CreateUser.instance ==  null) {
			CreateUser.instance = new CreateUser();
		}
		return CreateUser.instance;
	}
		
	
	// ----- Class methods -----
	
	
	/**
	 * Create an user in the database
	 * 
	 * @param user The user to insert in database
	 * @throws UserException If the user doesn't contains correct informations
	 * @throws SQLException If there is a problem with the database
	 */
	public void createUser(UserModel user) throws UserException, SQLException {
		// Verify the user parameters
		boolean valid = true;
		StringBuilder message = new StringBuilder();
		
		if(user.getUserId() == null || !this.security.isValidUserId(user.getUserId())) {
			valid = false;
			message.append(" - Invalid user id : " + user.getUserId());
		}
		if(user.getUserPseudo() == null || user.getUserPseudo().equals("")) {
			valid = false;
			message.append(" - Invalid user pseudo : " + user.getUserPseudo());
		}
		if(user.getUserName() == null || user.getUserName().equals("")) {
			valid = false;
			message.append(" - Invalid user name : " + user.getUserName());
		}
		if(user.getUserSurname() == null || user.getUserSurname().equals("")) {
			valid = false;
			message.append(" - Invalid user surname : " + user.getUserSurname());
		}
		if(user.getUserEmail() == null || !this.security.isValidEmail(user.getUserEmail())) {
			valid = false;
			message.append(" - Invalid user email : " + user.getUserEmail());
		}
		if(user.getUserPassword() == null || !this.security.isValidPassword(user.getUserPassword())) {
			valid = false;
			message.append(" - Invalid user password : " + user.getUserPassword());
		}
		
		// If there is an error, throw an exception
		if(!valid) {
			
			throw new UserException(message.toString());
		
		} else {
			
			// Call the user database manager to insert a new user
			this.userDatabaseManager.insertUser(user);
			
		}
	}
	
}
