package one.anny.main.services.message;

import one.anny.main.db.managers.MessageDatabaseManager;
import one.anny.main.tools.Security;
import one.anny.main.tools.exceptions.MessageException;
import one.anny.main.tools.exceptions.MongoException;
import one.anny.main.tools.models.MessageModel;

public class ModifyMessage {

	// ----- Class methods -----


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

}
