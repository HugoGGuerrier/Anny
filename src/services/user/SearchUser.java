package services.user;

import java.sql.SQLException;
import java.util.List;

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
	 * @param isLike If we want a precise research or a is like research
	 * @return The list of user corresponding to the model
	 * @throws SQLException 
	 */
	public List<UserModel> searchUser(UserModel user, boolean isLike) throws SQLException {
		// Call the user database manager to search users
		return this.userDatabaseManager.getUsers(user, isLike);
	}

}
