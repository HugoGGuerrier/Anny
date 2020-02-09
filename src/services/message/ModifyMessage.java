package services.message;

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


	public int modifyMessage(Message message) {
		// TODO : insertion en base de données

		return 0;
	}

}
