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
	
	/** Answers to the message which are also message instances created by mongodb */
	private List<MessageModel> messageAnswers;

	
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
		this.messageAnswers = new ArrayList<MessageModel>();
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

	public List<MessageModel> getMessageAnswers() {
		return this.messageAnswers;
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
	
	public void addAnswer(MessageModel answer) {
		if(!this.messageAnswers.contains(answer)) {
			this.messageAnswers.add(answer);
		}
	}
	
	public void removeAnswer(MessageModel answer) {
		this.messageAnswers.remove(answer);
	}

	public void setMessageAnswers(List<MessageModel> answers) {
		this.messageAnswers = answers;
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
