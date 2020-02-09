package services.user;

import tools.models.User;

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
	 * @return The session ID if the log in works, null else
	 */
	public String loginUser(User user) {
		// TODO : Connecter l'utilisateur et stocker et retourner le token de session
		
		return null;
	}

}
