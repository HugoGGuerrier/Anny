package one.anny.main.tools.exceptions;

/**
 * Exception to handle error in message management
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class MessageException extends Exception {

	private static final long serialVersionUID = 3564511401250286038L;
	
	public MessageException(String msg) {
		super(msg);
	}

}
