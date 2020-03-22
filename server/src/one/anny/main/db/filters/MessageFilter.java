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
	
	
	private Set<String> messageIdSet;
	
	private Set<String> messageTextSet;
	
	private Set<String> messageBoardNameSet;
	
	private Set<String> messagePosterIdSet;
	
	private Set<Date> messageDateSet;
	
	private Set<String> messageAnswerIdSet;
	
	
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
		this.messageAnswerIdSet = new HashSet<String>();
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
	
	public Set<String> getMessageAnswerIdSet() {
		return this.messageAnswerIdSet;
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
	
	// --- Message answer id
	
	public void setMessageAnswerIdSet(Set<String> messageAnswerIdSet) {
		this.messageAnswerIdSet = messageAnswerIdSet;
	}
	
	public void addMessageAnswerId(String messageAnswerId) {
		this.messageAnswerIdSet.add(messageAnswerId);
	}

}
