package services.board;

import tools.exceptions.BoardException;
import tools.models.BoardModel;

public class DeleteBoard {

	// ----- Attributes -----


	private static DeleteBoard instance = null;


	// ----- Constructors -----


	private DeleteBoard() {

	}

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
	 */
	public void deleteBoard(BoardModel board) throws BoardException {
		// TODO : Supprimer le board dans la base de données
	}

}
