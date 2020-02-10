package services.follow;

import tools.exceptions.FollowException;
import tools.exceptions.MessageException;
import tools.models.Follow;

public class CreateFollow {

	// ----- Attributes -----


	private static CreateFollow instance = null;


	// ----- Constructors -----


	private CreateFollow() {

	}

	public static CreateFollow getInstance() {
		if(CreateFollow.instance ==  null) {
			CreateFollow.instance = new CreateFollow();
		}
		return CreateFollow.instance;
	}


	// ----- Class methods -----


	/**
	 * Try to insert the follow in the database
	 * 
	 * @param follow The follow to create in database
	 * @throws FollowException If there were an error during the service
	 */
	public void createFollow(Follow follow) throws FollowException {
		// TODO : Insérer le follow dans la base de donnée
	}

}
