package services.message;

import db.managers.MessageDatabaseManager;
import tools.Security;
import tools.exceptions.MessageException;
import tools.exceptions.MongoException;
import tools.models.MessageModel;

public class ModifyMessage {

	// ----- Attributes -----

	
	/** The message database manager */
	private MessageDatabaseManager messageDatabaseManager;

	/** Security tool */
	private Security security;
	
	/** The ModifyMessage unique instance (singleton) */
	private static ModifyMessage instance = null;


	// ----- Constructors -----


	/**
	 * Construct a new service instance
	 */
	private ModifyMessage() {
		this.messageDatabaseManager = MessageDatabaseManager.getInstance();
		this.security = Security.getInstance();
	}

	/**
	 * Get the unique service instance
	 * 
	 * @return The instance
	 */
	public static ModifyMessage getInstance() {
		if(ModifyMessage.instance ==  null) {
			ModifyMessage.instance = new ModifyMessage();
		}
		return ModifyMessage.instance;
	}


	// ----- Class methods -----


	/**
	 * Modify the message in database identified with the ID
	 * 
	 * @param message The new message
	 * @throws MessageException If the message can't be modified
	 */
	public void modifyMessage(MessageModel message) throws MessageException, MongoException {
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

		// If there is an error, throw an error
		if(!valid) {

			throw new MessageException(errorMessage.toString());

		} else {

			// Update the message
			this.messageDatabaseManager.updateMessage(message);

		}
	}

}
