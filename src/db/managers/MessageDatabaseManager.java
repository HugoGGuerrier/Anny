package db.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		// Prepare the update filter and set
		Document updateFilter = new Document();
		updateFilter.append("messageId", messageModel.getMessageId());

		Document updateSet = new Document();
		Document updateElems = new Document();
		updateElems.append("messageText", messageModel.getMessageText());
		updateSet.append("$set", updateElems);

		// Execute the update
		if(this.messageCollection.updateOne(updateFilter, updateSet).getMatchedCount() != 1) {
			throw new MongoException("Errod during the message updating : " + messageModel.getMessageId());
		}
	}

	/**
	 * Delete a message and all its children
	 * 
	 * @param messageModel The message to delete
	 * @throws MongoException If there is an error during the message deletion
	 */
	public void deleteMessage(MessageModel messageModel) throws MongoException, SQLException {
		// Prepare the delete query
		Document deleteQuery = new Document();
		deleteQuery.append("messageId", messageModel.getMessageId());

		// Execute the query
		if(this.messageCollection.deleteOne(deleteQuery).getDeletedCount() != 1) {
			throw new MongoException("Error in the deletion of the message " + messageModel.getMessageId());
		};

		// Delete all the answers recusively
		for(String answerId : messageModel.getMessageAnswersId()) {
			MessageModel filter = new MessageModel();
			filter.setMessageId(answerId);

			List<MessageModel> answerList = this.getMessage(filter, false);
			MessageModel answer = answerList.size() > 0 ? answerList.get(0) : null;
			if(answer != null) {
				try {

					this.deleteMessage(answer);

				} catch (MongoException e) {

					// Log the error because it's not possible theorically...
					Logger logger = Logger.getInstance();
					logger.log("A message you want to delete does not exists or is dereferenced : " + answer.getMessageId(), Logger.WARNING);
					logger.log(e, Logger.WARNING);

				}
			}
		}

		// Pull the id from the parent answers list of from the belongs to board table
		if(messageModel.getParentId() != null) {

			// Prepare the pool statement
			Document parentFilter = new Document();
			parentFilter.append("messageId", messageModel.getParentId());

			Document pullQuery = new Document();
			Document pullElems = new Document();
			pullElems.append("messageAnswersId", messageModel.getMessageId());
			pullQuery.append("$pull", pullElems);

			// Execute the pool
			this.messageCollection.updateOne(parentFilter, pullQuery);

		} else {

			Connection connection = Database.getMySQLConnection();
			String insert = "DELETE FROM BELONGS_TO_BOARD WHERE messageId = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(insert);

			// Bind the parameters
			preparedStatement.setString(1, messageModel.getMessageId());

			// Execute the delete
			preparedStatement.executeUpdate();

		}

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
		
		// Prepare the find query
		Document query = new Document();
		
		// Put the result in the query
		if(andQueries.size() > 0) {
			query.append("$and", andQueries);
		}

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
	public String getNextRootMessageId() throws SQLException {
		// Get the mysql connection
		Connection connection = Database.getMySQLConnection();
		Statement stmt = connection.createStatement();
		
		// Prepare the request
		String query = "SELECT MAX(CAST(messageId AS UNSIGNED)) as maxId FROM BELONGS_TO_BOARD";
		
		// Get the result
		ResultSet resultset = stmt.executeQuery(query);
		int maxValue = 1;
		while(resultset.next()) {
			Integer value = resultset.getInt("maxId");
			if (maxValue <= value) {
				maxValue = value + 1;
			}
		}
		
		// Return the result
		return String.valueOf(maxValue);
	}

}
