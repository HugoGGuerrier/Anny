package services.board;

import java.sql.SQLException;

import db.managers.BoardDatabaseManager;
import tools.Security;
import tools.exceptions.BoardException;
import tools.models.BoardModel;

public class CreateBoard {

	// ----- Attributes -----

	
	/**	The user database manager unique instance */
	private BoardDatabaseManager boardDatabaseManager;
	
	/** Security tool */
	private Security security;
	
	/** The service unique instance (singleton) */
	private static CreateBoard instance = null;


	// ----- Constructors -----

	
	/**
	 * Construct a new service
	 */
	private CreateBoard() {

	}
	
	/**
	 * Get the service unique instance
	 * 
	 * @return The instance
	 */
	public static CreateBoard getInstance() {
		if(CreateBoard.instance ==  null) {
			CreateBoard.instance = new CreateBoard();
		}
		return CreateBoard.instance;
	}


	// ----- Class methods -----


	/**
	 * Try to insert the board in the database
	 * 
	 * @param board The board to create in database
	 * @throws BoardException If there were an error during the service
	 * @throws SQLException 
	 */
	public void createBoard(BoardModel board) throws BoardException, SQLException {
		// Verify the board parameters
		boolean valid = true;
		StringBuilder message = new StringBuilder();
		
		if(board.getBoardName() == null || !this.security.isValidBoardName(board.getBoardName())) {
			valid = false; 
			message.append(" - Invalid board name : " + board.getBoardName());
		}
		if(board.getBoardCreatorId() == null || !this.security.isValidUserId(board.getBoardCreatorId())) {
			valid = false;
			message.append(" - Invalid board creator id : " + board.getBoardCreatorId());
		}
		if(board.getBoardDescription() == null || !this.security.isStringNotEmpty(board.getBoardDescription())) {
			valid = false;
			message.append(" - Invalid board description : " + board.getBoardDescription());
		}
		
		
		if(!valid) {
			
			throw new BoardException(message.toString());
			
		} else {
			
			// Call the board database manager to insert a new board
			this.boardDatabaseManager.insertBoard(board);
			
		}
	}

}
