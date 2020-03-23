package one.anny.main.db.managers;

import java.sql.Connection;
import java.sql.Date;
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

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import one.anny.main.db.Database;
import one.anny.main.db.filters.MessageFilter;
import one.anny.main.tools.Config;
import one.anny.main.tools.Logger;
import one.anny.main.tools.exceptions.MongoException;
import one.anny.main.tools.models.MessageModel;

/**
 * A manager to add an abstract layer between Java and mongoDB
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class MessageDatabaseManager {

	// ----- Attributes -----


	/** The Mongo message collection */
	private static MongoCollection<Document> messageCollection = Database.getMongoDBConnection().getCollection(Config.getMongoMessageCollection());


	// ----- Class Methods -----


	/**
	 * Insert a new message in the database
	 * 
	 * @param messageModel The message to insert
	 * @throws MongoException If there is an error during the message insertion
	 * @throws SQLException If there is an error during the message insertion in the MySQL
	 */
	public static void insertMessage(MessageModel messageModel) throws MongoException, SQLException{
		// Check if this id already exists
		MessageFilter filter = new MessageFilter();
		filter.addMessageId(messageModel.getMessageId());
		List<MessageModel> test = MessageDatabaseManager.getMessage(filter, false, false);

		if(test.size() == 0) {

			// Get the document of the message
			JSONObject messageJSON = messageModel.getJSON();
			Document messageDocument = Document.parse(messageJSON.toJSONString());

			// Insert the new message
			MessageDatabaseManager.messageCollection.insertOne(messageDocument);

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
				MessageDatabaseManager.messageCollection.updateOne(queryParent, pushInAnswers);

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

			throw new MongoException("Multiple message ID : " + messageModel.getMessageId());

		}
	}

	/**
	 * Update a message with its ID
	 * 
	 * @param messageModel The message to update
	 * @throws MongoException If there is an error during the message updating
	 */
	public static void updateMessage(MessageModel messageModel) throws MongoException {
		// Prepare the update filter and set
		Document updateFilter = new Document();
		updateFilter.append("messageId", messageModel.getMessageId());
		updateFilter.append("messagePosterId", messageModel.getMessagePosterId());

		Document updateSet = new Document();
		Document updateElems = new Document();
		updateElems.append("messageText", messageModel.getMessageText());
		updateSet.append("$set", updateElems);

		// Execute the update
		if(MessageDatabaseManager.messageCollection.updateOne(updateFilter, updateSet).getMatchedCount() != 1) {
			throw new MongoException("Errod during the message updating : " + messageModel.getMessageId());
		}
	}

	/**
	 * Delete a message and all its children
	 * 
	 * @param messageModel The message to delete
	 * @throws MongoException If there is an error during the message deletion
	 * @throws SQLException If there is an error during the deletion in the BELONGS_TO_BOARD
	 */
	public static void deleteMessage(MessageModel messageModel) throws MongoException, SQLException {
		// Prepare the delete query
		Document query = new Document();
		query.append("messageId", messageModel.getMessageId());
		if(messageModel.getMessagePosterId() != null) {
			query.append("messagePosterId", messageModel.getMessagePosterId());
		}

		// Get the full message model
		try {
			MessageFilter filter = new MessageFilter();
			filter.addMessageId(messageModel.getMessageId());
			filter.addMessagePosterId(messageModel.getMessagePosterId());
			
			messageModel = MessageDatabaseManager.getMessage(filter, false, false).get(0);
		} catch (IndexOutOfBoundsException e) {
			throw new MongoException("Wrong message id or poster id : " + messageModel.getMessageId() + " - " + messageModel.getMessagePosterId());
		}

		// Execute the query
		if(MessageDatabaseManager.messageCollection.deleteOne(query).getDeletedCount() != 1) {
			throw new MongoException("Error in the deletion of the message : " + messageModel.getMessageId());
		}

		// Delete all the answers recursively
		for(String answerId : messageModel.getMessageAnswersId()) {
			MessageFilter filter = new MessageFilter();
			filter.addMessageId(answerId);

			List<MessageModel> answerList = MessageDatabaseManager.getMessage(filter, false, false);
			MessageModel answer = answerList.size() > 0 ? answerList.get(0) : null;
			if(answer != null) {
				try {

					MessageDatabaseManager.deleteMessage(answer);

				} catch (MongoException e) {

					// Log the error because it's not possible...
					Logger.log("A message you want to delete does not exists or is dereferenced : " + answer.getMessageId(), Logger.WARNING);
					Logger.log(e, Logger.WARNING);

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
			MessageDatabaseManager.messageCollection.updateOne(parentFilter, pullQuery);

		} else {

			// Get the MySQL connection to insert the message in the board
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
	 */
	public static List<MessageModel> getMessage(MessageFilter filter, boolean regexSearch, boolean limitSize) {
		// Prepare the and queries
		List<Bson> andQueries = new ArrayList<Bson>();

		// Prepare the selection query

		// Message id
		if(filter.getMessageIdSet().size() > 0) {
			Document orQuery = new Document();
			List<Bson> orQueries = new ArrayList<Bson>();

			// Add all message id possibility
			for(String id : filter.getMessageIdSet()) {
				Document query = new Document();
				if(regexSearch) {
					Document regex = new Document();
					regex.append("$regex", id);
					regex.append("$options", "i");
					query.append("messageId", regex);
				} else {
					query.append("messageId", id);
				}
				orQueries.add(query);
			}

			orQuery.append("$or", orQueries);
			andQueries.add(orQuery);
		}

		// Message text
		if(filter.getMessageTextSet().size() > 0) {
			Document orQuery = new Document();
			List<Bson> orQueries = new ArrayList<Bson>();

			// Add all message text possibility
			for(String text : filter.getMessageTextSet()) {
				Document query = new Document();
				if(regexSearch) {
					Document regex = new Document();
					regex.append("$regex", text);
					regex.append("$options", "i");
					query.append("messageText", regex);
				} else {
					query.append("messageText", text);
				}
				orQueries.add(query);
			}

			orQuery.append("$or", orQueries);
			andQueries.add(orQuery);
		}

		// Message board name
		if(filter.getMessageBoardNameSet().size() > 0) {
			Document orQuery = new Document();
			List<Bson> orQueries = new ArrayList<Bson>();

			// Add all message text possibility
			for(String boardName : filter.getMessageBoardNameSet()) {
				Document query = new Document();
				if(regexSearch) {
					Document regex = new Document();
					regex.append("$regex", boardName);
					regex.append("$options", "i");
					query.append("messageBoardName", regex);
				} else {
					query.append("messageBoardName", boardName);
				}
				orQueries.add(query);
			}

			orQuery.append("$or", orQueries);
			andQueries.add(orQuery);
		}

		// Message poster id
		if(filter.getMessagePosterIdSet().size() > 0) {
			Document orQuery = new Document();
			List<Bson> orQueries = new ArrayList<Bson>();

			// Add all message text possibility
			for(String posterId : filter.getMessagePosterIdSet()) {
				Document query = new Document();
				if(regexSearch) {
					Document regex = new Document();
					regex.append("$regex", posterId);
					regex.append("$options", "i");
					query.append("messagePosterId", regex);
				} else {
					query.append("messagePosterId", posterId);
				}
				orQueries.add(query);
			}

			orQuery.append("$or", orQueries);
			andQueries.add(orQuery);
		}

		// Message date
		if(filter.getMessageDateSet().size() > 0) {
			Document orQuery = new Document();
			List<Bson> orQueries = new ArrayList<Bson>();

			// Add all message text possibility
			for(Date date : filter.getMessageDateSet()) {
				Document query = new Document();
				query.append("messageDate", date.getTime());
				orQueries.add(query);
			}

			orQuery.append("$or", orQueries);
			andQueries.add(orQuery);
		}
		
		// Set the max message date value
		if(filter.getMaxDate() != null) {
			Document maxDateQuery = new Document();
			Document lesserQuery = new Document();
			lesserQuery.append("$lt", filter.getMaxDate().getTime());
			maxDateQuery.append("messageDate", lesserQuery);
			andQueries.add(maxDateQuery);
		}

		// Prepare the find query
		Document query = new Document();

		// Put the result in the query
		if(andQueries.size() > 0) {
			query.append("$and", andQueries);
		}

		// Prepare the result list
		List<MessageModel> res = new ArrayList<MessageModel>();

		// Get the find iterator from the database
		FindIterable<Document> messageFindIterable = MessageDatabaseManager.messageCollection.find(query);

		// Order the messages if the request need it
		if(filter.getOrderColumn() != null) {
			Document sortDocument = new Document();
			sortDocument.append(filter.getOrderColumn(), (filter.isOrderReversed() ? -1 : 1));
			messageFindIterable = messageFindIterable.sort(sortDocument);
		}

		// Limit the size if the order wants to
		if(limitSize) {
			messageFindIterable = messageFindIterable.limit(Config.getMessageSelectionLimitSize());
		}

		for(Document message : messageFindIterable) {
			try {

				// Parse all message result
				JSONObject messageJSON = (JSONObject) new JSONParser().parse(message.toJson());				
				MessageModel newMessage = new MessageModel(messageJSON);
				res.add(newMessage);

			} catch (ParseException e) {

				// Log the error because it's not possible...
				Logger.log("Error during a message BSON parsing, this error cannot happend", Logger.ERROR);
				Logger.log(e, Logger.ERROR);

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
	public static String getNextRootMessageId() throws SQLException {
		// Get the MySQL connection
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
