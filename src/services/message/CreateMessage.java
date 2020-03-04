package services.message;

import java.sql.SQLException;

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
	 * @param message The message to insert in database
	 * @throws MessageException If the message already exists or if there is an database exception
	 * @throws MongoException If the message cannot be inserted in the database
	 */
	public void createMessage(MessageModel message) throws MessageException, MongoException, SQLException {
		// Verify the message parameters
		boolean valid = true;
		StringBuilder errorMessage = new StringBuilder();
		
		if(message.getMessageId() == null || !this.security.isValidMessageId(message.getMessageId())) {
			valid = false;
			errorMessage.append(" - Invalid message ID : " + message.getMessageId());
		}
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
			
			// Insert the new message
			this.messageDatabaseManager.insertMessage(message);
			
		}
	}

}
