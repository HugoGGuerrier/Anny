package db.managers;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.json.simple.JSONObject;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import db.Database;
import tools.Config;
import tools.exceptions.MongoException;
import tools.models.MessageModel;

/**
 * A manager to add an abstract layer between Java and mongoDB
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class MessageDatabaseManager {

	// ----- Attributes -----
	
	
	/** The mongo message collection */
	private MongoCollection<Document> messageCollection;
	
	/** Unique instance of the message manager */
	private static MessageDatabaseManager instance = null;
	
	
	// ----- Constructors ----- 
	
	
	/**
	 * Construct an new message database manager
	 */
	private MessageDatabaseManager() {
		MongoDatabase database = Database.getMongoDBConnection();
		this.messageCollection = database.getCollection(Config.getMongoMessageCollection());
	}
	
	/**
	 * Get the unique instance of the manager
	 * 
	 * @return The manager
	 */
	public static MessageDatabaseManager getInstance() {
		if(MessageDatabaseManager.instance == null) {
			MessageDatabaseManager.instance = new MessageDatabaseManager();
		}
		return MessageDatabaseManager.instance;
	}
	
	
	// ----- Class Methods -----

	
	/**
	 * Insert a new message in the database
	 * 
	 * @param messageModel The message to insert
	 * @throws MongoException If there is an error during the message insertion
	 */
	public void insertMessage(MessageModel messageModel) throws MongoException {
		// Check if this id already exists
		List<MessageModel> test = this.getMessage(messageModel);
		
		if(test.size() == 0) {
			
			// Get the document of the message
			JSONObject messageJSON = messageModel.getJSON();
			Document messageDocument = Document.parse(messageJSON.toJSONString());
			
			// Insert the new message
			this.messageCollection.insertOne(messageDocument);
			
			// Update the parent answers list
			String parentId = messageModel.getParentId();
			
			if(parentId != null) {
				
				// Prepare the query document
				Document queryParent = new Document();
				queryParent.append("messageId", parentId);
				
				// Prepare the push document
				Document fieldValue = new Document();
				fieldValue.append("messageAnswersId", messageModel.getMessageId());
				Document pushInAnswers = new Document();
				pushInAnswers.append("$push", fieldValue);
				
				// Update the parent
				this.messageCollection.updateOne(queryParent, pushInAnswers);
				
			}
			
		} else {
			
			throw new MongoException("Multiple message ID");
			
		}
	}
	
	/**
	 * Update a message with its ID
	 * 
	 * @param messageModel The message to update
	 * @throws MongoException If there is an error during the message updating
	 */
	public void updateMessage(MessageModel messageModel) throws MongoException {
		
	}
	
	/**
	 * Delete a message and all its children
	 * 
	 * @param messageModel The message to delete
	 * @throws MongoException If there is an error during the message deletion
	 */
	public void deleteMessage(MessageModel messageModel) throws MongoException {
		
	}
	
	/**
	 * Get a message with its ID
	 * 
	 * @param model The message model to get
	 * @return The wanted message or null if it doesn't exists
	 * @throws MongoException If there is an error in the message getting
	 */
	public List<MessageModel> getMessage(MessageModel model) throws MongoException {
		return new ArrayList<MessageModel>();
	}
	
	/**
	 * Get the next message id for the specified parent message
	 * 
	 * @param parent The parent message ID
	 * @return The next available message ID
	 */
	public String getNextMessageId(String parent) {
		return null;
	}
	
}
