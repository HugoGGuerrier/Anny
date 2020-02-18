package services.user;

import java.sql.SQLException;

import tools.exceptions.UserException;
import tools.models.UserModel;

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
	 * @throws UserException If the user doesn't contains correct informations
	 * @throws SQLException if there is a problem with the database
	 */
	public void createUser(UserModel user) throws UserException, SQLException {
		// TODO : insertion en base de données de l'utilisateur
	}
	
}
