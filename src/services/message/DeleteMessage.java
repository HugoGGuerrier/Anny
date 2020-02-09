package services.message;

import tools.models.Message;

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


	public int deleteMessage(Message message) {
		// TODO : insertion en base de données

		return 0;
	}

}
