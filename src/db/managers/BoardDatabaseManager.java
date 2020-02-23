package db.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		for(Long messageId : boardModel.getBoardMessagesId()) {
			belongsPreparedStatement.setLong(1, messageId);
			belongsPreparedStatement.setString(2, boardModel.getBoardName());
			
			belongsPreparedStatement.executeUpdate();
		}
	}

	public void updateBoard(BoardModel boardModel) throws SQLException {

	}

	public void deleteBoard(BoardModel boardModel) throws SQLException {

	}

	public List<BoardModel> getBoards(BoardModel model, boolean isLike) throws SQLException {
		// Get the MySQL connection
		Connection connection = Database.getMySQLConnection();

		// Prepare the selection query
		StringBuilder boardQuery = new StringBuilder("SELECT * FROM BOARD WHERE 1=1");
		StringBuilder belongsQuery = new StringBuilder("SELECT * FROM BELONGS_TO_BOARD WHERE 1=1");

		// Create the wanted SQL
		if(isLike) {

			if(model.getBoardName() != null) {
				boardQuery.append(" AND boardName LIKE ?");
				belongsQuery.append(" AND boardName LIKE ?");
			}
			if(model.getBoardDescription() != null) {
				boardQuery.append(" AND boardDescription LIKE ?");
			}
			if(model.getBoardCreatorId() != null) {
				boardQuery.append(" AND boardCreatorId LIKE ?");
			}

			if(model.getBoardMessagesId().size() > 0) {
				belongsQuery.append(" AND (");
				for(int i = 0; i < model.getBoardMessagesId().size(); i++) {
					belongsQuery.append("messageId = ?");
					if(i != model.getBoardMessagesId().size() - 1) {
						belongsQuery.append(" OR ");
					}
				}
				belongsQuery.append(")");
			}

		} else {

			if(model.getBoardName() != null) {
				boardQuery.append(" AND boardName = ?");
				belongsQuery.append(" AND boardName = ?");
			}
			if(model.getBoardDescription() != null) {
				boardQuery.append(" AND boardDescription = ?");
			}
			if(model.getBoardCreatorId() != null) {
				boardQuery.append(" AND boardCreatorId = ?");
			}

			if(model.getBoardMessagesId().size() > 0) {
				belongsQuery.append(" AND (");
				for(int i = 0; i < model.getBoardMessagesId().size(); i++) {
					belongsQuery.append("messageId = ?");
					if(i != model.getBoardMessagesId().size() - 1) {
						belongsQuery.append(" OR ");
					}
				}
				belongsQuery.append(")");
			}

		}

		// Prepare the statement
		PreparedStatement boardPreparedStatement = connection.prepareStatement(boardQuery.toString());
		PreparedStatement belongsPreparedStatement = connection.prepareStatement(belongsQuery.toString());
		int boardNextArgPointer = 1;
		int belongsNextArgPointer = 1;

		// Bind the parameters
		if(isLike) {

			if(model.getBoardName() != null) {
				boardPreparedStatement.setString(boardNextArgPointer++, "%" + model.getBoardName() + "%");
				belongsPreparedStatement.setString(belongsNextArgPointer++, "%" + model.getBoardName() + "%");
			}
			if(model.getBoardDescription() != null) {
				boardPreparedStatement.setString(boardNextArgPointer++, "%" + model.getBoardDescription() + "%");
			}
			if(model.getBoardCreatorId() != null) {
				boardPreparedStatement.setString(boardNextArgPointer++, "%" + model.getBoardCreatorId() + "%");
			}

			if(model.getBoardMessagesId().size() > 0) {
				for (Long messageId : model.getBoardMessagesId()) {
					belongsPreparedStatement.setLong(belongsNextArgPointer++, messageId);
				}
			}

		} else {

			if(model.getBoardName() != null) {
				boardPreparedStatement.setString(boardNextArgPointer++, model.getBoardName());
				belongsPreparedStatement.setString(belongsNextArgPointer++, model.getBoardName());
			}
			if(model.getBoardDescription() != null) {
				boardPreparedStatement.setString(boardNextArgPointer++, model.getBoardDescription());
			}
			if(model.getBoardCreatorId() != null) {
				boardPreparedStatement.setString(boardNextArgPointer++, model.getBoardCreatorId());
			}

			if(model.getBoardMessagesId().size() > 0) {
				for (Long messageId : model.getBoardMessagesId()) {
					belongsPreparedStatement.setLong(belongsNextArgPointer++, messageId);
				}
			}

		}

		// Get the message id list
		Map<String, List<Long>> boardsMessagesId = new HashMap<String, List<Long>>();

		ResultSet belongsResultSet = belongsPreparedStatement.executeQuery();

		while (belongsResultSet.next()) {
			String boardName = belongsResultSet.getString("boardName");
			Long messageId = belongsResultSet.getLong("messageId");
			
			List<Long> boardMessagesId = boardsMessagesId.getOrDefault(boardName, new ArrayList<Long>());
			boardMessagesId.add(messageId);
			boardsMessagesId.put(boardName, boardMessagesId);
		}

		// Prepare the result of boards
		List<BoardModel> res = new ArrayList<BoardModel>();

		// Get the boards from the database
		ResultSet boardResultSet = boardPreparedStatement.executeQuery();

		while(boardResultSet.next()) {
			String name = boardResultSet.getString("boardName");
			String description = boardResultSet.getString("boardDescription");
			String creatorId = boardResultSet.getString("boardCreatorId");

			if(boardsMessagesId.keySet().contains(name)) {
				BoardModel newBoard = new BoardModel();
				newBoard.setBoardName(name);
				newBoard.setBoardDescription(description);
				newBoard.setBoardCreatorId(creatorId);
				newBoard.setBoardMessagesId(boardsMessagesId.get(name));

				res.add(newBoard);
			}
		}

		// Return the result
		return res;
	}


}
