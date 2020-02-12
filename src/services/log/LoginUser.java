package services.log;

import tools.exceptions.SessionException;
import tools.models.UserModel;

public class LoginUser {
	
	// ----- Attributes -----
	
	
	private static LoginUser instance = null;
	
	
	// ----- Constructors -----
	
	
	private LoginUser() {
		
	}
	
	public static LoginUser getInstance() {
		if(LoginUser.instance == null) {
			LoginUser.instance = new LoginUser();
		}
		return LoginUser.instance;
	}
	
	
	// ----- Class methods -----
	
	
	/**
	 * Try to log the wanted user with its email and password
	 * 
	 * @param user The user you want to log in
	 * @return The session ID
	 * @throws SessionException If the service can't create the session
	 */
	public String loginUser(UserModel user) throws SessionException {
		// TODO : Connecter l'utilisateur et stocker et retourner le token de session
		
		return null;
	}

}
