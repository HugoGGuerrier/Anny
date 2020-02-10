package services.user;

import tools.exceptions.UserException;
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
	
	
	/**
	 * Delete an user from the database (or more if the user match with multiple users)
	 * 
	 * @param user The user model to look for
	 * @throws UserException If there were no user deleted
	 */
	public void deleteUser(User user) throws UserException {
		// TODO : Supprimer le ou les utilisateurs de la base de données
	}

}
