package tools.models;

import java.util.Date;
import java.util.List;

/**
 * This is the wrapper class for a message. It contains all informations that database store on a message
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class Message {

	// ----- Attributes -----
	
	
	/** Message database id */
	private long messageId;
	
	/** Message text content */
	private String text;
	
	/** User who posted that message */
	private User messagePoster;
	
	/** Date of the message */
	private Date messageDate;
	
	/** Answers to the message which are also message instances */
	private List<Message> answers;

	
	// ----- Constructors -----
	
	
	/**
	 * Construct a new message from its id
	 * 
	 * @param messageId
	 */
	public Message(long messageId) {
		this.messageId = messageId;
	}
	
	/**
	 * Construct a new message with the default id -1
	 */
	public Message() {
		this.messageId = -1;
	}
	
	
	// ----- Getters -----

	
	public long getMessageId() {
		return messageId;
	}

	public String getText() {
		return text;
	}

	public User getMessagePoster() {
		return messagePoster;
	}

	public Date getMessageDate() {
		return messageDate;
	}

	public List<Message> getAnswers() {
		return answers;
	}
	
	
	// ----- Setters -----
	

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	public void setMessagePoster(User messagePoster) {
		this.messagePoster = messagePoster;
	}

	public void setMessageDate(Date messageDate) {
		this.messageDate = messageDate;
	}

	public void setAnswers(List<Message> answers) {
		this.answers = answers;
	}
	
}
