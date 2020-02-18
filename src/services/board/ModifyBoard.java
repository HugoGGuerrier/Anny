package services.board;

import tools.exceptions.BoardException;
import tools.models.BoardModel;

public class ModifyBoard {

	// ----- Attributes -----


	private static ModifyBoard instance = null;


	// ----- Constructors -----


	private ModifyBoard() {

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
	 */
	public void modifyBoard(BoardModel board) throws BoardException {
		// TODO : Modifier le board dans la base de données
	}

}
