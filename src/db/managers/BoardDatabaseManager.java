package db.managers;

public class BoardDatabaseManager {

	// ----- Attributes -----
	
	
	/** Unique instance of the manager (singleton) */
	private static BoardDatabaseManager instance = null;
	
	
	// ----- Constructors ----- 
	
	
	/**
	 * Construct a new empty board database manager
	 */
	private BoardDatabaseManager() {
		
	}
	
	/**
	 * Get the instance of board database manager
	 * 
	 * @return The manager
	 */
	public static BoardDatabaseManager getInstance() {
		if(BoardDatabaseManager.instance == null) {
			BoardDatabaseManager.instance = new BoardDatabaseManager();
		}
		return BoardDatabaseManager.instance;
	}
	
	
	// ----- Class Methods -----

	
}
