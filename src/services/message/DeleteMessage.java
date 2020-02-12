package services.message;

import tools.exceptions.MessageException;
import tools.models.MessageModel;

public class DeleteMessage {

	// ----- Attributes -----


	private static DeleteMessage instance = null;


	// ----- Constructors -----


	private DeleteMessage() {

	}

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
	public void deleteMessage(MessageModel message) throws MessageException {
		// TODO : Supprimer le message dans la base de données
	}

}
