package one.anny.main.services.board;

import java.sql.SQLException;
import java.util.List;

import org.json.simple.JSONArray;

import one.anny.main.db.managers.BoardDatabaseManager;
import one.anny.main.tools.Security;
import one.anny.main.tools.models.BoardModel;

public class SearchBoard {

	// ----- Class methods -----


	/**
	 * Get board with the specified model
	 * 
	 * @param board The board model
	 * @return A list of the board corresponding to the model
	 * @throws SQLException If there is an error in the MySQL database
	 */
	@SuppressWarnings("unchecked")
	public static JSONArray searchBoard(BoardModel board, boolean isLike) throws SQLException {
		// Escape the HTML special characters to make the research works
		if(board.getBoardName() != null) {
			board.setBoardName(Security.htmlEncode(board.getBoardName()));
		}
		if(board.getBoardDescription() != null) {
			board.setBoardDescription(Security.htmlEncode(board.getBoardDescription()));
		}
		
		// Get the board from the database
		List<BoardModel> boards = BoardDatabaseManager.getBoards(board, isLike);
		
		// Put the result in a JSON array
		JSONArray res = new JSONArray();
		
		for (BoardModel boardModel : boards) {
			res.add(boardModel.getJSON());
		}
		
		// Return the result
		return res;
	}

}
