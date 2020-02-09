package services.user;

import org.json.simple.JSONObject;

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
	
	
	public int loginUser(User user) {
		// TODO : Connecter l'utilisateur et stocker le token de session
		
		return 0;
	}

}
