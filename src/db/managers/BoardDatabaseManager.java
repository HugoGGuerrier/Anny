package db.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.Database;
import tools.models.BoardModel;

public class BoardDatabaseManager {

	// ----- Attributes -----


	/** Unique instance of the manager (singleton) */
	private static BoardDatabaseManager instance = null;


	// ----- Constructors ----- 


	/**
	 * Construct a new empty board database manager
	 */
	private BoardDatabaseManager() {

	}

	/**
	 * Get the instance of board database manager
	 * 
	 * @return The manager
	 */
	public static BoardDatabaseManager getInstance() {
		if(BoardDatabaseManager.instance == null) {
			BoardDatabaseManager.instance = new BoardDatabaseManager();
		}
		return BoardDatabaseManager.instance;
	}


	// ----- Class Methods -----


	/**
	 * Insert a new board with all its messages
	 * 
	 * @param boardModel The board to insert
	 * @throws SQLException If there is an error during the board insertion
	 */
	public void insertBoard(BoardModel boardModel) throws SQLException {
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
	 * @param boardModel The board ro update
	 * @throws SQLException Id there is an error during the board updating
	 */
	public void updateBoard(BoardModel boardModel) throws SQLException {
		// Get the mysql connection
		Connection connection = Database.getMySQLConnection();

		// Create the SQL updates
		String update = "UPDATE BOARD SET boardDescription = ?, boardCreatorId = ? WHERE boardName = ?";

		// Prepare the update
		PreparedStatement preparedStatement = connection.prepareStatement(update);

		// Bind parameters
		preparedStatement.setString(1, boardModel.getBoardDescription());
		preparedStatement.setString(2, boardModel.getBoardCreatorId());
		preparedStatement.setString(3, boardModel.getBoardName());

		// Execute the update
		preparedStatement.executeUpdate();
	}

	public void deleteBoard(BoardModel boardModel) throws SQLException {
		// Get the mysql connection
		Connection connection = Database.getMySQLConnection();

		// Create the SQL deletion
		String deletion = "DELETE FROM BOARD WHERE boardName = ?";

		// Prepare the deletion
		PreparedStatement preparedStatement = connection.prepareStatement(deletion);

		// Bind parameters
		preparedStatement.setString(1, boardModel.getBoardName());

		// Execute the update
		preparedStatement.executeUpdate();
	}

	public List<BoardModel> getBoards(BoardModel model, boolean isLike) throws SQLException {
		// Get the MySQL connection
		Connection connection = Database.getMySQLConnection();

		// Prepare the selection query
		StringBuilder boardQuery = new StringBuilder("SELECT * FROM BOARD WHERE 1=1");

		// Create the wanted SQL
		if(isLike) {

			if(model.getBoardName() != null) {
				boardQuery.append(" AND boardName LIKE ?");
			}
			if(model.getBoardDescription() != null) {
				boardQuery.append(" AND boardDescription LIKE ?");
			}
			if(model.getBoardCreatorId() != null) {
				boardQuery.append(" AND boardCreatorId LIKE ?");
			}

		} else {

			if(model.getBoardName() != null) {
				boardQuery.append(" AND boardName = ?");
			}
			if(model.getBoardDescription() != null) {
				boardQuery.append(" AND boardDescription = ?");
			}
			if(model.getBoardCreatorId() != null) {
				boardQuery.append(" AND boardCreatorId = ?");
			}

		}

		// Prepare the statement
		PreparedStatement boardPreparedStatement = connection.prepareStatement(boardQuery.toString());
		int boardNextArgPointer = 1;

		// Bind the parameters
		if(isLike) {

			if(model.getBoardName() != null) {
				boardPreparedStatement.setString(boardNextArgPointer++, "%" + model.getBoardName() + "%");
			}
			if(model.getBoardDescription() != null) {
				boardPreparedStatement.setString(boardNextArgPointer++, "%" + model.getBoardDescription() + "%");
			}
			if(model.getBoardCreatorId() != null) {
				boardPreparedStatement.setString(boardNextArgPointer++, "%" + model.getBoardCreatorId() + "%");
			}

		} else {

			if(model.getBoardName() != null) {
				boardPreparedStatement.setString(boardNextArgPointer++, model.getBoardName());
			}
			if(model.getBoardDescription() != null) {
				boardPreparedStatement.setString(boardNextArgPointer++, model.getBoardDescription());
			}
			if(model.getBoardCreatorId() != null) {
				boardPreparedStatement.setString(boardNextArgPointer++, model.getBoardCreatorId());
			}

		}

		// Prepare the result of boards
		List<BoardModel> res = new ArrayList<BoardModel>();

		// Get the boards from the database
		ResultSet boardResultSet = boardPreparedStatement.executeQuery();

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
