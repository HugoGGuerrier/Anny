package services.user;

import org.json.simple.JSONObject;

import tools.models.User;

public class CreateUser {
	
	// ----- Attributes -----

	
	private static CreateUser instance = null;
	
	
	// ----- Constructors -----
	
	
	private CreateUser() {
		
	}
	
	public static CreateUser getInstance() {
		if(CreateUser.instance ==  null) {
			CreateUser.instance = new CreateUser();
		}
		return CreateUser.instance;
	}
		
	
	// ----- Class methods -----
	
	
	public int createUser(User user) {
		// TODO : insertion en base de données de l'utilisateur
		
		return 0;
	}
	
}
