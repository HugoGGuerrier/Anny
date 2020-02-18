package services.board;

import java.util.List;

import tools.models.BoardModel;

public class SearchBoard {

	// ----- Attributes -----


	private static SearchBoard instance = null;


	// ----- Constructors -----


	private SearchBoard() {

	}

	public static SearchBoard getInstance() {
		if(SearchBoard.instance ==  null) {
			SearchBoard.instance = new SearchBoard();
		}
		return SearchBoard.instance;
	}


	// ----- Class methods -----


	/**
	 * Get board with the specified model
	 * 
	 * @param board The board model
	 * @return A list of the board corresponding to the model
	 */
	public List<BoardModel> searchBoard(BoardModel board) {
		// TODO : Récupérer les boards dans la base de données
		
		return null;
	}

}
