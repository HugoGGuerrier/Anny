package services.message;

import tools.exceptions.MessageException;
import tools.models.Message;

public class ModifyMessage {

	// ----- Attributes -----


	private static ModifyMessage instance = null;


	// ----- Constructors -----


	private ModifyMessage() {

	}

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
	public void modifyMessage(Message message) throws MessageException {
		// TODO : Modifier le message en se basant sur son ID
	}

}
