package services.user;

import org.json.simple.JSONObject;

import tools.models.User;

public class DeleteUser {

	// ----- Attributes -----

	private static DeleteUser instance = null;


	// ----- Constructors -----

	private DeleteUser() {

	}

	public static DeleteUser getInstance() {
		if(DeleteUser.instance ==  null) {
			DeleteUser.instance = new DeleteUser();
		}
		return DeleteUser.instance;
	}


	// ----- Class methods -----
	
	
	public JSONObject deleteUser(User user) {
		// TODO : Les insertions en bd en fonction du param		
		
		return null;
	}

}
