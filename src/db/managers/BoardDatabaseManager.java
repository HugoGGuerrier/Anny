package db.managers;

public class BoardDatabaseManager {

	// ----- Attributes -----
	
	
	/** */
	private static BoardDatabaseManager instance = null;
	
	
	// ----- Constructors ----- 
	
	
	private BoardDatabaseManager() {
		
	}
	
	public static BoardDatabaseManager getInstance() {
		if(BoardDatabaseManager.instance == null) {
			BoardDatabaseManager.instance = new BoardDatabaseManager();
		}
		return BoardDatabaseManager.instance;
	}
	
	
	// ----- Class Methods -----

	
}
