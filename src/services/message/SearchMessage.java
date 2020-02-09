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


	public List<Message> searchMessage(Message message) {
		// TODO : recherche des messages

		return null;
	}

}
