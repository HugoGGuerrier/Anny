package services.user;

import java.util.List;

import org.json.simple.JSONObject;

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


	public List<User> searchUser(User user) {
		// TODO : insertion en base de données de l'utilisateur

		return null;
	}

}
