package tools.exceptions;

/**
 * A custom exception for mongo database error
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class MongoException extends Exception {
	
	private static final long serialVersionUID = -1217951195288411503L;
	
	public MongoException(String msg) {
		super(msg);
	}
	
}
