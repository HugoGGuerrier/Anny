package services.user;

import java.sql.SQLException;
import java.util.List;

import org.json.simple.JSONArray;

import db.managers.UserDatabaseManager;
import tools.models.UserModel;

public class SearchUser {

	// ----- Attributes -----


	/**	The user database manager unique instance */
	private UserDatabaseManager userDatabaseManager;

	/** The service unique instance (singleton) */
	private static SearchUser instance = null;


	// ----- Constructors -----


	/**
	 * Construct a new service
	 */
	private SearchUser() {
		this.userDatabaseManager = UserDatabaseManager.getInstance();
	}

	/**
	 * Get the service unique instance
	 * 
	 * @return The instance
	 */
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
	 * @param isLike If we want a ISLIKE research (true) or a equals research (false)
	 * @return The list of user corresponding to the model
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public JSONArray searchUser(UserModel user, boolean isLike) throws SQLException {
		// Call the user database manager to search users
		List<UserModel> users = this.userDatabaseManager.getUsers(user, isLike);
		
		// Place the result inside a JSON array
		JSONArray res = new JSONArray();
		for(UserModel userRes : users) {
			res.add(userRes.getJSON());
		}
		
		// Return the result
		return res;
	}

}
