package services.follow;

import tools.exceptions.FollowException;
import tools.models.FollowModel;

public class DeleteFollow {

	// ----- Attributes -----


	private static DeleteFollow instance = null;


	// ----- Constructors -----


	private DeleteFollow() {

	}

	public static DeleteFollow getInstance() {
		if(DeleteFollow.instance ==  null) {
			DeleteFollow.instance = new DeleteFollow();
		}
		return DeleteFollow.instance;
	}


	// ----- Class methods -----


	/**
	 * Try to delete a follow from the database
	 * 
	 * @param follow The follow to delete
	 * @throws FollowException If there were an error during the service
	 */
	public void deleteFollow(FollowModel follow) throws FollowException {
		// TODO : Supprimer le follow de la base de données
	}

}
