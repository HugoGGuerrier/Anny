package one.anny.main.tools.exceptions;

/**
 * Custom exception for user error
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class UserException extends Exception {
	
	private static final long serialVersionUID = -1908900443690062247L;

	public UserException(String msg) {
		super(msg);
	}

}
