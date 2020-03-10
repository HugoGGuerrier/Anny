package one.anny.main.services.board;

import java.sql.SQLException;

import one.anny.main.db.managers.BoardDatabaseManager;
import one.anny.main.tools.Security;
import one.anny.main.tools.exceptions.BoardException;
import one.anny.main.tools.models.BoardModel;

public class DeleteBoard {

	// ----- Attributes -----


	/** The database manager */
	private BoardDatabaseManager boardDatabaseManager;

	/** Security tool */
	private Security security;

	/** The service unique instance (singleton) */
	private static DeleteBoard instance = null;


	// ----- Constructors -----


	/**
	 * Construct a new service
	 */
	private DeleteBoard() {
		this.boardDatabaseManager = BoardDatabaseManager.getInstance();
		this.security = Security.getInstance();
	}

	/**
	 * Get the unique service instance
	 * 
	 * @return The instance
	 */
	public static DeleteBoard getInstance() {
		if(DeleteBoard.instance ==  null) {
			DeleteBoard.instance = new DeleteBoard();
		}
		return DeleteBoard.instance;
	}


	// ----- Class methods -----


	/**
	 * Try to remove a board from the database
	 * 
	 * @param board The board to remove
	 * @throws BoardException If there were an error during the service
	 * @throws SQLException If there is an error in the MySQL database
	 */
	public void deleteBoard(BoardModel board) throws BoardException, SQLException {
		// Verify the board parameters
		boolean valid = true;
		StringBuilder message = new StringBuilder();

		if(board.getBoardName() == null || this.security.isValidBoardName(board.getBoardName())) {
			valid = false;
			message.append(" - Invalid board name : " + board.getBoardName());
		}

		if(!valid) {

			throw new BoardException(message.toString());

		} else {

			// Escape the HTML special characters
			board.setBoardName(this.security.htmlEncode(board.getBoardName()));
			
			// Delete the board from the database
			this.boardDatabaseManager.deleteBoard(board);

		}
	}

}
