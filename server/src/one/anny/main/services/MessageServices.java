package one.anny.main.services;

import java.sql.SQLException;
import java.util.List;

import org.json.simple.JSONArray;

import one.anny.main.db.managers.MessageDatabaseManager;
import one.anny.main.tools.Security;
import one.anny.main.tools.exceptions.MessageException;
import one.anny.main.tools.exceptions.MongoException;
import one.anny.main.tools.models.MessageModel;

/**
 * This class contains all message services
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class MessageServices {
	
	// ----- Services methods -----
	
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
	
	/**
	 * Delete the message in the database (or more if the message correspond with many)
	 * 
	 * @param message The message model to delete
	 * @throws MessageException If the message can't be deleted
	 */
	public static void deleteMessage(MessageModel message) throws MessageException, MongoException, SQLException {
		// Verify the message
		boolean valid = true;
		StringBuilder errorMessage = new StringBuilder();
		
		if(message.getMessageId() == null || !Security.isValidMessageId(message.getMessageId())) {
			valid = false;
			errorMessage.append(" - Invalid message id : " + message.getMessageId());
		}
		if(message.getMessagePosterId() == null && !Security.isValidUserId(message.getMessagePosterId())) {
			valid = false;
			errorMessage.append(" - Invalid messsage poster id : " + message.getMessageId());
		}
		
		// If there is an error, throw an exception
		if(!valid) {
			
			throw new MessageException(errorMessage.toString());
			
		} else {
			
			// Delete the message from the database
			MessageDatabaseManager.deleteMessage(message);
			
		}
	}
	
	/**
	 * Modify the message in database identified with the ID
	 * 
	 * @param message The new message
	 * @throws MessageException If the message can't be modified
	 */
	public static void modifyMessage(MessageModel message) throws MessageException, MongoException {
		// Verify the message parameters
		boolean valid = true;
		StringBuilder errorMessage = new StringBuilder();

		if(message.getMessageId() == null || !Security.isValidMessageId(message.getMessageId())) {
			valid = false;
			errorMessage.append(" - Invalid message id : " + message.getMessageId());
		}
		if(message.getMessageText() == null || !Security.isStringNotEmpty(message.getMessageText())) {
			valid = false;
			errorMessage.append(" - Invalid message text : " + message.getMessageText());
		}
		if(message.getMessagePosterId() == null || !Security.isValidUserId(message.getMessagePosterId())) {
			valid = false;
			errorMessage.append(" - Invalid message poster id : " + message.getMessagePosterId());
		}

		// If there is an error, throw an error
		if(!valid) {

			throw new MessageException(errorMessage.toString());

		} else {

			// Escape the HTML special characters
			message.setMessageText(Security.htmlEncode(message.getMessageText()));
			message.setMessageBoardName(Security.htmlEncode(message.getMessageBoardName()));

			// Update the message
			MessageDatabaseManager.updateMessage(message);

		}
	}
	
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
