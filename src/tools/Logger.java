package tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class is used to write logs of the website
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class Logger {
	
	// ----- Attributes -----
	
	
	/** Error log level */
	public static final int ERROR = 0;
	
	/** Warning log level */
	public static final int WARNING = 1;
	
	/** Info log level */
	public static final int INFO =  2;
	
	/** Error log file */
	private File logFile;
	
	/** Unique logger instance (singleton) */
	private static Logger instance = null;
	
	
	// ----- Constructors -----
	
	
	/**
	 * Create the unique logger instance and create the log file if it doesn't exists
	 */
	private Logger() {
		this.logFile = Paths.get(Config.getBasePath() + StdVar.LOG_FILE).toFile();
		
		try {
			
			this.logFile.createNewFile();
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
	}
	
	/**
	 * Get the logger unique instance
	 * 
	 * @return
	 */
	public static Logger getInstance() {
		if(Logger.instance == null) {
			Logger.instance = new Logger();
		}
		return Logger.instance;
	}
	
	
	// ----- Class methods -----
	
	
	/**
	 * Write a string in the log file
	 * 
	 * @param message The string to write
	 * @param level The level of the log
	 */
	public synchronized void log(String message, int level) {
		// Create the needed variables
		FileWriter writer = null;
		SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		StringBuilder toWrite = new StringBuilder();
		Date currentDate = new Date();
		
		try {
			
			// Open the log file
			writer = new FileWriter(this.logFile, true);
			
			// Add the log type
			switch (level) {
			
			case Logger.ERROR:
				toWrite.append("[ERROR - ");
				break;

			case Logger.WARNING:
				toWrite.append("[WARNING - ");
				break;
				
			case Logger.INFO:
				toWrite.append("[INFO - ");
				break;
				
			}
			
			// Append the common part
			toWrite.append(formater.format(currentDate) + "] : ");
			toWrite.append(message);
			toWrite.append("\n");
			
			// Write in the log file
			writer.write(toWrite.toString());
			
			// Print the message in the console
			System.out.print(toWrite.toString());
			
			// Close the file writer
			writer.close();
			
		} catch(IOException e) {
			
			e.printStackTrace();
			
		}
	}
	
	/**
	 * Write an integer in the log file
	 * 
	 * @param message The int to write
	 * @param level The log level
	 */
	public void log(int message, int level) {
		this.log(String.valueOf(message), level);
	}
	
	/**
	 * Write a long integer in the log file
	 * 
	 * @param message The long to write
	 * @param level The log level
	 */
	public void log(long message, int level) {
		this.log(String.valueOf(message), level);
	}
	
	/**
	 * Write a float in the log file
	 * 
	 * @param message The float to write in the log file
	 * @param level The log level
	 */
	public void log(float message, int level) {
		this.log(String.valueOf(message), level);
	}
	
	/**
	 * Write a double in the log file
	 * 
	 * @param message The double to write in the log file
	 * @param level The log level
	 */
	public void log(double message, int level) {
		this.log(String.valueOf(message), level);
	}
	
	/**
	 * Write a boolean in the log file
	 * 
	 * @param message The boolean to write in the log file
	 * @param level The log level
	 */
	public void log(boolean message, int level) {
		this.log(String.valueOf(message), level);
	}
	
	/**
	 * Write an Exception in the log file
	 * 
	 * @param e The Exception to write in the log file
	 * @param level The log level
	 */
	public void log(Exception e, int level) {
		// Create the result string builder
        StringBuilder message = new StringBuilder();

        // Display the error class
        message.append("Throwed : ");
        message.append(e.getClass());

        // Append the error message
        message.append(" : ");
        if(e.getMessage() != null) {
        	message.append(e.getMessage());
        }
        message.append("\n");

        // Add the error stack trace
        for(StackTraceElement ste : e.getStackTrace()) {
        	message.append("    at ");
        	message.append(ste.toString());
        	message.append("\n");
        }

        // Log the error
        this.log(message.toString(), level);
	}

}
