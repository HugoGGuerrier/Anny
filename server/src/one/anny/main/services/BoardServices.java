package one.anny.main.services;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;

import one.anny.main.db.filters.BoardFilter;
import one.anny.main.db.managers.BoardDatabaseManager;
import one.anny.main.tools.Security;
import one.anny.main.tools.exceptions.BoardException;
import one.anny.main.tools.models.BoardModel;

/**
 * A class that contains all services to board
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class BoardServices {
	
	// ----- Services methods -----
	
	
	/**
	 * Try to insert the board in the database
	 * 
	 * @param board The board to create in database
	 * @throws BoardException If there were an error during the service
	 * @throws SQLException If there is an error in the MySQL database
	 */
	public static void createBoard(BoardModel board) throws BoardException, SQLException {
		// Verify the board parameters
		boolean valid = true;
		StringBuilder message = new StringBuilder();

		if(board.getBoardName() == null || !Security.isValidBoardName(board.getBoardName())) {
			valid = false; 
			message.append(" - Invalid board name : " + board.getBoardName());
		}
		if(board.getBoardCreatorId() == null || !Security.isValidUserId(board.getBoardCreatorId())) {
			valid = false;
			message.append(" - Invalid board creator id : " + board.getBoardCreatorId());
		}
		if(board.getBoardDescription() == null || !Security.isStringNotEmpty(board.getBoardDescription())) {
			valid = false;
			message.append(" - Invalid board description : " + board.getBoardDescription());
		}


		if(!valid) {

			throw new BoardException(message.toString());

		} else {

			// Escape the HTML special characters
			board.setBoardName(Security.htmlEncode(board.getBoardName()));
			board.setBoardDescription(Security.htmlEncode(board.getBoardDescription()));

			// Call the board database manager to insert a new board
			BoardDatabaseManager.insertBoard(board);
			
		}
	}
	
	/**
	 * Try to remove a board from the database
	 * 
	 * @param board The board to remove
	 * @throws BoardException If there were an error during the service
	 * @throws SQLException If there is an error in the MySQL database
	 */
	public static void deleteBoard(BoardModel board) throws BoardException, SQLException {
		// Verify the board parameters
		boolean valid = true;
		StringBuilder message = new StringBuilder();

		if(board.getBoardName() == null || !Security.isValidBoardName(board.getBoardName())) {
			valid = false;
			message.append(" - Invalid board name : " + board.getBoardName());
		}
		if(board.getBoardCreatorId() != null && !Security.isValidUserId(board.getBoardCreatorId())) {
			valid = false;
			message.append(" - Invalid board creator id : " + board.getBoardCreatorId());
		}

		if(!valid) {

			throw new BoardException(message.toString());

		} else {

			// Escape the HTML special characters
			board.setBoardName(Security.htmlEncode(board.getBoardName()));
			
			// Delete the board from the database
			BoardDatabaseManager.deleteBoard(board);

		}
	}
	
	/**
	 * Try to modify a board
	 * 
	 * @param board The board to update in database
	 * @throws BoardException If there were an error during the service
	 * @throws SQLException If there is an error in the Mysql database
	 */
	public static void modifyBoard(BoardModel board) throws BoardException, SQLException {
		// Verify the board parameters
		boolean valid = true;
		StringBuilder message = new StringBuilder();
		
		if(board.getBoardName() == null || !Security.isValidBoardName(board.getBoardName())) {
			valid = false;
			message.append(" - Invalid board name : " + board.getBoardName());
		}
		if(board.getBoardDescription() == null || !Security.isStringNotEmpty(board.getBoardDescription())) {
			valid = false;
			message.append(" - Invalid board description : " + board.getBoardName());
		}
		if(board.getBoardCreatorId() == null || !Security.isValidUserId(board.getBoardCreatorId())) {
			valid = false;
			message.append(" - Invalid board creator id : " + board.getBoardCreatorId());
		}

		// If there is an error, throw an exception
		if(!valid) {

			throw new BoardException(message.toString());

		} else {

			// Escape the HTML special characters
			board.setBoardName(Security.htmlEncode(board.getBoardName()));
			board.setBoardDescription(Security.htmlEncode(board.getBoardDescription()));
			
			// Modify the board
			BoardDatabaseManager.updateBoard(board);

		}
	}
	
	/**
	 * Get board with the specified model
	 * 
	 * @param board The board model
	 * @return A list of the board corresponding to the model
	 * @throws SQLException If there is an error in the MySQL database
	 */
	@SuppressWarnings("unchecked")
	public static JSONArray searchBoard(BoardFilter filter, boolean isLike) throws SQLException {
		// Escape the HTML special characters to make the research works
		if(filter.getBoardNameSet().size() > 0) {
			Set<String> escapedNameSet = new HashSet<String>();
			for(String name : filter.getBoardNameSet()) {
				escapedNameSet.add(Security.htmlEncode(name));
			}
			filter.setBoardNameSet(escapedNameSet);
		}
		
		if(filter.getBoardDescriptionSet().size() > 0) {
			Set<String> escapedDescriptionSet = new HashSet<String>();
			for(String description : filter.getBoardDescriptionSet()) {
				escapedDescriptionSet.add(Security.htmlEncode(description));
			}
			filter.setBoardDescriptionSet(escapedDescriptionSet);
		}
		
		// Get the board from the database
		List<BoardModel> boards = BoardDatabaseManager.getBoards(filter, isLike);
		
		// Put the result in a JSON array
		JSONArray res = new JSONArray();
		
		for (BoardModel boardModel : boards) {
			res.add(boardModel.getJSON());
		}
		
		// Return the result
		return res;
	}

}
