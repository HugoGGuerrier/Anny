package services.message;

import java.util.List;

import org.json.simple.JSONArray;

import db.managers.MessageDatabaseManager;
import tools.exceptions.MongoException;
import tools.models.MessageModel;

public class SearchMessage {

	// ----- Attributes -----


	/** Database manager */
	private MessageDatabaseManager messageDatabaseManager;
	
	/** The unique instance of this service (singleton) */
	private static SearchMessage instance = null;


	// ----- Constructors -----


	/**
	 * Construct a new search message service
	 */
	private SearchMessage() {
		this.messageDatabaseManager = MessageDatabaseManager.getInstance();
	}

	/**
	 * Get the unique service instance
	 * 
	 * @return The instance
	 */
	public static SearchMessage getInstance() {
		if(SearchMessage.instance ==  null) {
			SearchMessage.instance = new SearchMessage();
		}
		return SearchMessage.instance;
	}


	// ----- Class methods -----


	/**
	 * Get a list of messages corresponding with the message model
	 * 
	 * @param message The message model
	 * @return The list of messages
	 * @throws MongoException 
	 */
	@SuppressWarnings("unchecked")
	public JSONArray searchMessage(MessageModel message, boolean isRegex) {
		// Call the database manager to get the messages
		List<MessageModel> messages = this.messageDatabaseManager.getMessage(message, isRegex);
		
		// Place the result in a JSON array
		JSONArray res = new JSONArray();
		for (MessageModel messageModel : messages) {
			res.add(messageModel);
		}

		// Return the result
		return res;
	}

}
