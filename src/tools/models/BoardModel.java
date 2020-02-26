package tools.models;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represent a board, a board is a general subject to sort messages
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class BoardModel {
	
	// ----- Attributes -----
	
	
	/** The board name "/b?b=testboard" (DB key) */
	private String boardName;
	
	/** The board description */
	private String boardDescription;
	
	/** Creators's id of the board */
	private String boardCreatorId;
	
	/** List of messages that compose the board */
	private List<String> boardMessagesId;
	
	
	// ----- Constructors -----
	
	
	/**
	 * Create an empty board model
	 */
	public BoardModel() {
		this.boardName = null;
		this.boardDescription = null;
		this.boardCreatorId = null;
		this.boardMessagesId = new ArrayList<String>();
	}
	
	
	// ----- Getters -----
	
	
	public String getBoardName() {
		return this.boardName;
	}
	
	public String getBoardDescription() {
		return this.boardDescription;
	}
	
	public String getBoardCreatorId() {
		return this.boardCreatorId;
	}
	
	public List<String> getBoardMessagesId() {
		return this.boardMessagesId;
	}
	
	
	// ----- Setters -----
	
	
	public void setBoardName(String name) {
		this.boardName = name;
	}
	
	public void setBoardDescription(String description) {
		this.boardDescription = description;
	}
	
	public void setBoardCreatorId(String creatorId) {
		this.boardCreatorId = creatorId;
	}
	
	public void addMessageId(String messageId) {
		if(!this.boardMessagesId.contains(messageId)) {
			this.boardMessagesId.add(messageId);
		}
	}
	
	public void removeMessageId(String messageId) {
		this.boardMessagesId.remove(messageId);
	}
	
	public void setBoardMessagesId(List<String> messagesId) {
		this.boardMessagesId = messagesId;
	}
	
	
	// ----- Standards methods -----
	
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof BoardModel)) {
			return false;
		} else {
			BoardModel b = (BoardModel) o;
			return this.boardName.equals(b.boardName);
		}
	}
	
}
