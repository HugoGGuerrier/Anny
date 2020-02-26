package db.managers;

import java.util.List;

import org.bson.Document;

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

	
	public void insertMessage(MessageModel messageModel) throws MongoException {
		
	}
	
	public void updateMessage(MessageModel messageModel) throws MongoException {
		
	}
	
	public void deleteMessage(MessageModel messageModel) throws MongoException {
		
	}
	
	public List<MessageModel> getMessages(MessageModel model) throws MongoException {
		return null;
	}
	
}
