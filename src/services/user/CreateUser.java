package services.user;

import tools.exceptions.UserException;
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
	
	
	/**
	 * Create an user in the database and return the code
	 * 
	 * @param user The user to insert in database
	 * @throws UserException If the user insertion fails
	 */
	public void createUser(User user) throws UserException {
		// TODO : insertion en base de données de l'utilisateur
	}
	
}
