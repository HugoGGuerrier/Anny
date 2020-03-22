package one.anny.main.services;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;

import one.anny.main.db.filters.UserFilter;
import one.anny.main.db.managers.UserDatabaseManager;
import one.anny.main.tools.Security;
import one.anny.main.tools.exceptions.UserException;
import one.anny.main.tools.models.UserModel;

/**
 * This class contains all user services
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class UserServices {
	
	// ----- Services methods -----
	
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
	
	/**
	 * Modify the wanted user identified by its ID
	 * 
	 * @param user The new value of the user
	 * @throws UserException If the user can't be modified
	 * @throws SQLException If there is a problem with the database
	 */
	public static void modifyUser(UserModel user) throws UserException, SQLException {
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
		if(user.isUserAdmin() == null) {
			valid = false;
			message.append(" - Invalid user admin : " + user.isUserAdmin());
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
			UserDatabaseManager.updateUser(user);

		}
	}
	
	/**
	 * Get an user list corresponding with the research user model
	 * 
	 * @param user The user model to look for
	 * @param isLike If we want a ISLIKE research (true) or a equals research (false)
	 * @return The list of user corresponding to the model
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public static JSONArray searchUser(UserFilter filter, boolean isLike, boolean adminRequest) throws SQLException {
		// Escape the HTML special characters
		if(filter.getUserPseudoSet().size() > 0) {
			Set<String> escapedPseudoSet = new HashSet<String>();
			for(String pseudo : filter.getUserPseudoSet()) {
				escapedPseudoSet.add(Security.htmlEncode(pseudo));
			}
			filter.setUserPseudoSet(escapedPseudoSet);
		}
		
		if(filter.getUserNameSet().size() > 0) {
			Set<String> escapedNameSet = new HashSet<String>();
			for(String name : filter.getUserNameSet()) {
				escapedNameSet.add(Security.htmlEncode(name));
			}
			filter.setUserNameSet(escapedNameSet);
		}
		
		if(filter.getUserSurnameSet().size() > 1) {
			Set<String> escapedSurnameSet = new HashSet<String>();
			for(String surname : filter.getUserSurnameSet()) {
				escapedSurnameSet.add(Security.htmlEncode(surname));
			}
			filter.setUserSurnameSet(escapedSurnameSet);
		}
		
		// Call the user database manager to search users
		List<UserModel> users = UserDatabaseManager.getUsers(filter, isLike);

		// Place the result inside a JSON array
		JSONArray res = new JSONArray();

		for(UserModel userRes : users) {
			res.add(userRes.getJSON(adminRequest));
		}

		// Return the result
		return res;
	}

}
