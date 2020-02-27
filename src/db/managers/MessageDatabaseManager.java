package db.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import db.Database;
import tools.Config;
import tools.Logger;
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
	public void insertMessage(MessageModel messageModel) throws MongoException, SQLException{
		// Check if this id already exists
		MessageModel filter = new MessageModel();
		filter.setMessageId(messageModel.getMessageId());
		List<MessageModel> test = this.getMessage(filter, false);
		
		if(test.size() == 0) {
			
			// Get the document of the message
			JSONObject messageJSON = messageModel.getJSON();
			Document messageDocument = Document.parse(messageJSON.toJSONString());
			
			// Insert the new message
			this.messageCollection.insertOne(messageDocument);
			
			// Update the parent answers list or the board
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
				
			} else {
				
				Connection connection = Database.getMySQLConnection();
				String insert = "INSERT INTO BELONGS_TO_BOARD (boardName, messageId) VALUES (?, ?)";
				PreparedStatement preparedStatement = connection.prepareStatement(insert);
				
				// Bind the parameters
				preparedStatement.setString(1, messageModel.getMessageBoardName());
				preparedStatement.setString(2, messageModel.getMessageId());
				
				// Execute the insert
				preparedStatement.executeUpdate();
				
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
	public List<MessageModel> getMessage(MessageModel model, boolean regexSearch) throws MongoException {
		// Prepare the and queries
		List<Bson> andQueries = new ArrayList<Bson>();
		
		if(regexSearch) {
			
			if(model.getMessageId() != null) {
				Document regex = new Document();
				regex.append("$regex", model.getMessageId());
				regex.append("$options", "i");
				Document query = new Document();
				query.append("messageId", regex);
				andQueries.add(query);
			}
			if(model.getMessageText() != null) {
				Document regex = new Document();
				regex.append("$regex", model.getMessageText());
				regex.append("$options", "i");
				Document query = new Document();
				query.append("messageText", regex);
				andQueries.add(query);
			}
			if(model.getMessageBoardName() != null) {
				Document regex = new Document();
				regex.append("$regex", model.getMessageBoardName());
				regex.append("$options", "i");
				Document query = new Document();
				query.append("messageBoardName", regex);
				andQueries.add(query);
			}
			if(model.getMessagePosterId() != null) {
				Document regex = new Document();
				regex.append("$regex", model.getMessagePosterId());
				regex.append("$options", "i");
				Document query = new Document();
				query.append("messagePosterId", regex);
				andQueries.add(query);
			}
			if(model.getMessageDate() != null) {
				Document regex = new Document();
				regex.append("$regex", String.valueOf(model.getMessageDate().getTime()));
				regex.append("$options", "i");
				Document query = new Document();
				query.append("messageDate", regex);
				andQueries.add(query);
			}
			
		} else {
			
			if(model.getMessageId() != null) {
				Document query = new Document();
				query.append("messageId", model.getMessageId());
				andQueries.add(query);
			}
			if(model.getMessageText() != null) {
				Document query = new Document();
				query.append("messageText", model.getMessageText());
				andQueries.add(query);
			}
			if(model.getMessageBoardName() != null) {
				Document query = new Document();
				query.append("messageBoardName", model.getMessageBoardName());
				andQueries.add(query);
			}
			if(model.getMessagePosterId() != null) {
				Document query = new Document();
				query.append("messagePosterId", model.getMessagePosterId());
				andQueries.add(query);
			}
			if(model.getMessageDate() != null) {
				Document query = new Document();
				query.append("messageDate", String.valueOf(model.getMessageDate().getTime()));
				andQueries.add(query);
			}
			
		}
		
		// Put the result in the query
		Document query = new Document();
		query.append("$and", andQueries);
		
		System.out.println(query.toJson());
		
		// Prepare the result list
		List<MessageModel> res = new ArrayList<MessageModel>();

		for(Document message : this.messageCollection.find(query)) {
			try {
				
				// Parse all message result
				JSONObject messageJSON = (JSONObject) new JSONParser().parse(message.toJson());
				MessageModel newMessage = new MessageModel(messageJSON);
				res.add(newMessage);
				
			} catch (ParseException e) {

				// Log the error because it's not possible theorically...
				Logger logger = Logger.getInstance();
				logger.log("Error during a message BSON parsing, this error cannot happend", Logger.ERROR);
				logger.log(e, Logger.ERROR);
				
			}
		}
		
		// Return the result
		return res;
	}
	
	/**
	 * Get the next message id for the specified parent message
	 * 
	 * @param parent The parent message
	 * @return The next available message ID
	 */
	public String getNextMessageId(MessageModel parent) throws MongoException {
		// Get the parent answers list
		MessageModel filter = new MessageModel();
		filter.setMessageId(parent.getMessageId());
		List<MessageModel> messages = this.getMessage(filter, false);
		
		if(messages.size() == 1) {
			int maxValue = 0;
			
			// Search the next available id
			for(String answerId : messages.get(0).getMessageAnswersId()) {
				String[] path = answerId.split("\\.");
				int childId = Integer.valueOf(path[path.length - 1]);
				if(childId > maxValue) {
					maxValue = (childId);
				}
			}
			
			// Return the result
			return parent.getMessageId() + "." + (maxValue + 1);
		} else if(messages.size() < 1) {
			
			return null;
			
		} else {
			
			throw new MongoException("Multiple messages with the same ID");
			
		}
	}
	
}
