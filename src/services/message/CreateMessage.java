package services.message;

import tools.models.Message;

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


	public int createMessage(Message message) {
		// TODO : insertion en base de données

		return 0;
	}

}
