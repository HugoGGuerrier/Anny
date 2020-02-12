package tools;

import java.util.concurrent.locks.Lock;

/**
 * This class is a manager to write and read the application cache, it is thread safe
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class Cache {
	
	// ----- Attributes -----
	
	
	private static Cache instance = null;
	
	private Lock writtingLock;
	
	private Lock readingLock;
	

}
