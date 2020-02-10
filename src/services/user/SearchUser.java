package services.user;

import java.util.List;

import tools.models.User;

public class SearchUser {

	// ----- Attributes -----


	private static SearchUser instance = null;


	// ----- Constructors -----


	private SearchUser() {

	}

	public static SearchUser getInstance() {
		if(SearchUser.instance ==  null) {
			SearchUser.instance = new SearchUser();
		}
		return SearchUser.instance;
	}


	// ----- Class methods -----


	/**
	 * Get an user list corresponding with the research user model
	 * 
	 * @param user The user model to look for
	 * @return The list of user corresponding to the model
	 */
	public List<User> searchUser(User user) {
		// TODO : insertion en base de données de l'utilisateur

		return null;
	}

}
