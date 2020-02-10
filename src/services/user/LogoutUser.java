package services.user;

import tools.exceptions.UserException;
import tools.models.User;

public class LogoutUser {

	// ----- Attributes -----


	private static LogoutUser instance = null;


	// ----- Constructors -----


	private LogoutUser() {

	}

	public static LogoutUser getInstance() {
		if(LogoutUser.instance == null) {
			LogoutUser.instance = new LogoutUser();
		}
		return LogoutUser.instance;
	}


	// ----- Class methods -----


	/**
	 * Logout the user if the session exists
	 * 
	 * @param user The user to log out
	 * @throws UserException If the session or the user doesn't exists
	 */
	public void logoutUser(User user) throws UserException {
		// TODO : Supprimer la connexion de l'utilisateur
	}

}
