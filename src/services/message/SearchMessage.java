package services.message;

import java.util.List;

import tools.models.Message;

public class SearchMessage {

	// ----- Attributes -----


	private static SearchMessage instance = null;


	// ----- Constructors -----


	private SearchMessage() {

	}

	public static SearchMessage getInstance() {
		if(SearchMessage.instance ==  null) {
			SearchMessage.instance = new SearchMessage();
		}
		return SearchMessage.instance;
	}


	// ----- Class methods -----


	/**
	 * Get a list of messages corresponding with the message model
	 * 
	 * @param message The message model
	 * @return The list of messages
	 */
	public List<Message> searchMessage(Message message) {
		// TODO : Recherche des message correspondants

		return null;
	}

}
