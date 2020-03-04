package services.message;

import tools.exceptions.MessageException;
import tools.exceptions.MongoException;
import tools.models.MessageModel;

/**
 * The service to delete a message
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class DeleteMessage {

	// ----- Attributes -----


	/** The DeleteMessage unique instance (singleton) */
	private static DeleteMessage instance = null;


	// ----- Constructors -----


	/**
	 * Create the DeleteMessage unique instance
	 */
	private DeleteMessage() {

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
	public void deleteMessage(MessageModel message) throws MessageException, MongoException {
		// TODO : Supprimer le message dans la base de données
	}

}
