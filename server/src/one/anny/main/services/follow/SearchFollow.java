package one.anny.main.services.follow;

import java.sql.SQLException;
import java.util.List;

import org.json.simple.JSONArray;

import one.anny.main.db.managers.FollowDatabaseManager;
import one.anny.main.tools.models.FollowModel;

public class SearchFollow {

	// ----- Class methods -----


	/**
	 * Get all the follows from the specified model
	 * 
	 * @param follow The follow model
	 * @param isLike If we want a ISLIKE research (true) or a equals research (false)
	 * @return the list of follow that correspond to the model
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public static JSONArray searchFollow(FollowModel follow, boolean isLike) throws SQLException {
		// Call the follow database manager to search following links
		List<FollowModel> follows = FollowDatabaseManager.getFollows(follow, isLike);
		
		// Put all follows in a JSONArray
		JSONArray res = new JSONArray();
		
		for (FollowModel followModel : follows) {
			res.add(followModel.getJSON());
		}
		
		return res;
	}

}
