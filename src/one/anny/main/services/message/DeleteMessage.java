package one.anny.main.services.message;

import java.sql.SQLException;

import one.anny.main.db.managers.MessageDatabaseManager;
import one.anny.main.tools.Security;
import one.anny.main.tools.exceptions.MessageException;
import one.anny.main.tools.exceptions.MongoException;
import one.anny.main.tools.models.MessageModel;

/**
 * The service to delete a message
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class DeleteMessage {

	// ----- Attributes -----


	/** Database manager */
	private MessageDatabaseManager messageDatabaseManager;
	
	/** Security tool */
	private Security security;
	
	/** The DeleteMessage unique instance (singleton) */
	private static DeleteMessage instance = null;


	// ----- Constructors -----


	/**
	 * Create the DeleteMessage unique instance
	 */
	private DeleteMessage() {
		this.messageDatabaseManager = MessageDatabaseManager.getInstance();
		this.security = Security.getInstance();
	}

	/**
	 * Get the DeleteMessage unique instance
	 * 
	 * @return The instance
	 */
	public static DeleteMessage getInstance() {
		if(DeleteMessage.instance ==  null) {
			DeleteMessage.instance = new DeleteMessage();
		}
		return DeleteMessage.instance;
	}


	// ----- Class methods -----


	/**
	 * Delete the message in the database (or more if the message correspond with many)
	 * 
	 * @param message The message model to delete
	 * @throws MessageException If the message can't be deleted
	 */
	public void deleteMessage(MessageModel message) throws MessageException, MongoException, SQLException {
		// Verify the message
		boolean valid = true;
		StringBuilder errorMessage = new StringBuilder();
		
		if(message.getMessageId() == null || !this.security.isValidMessageId(message.getMessageId())) {
			valid = false;
			errorMessage.append(" - Invalid message ID : " + message.getMessageId());
		}
		
		// If there is an error, throw an exception
		if(!valid) {
			
			throw new MessageException(errorMessage.toString());
			
		} else {
			
			// Delete the message from the database
			this.messageDatabaseManager.deleteMessage(message);
			
		}
	}

}
