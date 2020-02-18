package tools.models;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

/**
 * This is the wrapper class for a message. It contains all informations that database store on a message
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class MessageModel {

	// ----- Attributes -----
	
	
	/** Message database id (DB key) */
	private Long messageId;
	
	/** Message text content */
	private String messageText;
	
	/** Board that the message belongs to */
	private String messageBoardName;
	
	/** User who posted that message */
	private String messagePosterId;
	
	/** Date of the message */
	private Date messageDate;
	
	/** Answers to the message which are also message instances */
	private List<Long> messageAnswersId;

	
	// ----- Constructors -----
	
	
	/**
	 * Create an new message from a JSON object
	 * 
	 * @param messageJSON The message JSON
	 */
	public MessageModel(JSONObject messageJSON) {
		// TODO : Créer le message depuis un JSON
	}
	
	/**
	 * Construct a new empty message
	 */
	public MessageModel() {
		this.messageId = null;
		this.messageText = null;
		this.messageBoardName = null;
		this.messagePosterId = null;
		this.messageDate = null;
		this.messageAnswersId = new ArrayList<Long>();
	}
	
	
	// ----- Getters -----

	
	public Long getMessageId() {
		return this.messageId;
	}

	public String getMessageText() {
		return this.messageText;
	}
	
	public String getMessageBoardName() {
		return this.messageBoardName;
	}

	public String getMessagePosterId() {
		return this.messagePosterId;
	}

	public Date getMessageDate() {
		return this.messageDate;
	}

	public List<Long> getMessageAnswersId() {
		return this.messageAnswersId;
	}
	
	
	// ----- Setters -----
	

	public void setMessageId(Long id) {
		this.messageId = id;
	}
	
	public void setMessageText(String text) {
		this.messageText = text;
	}
	
	public void setMessageBoardName(String boardName) {
		this.messageBoardName = boardName;
	}

	public void setMessagePosterId(String posterId) {
		this.messagePosterId = posterId;
	}

	public void setMessageDate(Date date) {
		this.messageDate = date;
	}
	
	public void addAnswerId(Long answerId) {
		if(!this.messageAnswersId.contains(answerId)) {
			this.messageAnswersId.add(answerId);
		}
	}
	
	public void removeAnswerId(Long answerId) {
		this.messageAnswersId.remove(answerId);
	}

	public void setMessageAnswersId(List<Long> answersId) {
		this.messageAnswersId = answersId;
	}
	
	
	// ----- Standards methods -----
	
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof MessageModel)) {
			return false;
		} else {
			MessageModel m = (MessageModel) obj;
			return this.messageId.equals(m.messageId);
		}
	}
	
}
