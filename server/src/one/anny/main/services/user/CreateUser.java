package one.anny.main.services.user;

import java.sql.SQLException;

import one.anny.main.db.managers.UserDatabaseManager;
import one.anny.main.tools.Security;
import one.anny.main.tools.exceptions.UserException;
import one.anny.main.tools.models.UserModel;

public class CreateUser {		
	
	// ----- Class methods -----
	
	
	/**
	 * Create an user in the database
	 * 
	 * @param user The user to insert in database
	 * @throws UserException If the user doesn't contains correct informations
	 * @throws SQLException If there is a problem with the database
	 */
	public static void createUser(UserModel user) throws UserException, SQLException {
		// Verify the user parameters
		boolean valid = true;
		StringBuilder message = new StringBuilder();
		
		if(user.getUserId() == null || !Security.isValidUserId(user.getUserId())) {
			valid = false;
			message.append(" - Invalid user id : " + user.getUserId());
		}
		if(user.getUserPseudo() == null || !Security.isStringNotEmpty(user.getUserPseudo())) {
			valid = false;
			message.append(" - Invalid user pseudo : " + user.getUserPseudo());
		}
		if(user.getUserName() == null || !Security.isStringNotEmpty(user.getUserName())) {
			valid = false;
			message.append(" - Invalid user name : " + user.getUserName());
		}
		if(user.getUserSurname() == null || !Security.isStringNotEmpty(user.getUserSurname())) {
			valid = false;
			message.append(" - Invalid user surname : " + user.getUserSurname());
		}
		if(user.getUserEmail() == null || !Security.isValidEmail(user.getUserEmail())) {
			valid = false;
			message.append(" - Invalid user email : " + user.getUserEmail());
		}
		if(user.getUserPassword() == null || !Security.isValidPassword(user.getUserPassword())) {
			valid = false;
			message.append(" - Invalid user password : " + user.getUserPassword());
		}
		if(user.getUserDate() == null) {
			valid = false;
			message.append(" - Invalid user date : null");
		}
		
		// If there is an error, throw an exception
		if(!valid) {
			
			throw new UserException(message.toString());
		
		} else {
			
			// Escape the HTML special characters
			user.setUserPseudo(Security.htmlEncode(user.getUserPseudo()));
			user.setUserName(Security.htmlEncode(user.getUserName()));
			user.setUserSurname(Security.htmlEncode(user.getUserSurname()));
			
			// Call the user database manager to insert a new user
			UserDatabaseManager.insertUser(user);
			
		}
	}
	
}
