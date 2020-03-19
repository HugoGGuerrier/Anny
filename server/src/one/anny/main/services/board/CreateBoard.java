package one.anny.main.services.board;

import java.sql.SQLException;

import one.anny.main.db.managers.BoardDatabaseManager;
import one.anny.main.tools.Security;
import one.anny.main.tools.exceptions.BoardException;
import one.anny.main.tools.models.BoardModel;

public class CreateBoard {

	// ----- Class methods -----


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

}
