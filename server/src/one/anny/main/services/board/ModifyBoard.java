package one.anny.main.services.board;

import java.sql.SQLException;

import one.anny.main.db.managers.BoardDatabaseManager;
import one.anny.main.tools.Security;
import one.anny.main.tools.exceptions.BoardException;
import one.anny.main.tools.models.BoardModel;

public class ModifyBoard {

	// ----- Class methods -----


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

}
