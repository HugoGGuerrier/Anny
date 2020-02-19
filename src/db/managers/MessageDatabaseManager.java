package db.managers;

public class MessageDatabaseManager {

	// ----- Attributes -----
	
	
	/** */
	private static MessageDatabaseManager instance = null;
	
	
	// ----- Constructors ----- 
	
	
	private MessageDatabaseManager() {
		
	}
	
	public static MessageDatabaseManager getInstance() {
		if(MessageDatabaseManager.instance == null) {
			MessageDatabaseManager.instance = new MessageDatabaseManager();
		}
		return MessageDatabaseManager.instance;
	}
	
	
	// ----- Class Methods -----

	
}
