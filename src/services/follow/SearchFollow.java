package services.follow;

import java.sql.SQLException;
import java.util.List;

import org.json.simple.JSONArray;

import db.managers.FollowDatabaseManager;
import tools.models.FollowModel;

public class SearchFollow {

	// ----- Attributes -----

	
	/**	The user database manager unique instance */
	private FollowDatabaseManager followDatabaseManager;

	/** The service unique instance (singleton) */
	private static SearchFollow instance = null;


	// ----- Constructors -----

	
	/**
	 * Construct a new service
	 */
	private SearchFollow() {

	}
	
	/**
	 * Get the service unique instance
	 * 
	 * @return The instance
	 */
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
	 * @param isLike If we want a ISLIKE research (true) or a equals research (false)
	 * @return the list of follow that correspond to the model
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public JSONArray searchFollow(FollowModel follow, boolean isLike) throws SQLException {
		// Call the follow database manager to search following links
		List<FollowModel> follows = this.followDatabaseManager.getFollows(follow, isLike);
		
		// Put all follows in a JSONArray
		JSONArray res = new JSONArray();
		
		for (FollowModel followModel : follows) {
			res.add(followModel.getJSON());
		}
		
		return res;
	}

}
