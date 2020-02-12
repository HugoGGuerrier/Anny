package services.follow;

import java.util.List;

import tools.models.FollowModel;

public class SearchFollow {

	// ----- Attributes -----


	private static SearchFollow instance = null;


	// ----- Constructors -----


	private SearchFollow() {

	}

	public static SearchFollow getInstance() {
		if(SearchFollow.instance ==  null) {
			SearchFollow.instance = new SearchFollow();
		}
		return SearchFollow.instance;
	}


	// ----- Class methods -----


	/**
	 * Get all the follows from the specified model
	 * 
	 * @param follow The follow model
	 * @return the list of follow that correspond to the model
	 */
	public List<FollowModel> searchFollow(FollowModel follow) {
		// TODO : Rechercher les follow selon le modèle
		
		return null;
	}

}
