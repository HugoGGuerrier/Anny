package one.anny.main.services.message;

import java.util.List;

import org.json.simple.JSONArray;

import one.anny.main.db.managers.MessageDatabaseManager;
import one.anny.main.tools.Security;
import one.anny.main.tools.exceptions.MongoException;
import one.anny.main.tools.models.MessageModel;

public class SearchMessage {
	
	// ----- Class methods -----


	/**
	 * Get a list of messages corresponding with the message model
	 * 
	 * @param message The message model
	 * @return The list of messages
	 * @throws MongoException 
	 */
	@SuppressWarnings("unchecked")
	public static JSONArray searchMessage(MessageModel message, boolean isRegex) {
		// Escape the HTML special characters to make research works
		if(message.getMessageText() != null) {
			message.setMessageText(Security.htmlEncode(message.getMessageText()));
		}
		if(message.getMessageBoardName() != null) {
			message.setMessageBoardName(Security.htmlEncode(message.getMessageBoardName()));
		}
		
		// Call the database manager to get the messages
		List<MessageModel> messages = MessageDatabaseManager.getMessage(message, isRegex);
		
		// Place the result in a JSON array
		JSONArray res = new JSONArray();
		for (MessageModel messageModel : messages) {
			res.add(messageModel.getJSON());
		}

		// Return the result
		return res;
	}

}
