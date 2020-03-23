package one.anny.main.db.filters;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * A filter class to make request on the message database
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class MessageFilter extends AbstractFilter {
	
	// ----- Attributes -----
	
	
	/** The wanted message id */
	private Set<String> messageIdSet;
	
	/** All wanted message text */
	private Set<String> messageTextSet;
	
	/** The message board name to look for */
	private Set<String> messageBoardNameSet;
	
	/** All possible poster id */
	private Set<String> messagePosterIdSet;
	
	/** The date of message */
	private Set<Date> messageDateSet;
	
	/** The date to search message after this date */
	private Date maxDate;
	
	
	// ----- Constructor -----
	
	
	/**
	 * Construct a new filter with the default void value
	 */
	public MessageFilter() {
		this.messageIdSet = new HashSet<String>();
		this.messageTextSet = new HashSet<String>();
		this.messageBoardNameSet = new HashSet<String>();
		this.messagePosterIdSet = new HashSet<String>();
		this.messageDateSet = new HashSet<Date>();
		this.maxDate = null;
	}
	
	
	// ----- Getters -----
	
	
	public Set<String> getMessageIdSet() {
		return this.messageIdSet;
	}
	
	public Set<String> getMessageTextSet() {
		return this.messageTextSet;
	}
	
	public Set<String> getMessageBoardNameSet() {
		return this.messageBoardNameSet;
	}
	
	public Set<String> getMessagePosterIdSet() {
		return this.messagePosterIdSet;
	}
	
	public Set<Date> getMessageDateSet() {
		return this.messageDateSet;
	}
	
	public Date getMaxDate() {
		return this.maxDate;
	}
	
	
	// ----- Setters -----
	
	
	// --- Message id
	
	public void setMessageIdSet(Set<String> messageIdSet) {
		this.messageIdSet = messageIdSet;
	}
	
	public void addMessageId(String messageId) {
		this.messageIdSet.add(messageId);
	}
	
	// --- Message text
	
	public void setMessageTextSet(Set<String> messageTextSet) {
		this.messageTextSet = messageTextSet;
	}
	
	public void addMessageText(String messageText) {
		this.messageTextSet.add(messageText);
	}
	
	// --- Message board name
	
	public void setMessageBoardNameSet(Set<String> messageBoardNameSet) {
		this.messageBoardNameSet = messageBoardNameSet;
	}
	
	public void addMessageBoardName(String boardName) {
		this.messageBoardNameSet.add(boardName);
	}
	
	// --- Message poster id
	
	public void setMessagePosterIdSet(Set<String> messagePosterIdSet) {
		this.messagePosterIdSet = messagePosterIdSet;
	}
	
	public void addMessagePosterId(String messagePosterId) {
		this.messagePosterIdSet.add(messagePosterId);
	}
	
	// --- Message date
	
	public void setMessageDateSet(Set<Date> messageDateSet) {
		this.messageDateSet = messageDateSet;
	}
	
	public void addMessageDate(Date messageDate) {
		this.messageDateSet.add(messageDate);
	}
	
	// --- Max date
	
	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

}
