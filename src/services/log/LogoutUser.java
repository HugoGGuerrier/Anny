package services.log;

import tools.exceptions.SessionException;

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
	 * Logout and destroy the wanted session
	 * 
	 * @param sessionId The session ID
	 * @throws SessionException If the session doesn't exists
	 */
	public void logoutUser(String sessionId) throws SessionException {
		// TODO : Supprimer la session
	}

}
