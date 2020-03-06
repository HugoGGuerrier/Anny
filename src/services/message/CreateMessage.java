package services.message;

import java.sql.SQLException;
import java.util.List;

import db.managers.MessageDatabaseManager;
import tools.Security;
import tools.exceptions.MessageException;
import tools.exceptions.MongoException;
import tools.models.MessageModel;

public class CreateMessage {

	// ----- Attributes -----

	
	/** The message manager */
	private MessageDatabaseManager messageDatabaseManager;
	
	/** The security tool */
	private Security security;

	/** The CreateMessage unique instance (singleton) */
	private static CreateMessage instance = null;


	// ----- Constructors -----


	/**
	 * Create the unique create message instance
	 */
	private CreateMessage() {
		this.messageDatabaseManager = MessageDatabaseManager.getInstance();
		this.security = Security.getInstance();
	}

	/**
	 * Get the unique instance
	 * 
	 * @return The instance
	 */
	public static CreateMessage getInstance() {
		if(CreateMessage.instance ==  null) {
			CreateMessage.instance = new CreateMessage();
		}
		return CreateMessage.instance;
	}


	// ----- Class methods -----


	/**
	 * Try to insert the message in database
	 * 
	 * @param message The message to insert in database WITHOUT ITS ID
	 * @throws MessageException If the message already exists or if there is an database exception
	 * @throws MongoException If the message cannot be inserted in the database
	 */
	public synchronized void createMessage(MessageModel message, String parentMessageId) throws MessageException, MongoException, SQLException {
		// Verify the message parameters
		boolean valid = true;
		StringBuilder errorMessage = new StringBuilder();
		
		if(message.getMessageText() == null || !this.security.isStringNotEmpty(message.getMessageText())) {
			valid = false;
			errorMessage.append(" - Invalid message text : " + message.getMessageText());
		}
		if(message.getMessageBoardName() == null || !this.security.isValidBoardName(message.getMessageBoardName())) {
			valid = false;
			errorMessage.append(" - Invalid message board name : " + message.getMessageBoardName());
		}
		if(message.getMessagePosterId() == null || !this.security.isValidUserId(message.getMessagePosterId())) {
			valid = false;
			errorMessage.append(" - Invalid message poster ID : " + message.getMessagePosterId());
		}
		if(message.getMessageDate() == null) {
			valid = false;
			errorMessage.append(" - Invalid message date : null");
		}
		
		// If there is an error, throw an error
		if(!valid) {
			
			throw new MessageException(errorMessage.toString());
			
		} else {
			
			// Get message ID dynamically
			if(parentMessageId != null && !parentMessageId.equals("")) {
				
				// Get the message parent
				MessageModel parentFilter = new MessageModel();
				parentFilter.setMessageId(parentMessageId);				
				List<MessageModel> parents = this.messageDatabaseManager.getMessage(parentFilter, false);
				if(parents.size() == 1) {
					
					MessageModel parent = parents.get(0);
					message.setMessageId(parent.getNextAnswerId());
					
				} else {
					
					throw new MessageException("Parent message not found : " + parentMessageId);
					
				}
				
			} else {
				
				// Get the next root message ID
				message.setMessageId(messageDatabaseManager.getNextRootMessageId());
				
			}
			
			// Insert the new message
			this.messageDatabaseManager.insertMessage(message);
			
		}
	}

}
