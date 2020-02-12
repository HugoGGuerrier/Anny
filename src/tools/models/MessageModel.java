package tools.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This is the wrapper class for a message. It contains all informations that database store on a message
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class MessageModel {

	// ----- Attributes -----
	
	
	/** Message database id */
	private long messageId;
	
	/** Message text content */
	private String text;
	
	/** User who posted that message */
	private UserModel messagePoster;
	
	/** Date of the message */
	private Date messageDate;
	
	/** Answers to the message which are also message instances */
	private List<MessageModel> answers;

	
	// ----- Constructors -----
	
	
	/**
	 * Construct a new message from its id
	 * 
	 * @param messageId
	 */
	public MessageModel(long messageId) {
		this.messageId = messageId;
		
		this.answers = new ArrayList<MessageModel>();
	}
	
	/**
	 * Construct a new message with the default id -1
	 */
	public MessageModel() {
		this(-1);
	}
	
	
	// ----- Getters -----

	
	public long getMessageId() {
		return messageId;
	}

	public String getText() {
		return text;
	}

	public UserModel getMessagePoster() {
		return messagePoster;
	}

	public Date getMessageDate() {
		return messageDate;
	}

	public List<MessageModel> getAnswers() {
		return answers;
	}
	
	
	// ----- Setters -----
	

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	public void setMessagePoster(UserModel messagePoster) {
		this.messagePoster = messagePoster;
	}

	public void setMessageDate(Date messageDate) {
		this.messageDate = messageDate;
	}
	
	public void addAnswer(MessageModel answer) {
		if(!this.answers.contains(answer)) {
			this.answers.add(answer);
		}
	}
	
	public void removeAnswer(MessageModel answer) {
		this.answers.remove(answer);
	}

	public void setAnswers(List<MessageModel> answers) {
		this.answers = answers;
	}
	
	
	// ----- Standards methods -----
	
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof MessageModel)) {
			return false;
		} else {
			MessageModel msg = (MessageModel) obj;
			
			return this.messageId == msg.messageId;
		}
	}
	
}
