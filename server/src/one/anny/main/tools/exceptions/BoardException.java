package one.anny.main.tools.exceptions;

/**
 * Custom exception if there is a problem in a board
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class BoardException extends Exception {

	private static final long serialVersionUID = -524409113170136317L;
	
	public BoardException(String msg) {
		super(msg);
	}

}
