package services.user;

import tools.exceptions.UserException;
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
	
	
	/**
	 * Modify the wanted user identifying it by its ID
	 * 
	 * @param user The new value of the user
	 * @throws UserException If the user can't be modified
	 */
	public void modifyUser(User user) throws UserException {
		// TODO : faire les insertions en bd (comme il a toujours le même id c'est bon ^^)
	}
	
}
