package services.user;

import org.json.simple.JSONObject;

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


	public int logoutUser(User user) {
		// TODO : Supprimer la connexion de l'utilisateur

		return 0;
	}

}
