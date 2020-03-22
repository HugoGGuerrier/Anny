package one.anny.main.tools.models;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
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
	private String messageId;
	
	/** Message text content */
	private String messageText;
	
	/** Board that the message belongs to */
	private String messageBoardName;
	
	/** User who posted that message */
	private String messagePosterId;
	
	/** Date of the message */
	private Date messageDate;
	
	/** Answers id to the message */
	private List<String> messageAnswersId;

	
	// ----- Constructors -----
	
	
	/**
	 * Create an new message from a JSON object
	 * 
	 * @param messageJSON The message JSON
	 */
	public MessageModel(JSONObject messageJSON) {
		this.messageId = (String) messageJSON.get("messageId");
		this.messageText = (String) messageJSON.get("messageText");
		this.messageBoardName = (String) messageJSON.get("messageBoardName");
		this.messagePosterId = (String) messageJSON.get("messagePosterId");
		this.messageDate = new Date(Long.parseLong((String) messageJSON.get("messageDate")));
		this.messageAnswersId = new ArrayList<String>();
		
		// Get all the answers ID
		JSONArray answers = (JSONArray) messageJSON.get("messageAnswersId");
		for(Object o : answers) {
			String answerId = (String) o;
			this.messageAnswersId.add(answerId);
		}
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
		this.messageAnswersId = new ArrayList<String>();
	}
	
	
	// ----- Getters -----

	
	public String getMessageId() {
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

	public List<String> getMessageAnswersId() {
		return this.messageAnswersId;
	}
	
	/**
	 * Get the message parent ID
	 * 
	 * @return The parent ID or null if the message doesn't have parent
	 */
	public String getParentId() {
		String[] path = this.messageId.split("\\.");
		if(path.length != 1) {
			List<String> parentPath = new ArrayList<String>();
			for(int i = 0; i < path.length - 1; i++) {
				parentPath.add(path[i]);
			}
			return String.join(".", parentPath);
		}
		return null;
	}
	
	/**
	 * Get the next answer ID
	 * 
	 * @return The next available answer ID
	 */
	public String getNextAnswerId() {
		int maxValue = 0;

		// Search the next available id
		for(String answerId : this.messageAnswersId) {
			String[] path = answerId.split("\\.");
			int childId = Integer.valueOf(path[path.length - 1]);
			if(childId > maxValue) {
				maxValue = (childId);
			}
		}

		// Return the result
		return this.messageId + "." + (maxValue + 1);
	}
	
	/**
	 * Get the message formated in a JSON object
	 * 
	 * @return The JSON object
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getJSON() {
		// Prepare the result JSON
		JSONObject res = new JSONObject();
		
		// Add all fields
		res.put("messageId", this.messageId);
		res.put("messageText", this.messageText);
		res.put("messageBoardName", this.messageBoardName);
		res.put("messagePosterId", this.messagePosterId);
		res.put("messageDate", String.valueOf(this.messageDate.getTime()));
		
		// Add the answers ID
		JSONArray answers = new JSONArray();
		for(String messageId : this.messageAnswersId) {
			answers.add(messageId);
		}
		res.put("messageAnswersId", answers);
		
		// Return the result
		return res;
	}
	
	
	// ----- Setters -----
	

	public void setMessageId(String id) {
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
	
	public void addAnwserId(String answerId) {
		this.messageAnswersId.add(answerId);
	}
	
	public void removeAnswerId(String answerId) {
		this.messageAnswersId.remove(answerId);
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
