package one.anny.main.services.message;

import java.sql.SQLException;
import java.util.List;

import one.anny.main.db.managers.MessageDatabaseManager;
import one.anny.main.tools.Security;
import one.anny.main.tools.exceptions.MessageException;
import one.anny.main.tools.exceptions.MongoException;
import one.anny.main.tools.models.MessageModel;

public class CreateMessage {

	// ----- Class methods -----


	/**
	 * Try to insert the message in database
	 * 
	 * @param message The message to insert in database WITHOUT ITS ID
	 * @throws MessageException If the message already exists or if there is an database exception
	 * @throws MongoException If the message cannot be inserted in the database
	 */
	public static synchronized void createMessage(MessageModel message, String parentMessageId) throws MessageException, MongoException, SQLException {
		// Verify the message parameters
		boolean valid = true;
		StringBuilder errorMessage = new StringBuilder();
		
		if(message.getMessageText() == null || !Security.isStringNotEmpty(message.getMessageText())) {
			valid = false;
			errorMessage.append(" - Invalid message text : " + message.getMessageText());
		}
		if(message.getMessageBoardName() == null || !Security.isValidBoardName(message.getMessageBoardName())) {
			valid = false;
			errorMessage.append(" - Invalid message board name : " + message.getMessageBoardName());
		}
		if(message.getMessagePosterId() == null || !Security.isValidUserId(message.getMessagePosterId())) {
			valid = false;
			errorMessage.append(" - Invalid message poster id : " + message.getMessagePosterId());
		}
		if(message.getMessageDate() == null) {
			valid = false;
			errorMessage.append(" - Invalid message date : null");
		}
		
		// If there is an error, throw an error
		if(!valid) {
			
			throw new MessageException(errorMessage.toString());
			
		} else {
			
			// Escape the HTML special characters
			message.setMessageText(Security.htmlEncode(message.getMessageText()));
			message.setMessageBoardName(Security.htmlEncode(message.getMessageBoardName()));
			
			// Get message ID dynamically
			if(parentMessageId != null && !parentMessageId.equals("")) {
				
				// Get the message parent
				MessageModel parentFilter = new MessageModel();
				parentFilter.setMessageId(parentMessageId);				
				List<MessageModel> parents = MessageDatabaseManager.getMessage(parentFilter, false);
				if(parents.size() == 1) {
					
					MessageModel parent = parents.get(0);
					message.setMessageId(parent.getNextAnswerId());
					
				} else {
					
					throw new MessageException("Parent message not found : " + parentMessageId);
					
				}
				
			} else {
				
				// Get the next root message ID
				message.setMessageId(MessageDatabaseManager.getNextRootMessageId());
				
			}
			
			// Insert the new message
			MessageDatabaseManager.insertMessage(message);
			
		}
	}

}
