package services.board;

import java.sql.SQLException;

import db.managers.BoardDatabaseManager;
import tools.Security;
import tools.exceptions.BoardException;
import tools.models.BoardModel;

public class ModifyBoard {

	// ----- Attributes -----


	/** Database manager */
	private BoardDatabaseManager boardDatabaseManager;
	
	/** Security tool */
	private Security security;
	
	/** Unique service instance */
	private static ModifyBoard instance = null;


	// ----- Constructors -----


	private ModifyBoard() {
		this.boardDatabaseManager = BoardDatabaseManager.getInstance();
		this.security = Security.getInstance();
	}

	public static ModifyBoard getInstance() {
		if(ModifyBoard.instance ==  null) {
			ModifyBoard.instance = new ModifyBoard();
		}
		return ModifyBoard.instance;
	}


	// ----- Class methods -----


	/**
	 * Try to modify a board
	 * 
	 * @param board The board to update in database
	 * @throws BoardException If there were an error during the service
	 * @throws SQLException If there is an error in the Mysql database
	 */
	public void modifyBoard(BoardModel board) throws BoardException, SQLException {
		// Verify the board parameters
		boolean valid = true;
		StringBuilder message = new StringBuilder();
		
		if(board.getBoardName() == null || !this.security.isValidBoardName(board.getBoardName())) {
			valid = false;
			message.append(" - Invalid board name : " + board.getBoardName());
		}
		if(board.getBoardDescription() == null || !this.security.isStringNotEmpty(board.getBoardDescription())) {
			valid = false;
			message.append(" - Invalid board description : " + board.getBoardName());
		}

		// If there is an error, throw an exception
		if(!valid) {

			throw new BoardException(message.toString());

		} else {

			// Escape the HTML special characters
			board.setBoardName(this.security.htmlEncode(board.getBoardName()));
			board.setBoardDescription(this.security.htmlEncode(board.getBoardDescription()));
			
			// Modify the board
			this.boardDatabaseManager.updateBoard(board);

		}
	}

}
