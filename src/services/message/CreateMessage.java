package services.message;

import tools.exceptions.MessageException;
import tools.models.MessageModel;

public class CreateMessage {

	// ----- Attributes -----


	private static CreateMessage instance = null;


	// ----- Constructors -----


	private CreateMessage() {

	}

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
	 */
	public void createMessage(MessageModel message) throws MessageException {
		// TODO : Insérer le message dans la base de données
	}

}
