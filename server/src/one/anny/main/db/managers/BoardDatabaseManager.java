package one.anny.main.db.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import one.anny.main.db.Database;
import one.anny.main.db.filters.BoardFilter;
import one.anny.main.tools.models.BoardModel;

public class BoardDatabaseManager {

	// ----- Class Methods -----


	/**
	 * Insert a new board with all its messages
	 * 
	 * @param boardModel The board to insert
	 * @throws SQLException If there is an error during the board insertion
	 */
	public static void insertBoard(BoardModel boardModel) throws SQLException {
		// Get the MySQL connection
		Connection connection = Database.getMySQLConnection();

		// Create the SQL insertions
		String boardInsertion = "INSERT INTO BOARD (boardName, boardDescription, boardCreatorId) VALUES (?, ?, ?)";
		String belongsInsertion = "INSERT INTO BELONGS_TO_BOARD (messageId, boardName) VALUE (?, ?)";

		// Prepare the statements
		PreparedStatement boardPreparedStatement = connection.prepareStatement(boardInsertion);
		PreparedStatement belongsPreparedStatement = connection.prepareStatement(belongsInsertion);

		// Bind the board parameters
		boardPreparedStatement.setString(1, boardModel.getBoardName());
		boardPreparedStatement.setString(2, boardModel.getBoardDescription());
		boardPreparedStatement.setString(3, boardModel.getBoardCreatorId());

		// Insert the board
		boardPreparedStatement.executeUpdate();

		// Insert all the message id
		for(String messageId : boardModel.getBoardMessagesId()) {
			belongsPreparedStatement.setString(1, messageId);
			belongsPreparedStatement.setString(2, boardModel.getBoardName());

			belongsPreparedStatement.executeUpdate();
		}
	}

	/**
	 * Update a board based on its name
	 * 
	 * @param boardModel The board to update
	 * @throws SQLException Id there is an error during the board updating
	 */
	public static void updateBoard(BoardModel boardModel) throws SQLException {
		// Get the MySQL connection
		Connection connection = Database.getMySQLConnection();

		// Create the SQL updates
		String update = "UPDATE BOARD SET boardDescription = ? WHERE boardCreatorId = ? AND boardName = ?";

		// Prepare the update
		PreparedStatement preparedStatement = connection.prepareStatement(update);

		// Bind parameters
		preparedStatement.setString(1, boardModel.getBoardDescription());
		preparedStatement.setString(2, boardModel.getBoardCreatorId());
		preparedStatement.setString(3, boardModel.getBoardName());

		// Execute the update
		preparedStatement.executeUpdate();
	}

	public static void deleteBoard(BoardModel boardModel) throws SQLException {
		// Get the MySQL connection
		Connection connection = Database.getMySQLConnection();

		// Create the SQL deletion
		String deletion = "DELETE FROM BOARD WHERE boardName = ?";
		if(boardModel.getBoardCreatorId() != null) {
			deletion += " AND boardCreatorId = ?";
		}

		// Prepare the deletion
		PreparedStatement preparedStatement = connection.prepareStatement(deletion);

		// Bind parameters
		preparedStatement.setString(1, boardModel.getBoardName());
		if(boardModel.getBoardCreatorId() != null) {
			preparedStatement.setString(2, boardModel.getBoardCreatorId());
		}

		// Execute the update
		preparedStatement.executeUpdate();
	}

	public static List<BoardModel> getBoards(BoardFilter filter, boolean isLike) throws SQLException {
		// Get the MySQL connection
		Connection connection = Database.getMySQLConnection();

		// Prepare the selection query
		StringBuilder query = new StringBuilder("SELECT * FROM BOARD WHERE true");

		// Create the wanted SQL

		// Add all wanted board name
		if(filter.getBoardNameSet().size() > 0) {
			query.append(" AND (");

			Iterator<String> iterator = filter.getBoardNameSet().iterator();
			while(iterator.hasNext()) {
				iterator.next();
				query.append(" boardName " + (isLike ? "LIKE" : "=") + " ?");
				if(iterator.hasNext()) {
					query.append(" OR");
				}
			}

			query.append(" )");
		}

		// Add all wanted board description
		if(filter.getBoardDescriptionSet().size() > 0) {
			query.append(" AND (");

			Iterator<String> iterator = filter.getBoardDescriptionSet().iterator();
			while(iterator.hasNext()) {
				iterator.next();
				query.append(" boardDescription " + (isLike ? "LIKE" : "=") + " ?");
				if(iterator.hasNext()) {
					query.append(" OR");
				}
			}

			query.append(" )");
		}

		// Add all searched board creator id
		if(filter.getBoardCreatorIdSet().size() > 0) {
			query.append(" AND (");

			Iterator<String> iterator = filter.getBoardCreatorIdSet().iterator();
			while(iterator.hasNext()) {
				iterator.next();
				query.append(" boardCreatorId " + (isLike ? "LIKE" : "=") + " ?");
				if(iterator.hasNext()) {
					query.append(" OR");
				}
			}

			query.append(" )");
		}

		// Write the order statement
		if(filter.getOrderColumn() != null) {
			query.append(" ORDER BY " + filter.getOrderColumn() + (filter.isOrderReversed() ? " DESC" : " ASC"));
		}

		// Prepare the statement
		PreparedStatement preparedStatement = connection.prepareStatement(query.toString());
		int boardNextArgPointer = 1;

		// Bind the parameters
		for(String name : filter.getBoardNameSet()) {
			preparedStatement.setString(boardNextArgPointer++, name);
		}
		for(String description : filter.getBoardDescriptionSet()) {
			preparedStatement.setString(boardNextArgPointer++, description);
		}
		for(String creatorId : filter.getBoardCreatorIdSet()) {
			preparedStatement.setString(boardNextArgPointer++, creatorId);
		}

		// Prepare the result of boards
		List<BoardModel> res = new ArrayList<BoardModel>();

		// Get the boards from the database
		ResultSet boardResultSet = preparedStatement.executeQuery();

		while(boardResultSet.next()) {
			String name = boardResultSet.getString("boardName");
			String description = boardResultSet.getString("boardDescription");
			String creatorId = boardResultSet.getString("boardCreatorId");

			BoardModel newBoard = new BoardModel();
			newBoard.setBoardName(name);
			newBoard.setBoardDescription(description);
			newBoard.setBoardCreatorId(creatorId);

			res.add(newBoard);
		}

		// For each board, get all messages id
		String belongsQuery = "SELECT * FROM BELONGS_TO_BOARD WHERE boardName = ?";
		PreparedStatement belongsPreparedStatement = connection.prepareStatement(belongsQuery);

		for (BoardModel boardModel : res) {
			belongsPreparedStatement.setString(1, boardModel.getBoardName());
			ResultSet belongsResultSet = belongsPreparedStatement.executeQuery();

			List<String> messagesId = new ArrayList<String>();

			while(belongsResultSet.next()) {
				String messageId = belongsResultSet.getString("messageId");
				messagesId.add(messageId);
			}

			boardModel.setBoardMessagesId(messagesId);
		}

		// Return the result
		return res;
	}


}
