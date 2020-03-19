package one.anny.main.services.user;

import java.sql.SQLException;
import java.util.List;

import org.json.simple.JSONArray;

import one.anny.main.db.managers.UserDatabaseManager;
import one.anny.main.tools.Security;
import one.anny.main.tools.models.UserModel;

public class SearchUser {

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
	public static JSONArray searchUser(UserModel user, boolean isLike) throws SQLException {
		// Escape the HTML special characters
		if(user.getUserPseudo() != null) {
			user.setUserPseudo(Security.htmlEncode(user.getUserPseudo()));
		}
		if(user.getUserName() != null) {
			user.setUserName(Security.htmlEncode(user.getUserName()));
		}
		if(user.getUserSurname() != null) {
			user.setUserSurname(Security.htmlEncode(user.getUserSurname()));
		}
		
		// Call the user database manager to search users
		List<UserModel> users = UserDatabaseManager.getUsers(user, isLike);

		// Place the result inside a JSON array
		JSONArray res = new JSONArray();

		for(UserModel userRes : users) {
			res.add(userRes.getJSON());
		}

		// Return the result
		return res;
	}

}
