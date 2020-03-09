package services.board;

import java.sql.SQLException;
import java.util.List;

import org.json.simple.JSONArray;

import db.managers.BoardDatabaseManager;
import tools.models.BoardModel;

public class SearchBoard {

	// ----- Attributes -----


	/** Board database manager */
	BoardDatabaseManager boardDatabaseManager;
	
	/** The service unique instance (singleton) */
	private static SearchBoard instance = null;


	// ----- Constructors -----


	private SearchBoard() {
		this.boardDatabaseManager = BoardDatabaseManager.getInstance();
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
	 * @throws SQLException If there is an error in the MySQL database
	 */
	@SuppressWarnings("unchecked")
	public JSONArray searchBoard(BoardModel board, boolean isLike) throws SQLException {
		// Get the board from the database
		List<BoardModel> boards = this.boardDatabaseManager.getBoards(board, isLike);
		
		// Put the result in a JSON array
		JSONArray res = new JSONArray();
		
		for (BoardModel boardModel : boards) {
			res.add(boardModel.getJSON());
		}
		
		// Return the result
		return res;
	}

}
