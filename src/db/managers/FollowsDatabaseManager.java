package db.managers;

public class FollowsDatabaseManager {

	// ----- Attributes -----
	
	
	/** */
	private static FollowsDatabaseManager instance = null;
	
	
	// ----- Constructors ----- 
	
	
	private FollowsDatabaseManager() {
		
	}
	
	public static FollowsDatabaseManager getInstance() {
		if(FollowsDatabaseManager.instance == null) {
			FollowsDatabaseManager.instance = new FollowsDatabaseManager();
		}
		return FollowsDatabaseManager.instance;
	}
	
	
	// ----- Class Methods -----

	
}
