package services.board;

import tools.exceptions.BoardException;
import tools.models.BoardModel;

public class CreateBoard {

	// ----- Attributes -----


	private static CreateBoard instance = null;


	// ----- Constructors -----


	private CreateBoard() {

	}

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
	 */
	public void createBoard(BoardModel board) throws BoardException {
		// TODO : Insérer le board dans la base de données
	}

}
