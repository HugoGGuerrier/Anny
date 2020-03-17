package one.anny.main.services.message;

import one.anny.main.db.managers.MessageDatabaseManager;
import one.anny.main.tools.Security;
import one.anny.main.tools.exceptions.MessageException;
import one.anny.main.tools.exceptions.MongoException;
import one.anny.main.tools.models.MessageModel;

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
			errorMessage.append(" - Invalid message id : " + message.getMessageId());
		}
		if(message.getMessageText() == null || !this.security.isStringNotEmpty(message.getMessageText())) {
			valid = false;
			errorMessage.append(" - Invalid message text : " + message.getMessageText());
		}
		if(message.getMessagePosterId() == null || !this.security.isValidUserId(message.getMessagePosterId())) {
			valid = false;
			errorMessage.append(" - Invalid message poster id : " + message.getMessagePosterId());
		}

		// If there is an error, throw an error
		if(!valid) {

			throw new MessageException(errorMessage.toString());

		} else {

			// Escape the HTML special characters
			message.setMessageText(this.security.htmlEncode(message.getMessageText()));
			message.setMessageBoardName(this.security.htmlEncode(message.getMessageBoardName()));

			// Update the message
			this.messageDatabaseManager.updateMessage(message);

		}
	}

}
