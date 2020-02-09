package services.user;

import org.json.simple.JSONObject;

import tools.models.User;

public class ModifyUser {

	// ----- Attributes -----

	
	private static ModifyUser instance = null;


	// ----- Constructors -----

	
	private ModifyUser() {

	}

	public static ModifyUser getInstance() {
		if(ModifyUser.instance ==  null) {
			ModifyUser.instance = new ModifyUser();
		}
		return ModifyUser.instance;
	}


	// ----- Class methods -----
	
	
	public int modifyUser(User user) {
		// TODO : faire les insertions en bd (comme il a toujours le même id c'est bon ^^)
		
		return 0;
	}
	
}
