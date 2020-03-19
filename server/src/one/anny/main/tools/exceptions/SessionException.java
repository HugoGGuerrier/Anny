package one.anny.main.tools.exceptions;

/**
 * Custom exception to handle session problem
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class SessionException extends Exception {
	
	private static final long serialVersionUID = -6827810724178963917L;

	public SessionException(String message) {
		super(message);
	}

}
