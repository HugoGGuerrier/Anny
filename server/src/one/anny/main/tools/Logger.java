package one.anny.main.tools;

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
	private static File errorFile;
	
	/** Warning log file */
	private static File warningFile;
	
	/** Info log file */
	private static File infoFile;
	
	
	// ----- Initializer -----
	
	
	/**
	 * Initialize the logger at the start of the server
	 */
	public static void init() throws IOException {
		// Get the log files
		Logger.errorFile = Paths.get(Config.getBasePath() + StdVar.ERROR_LOG_FILE).toFile();
		Logger.warningFile = Paths.get(Config.getBasePath() + StdVar.WARNING_LOG_FILE).toFile();
		Logger.infoFile = Paths.get(Config.getBasePath() + StdVar.INFO_LOG_FILE).toFile();
		
		// If log files were not in the directory create them
		Logger.errorFile.createNewFile();
		Logger.warningFile.createNewFile();
		Logger.infoFile.createNewFile();
			
	}
	
	
	// ----- Class methods -----
	
	
	/**
	 * Write a string in the log file
	 * 
	 * @param message The string to write
	 * @param level The level of the log
	 */
	public static synchronized void log(String message, int level) {
		// Create the needed variables
		FileWriter writer = null;
		SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		StringBuilder toWrite = new StringBuilder();
		Date currentDate = new Date();
		
		try {
			
			// Add the log type
			switch (level) {
			
			case Logger.ERROR:
				writer = new FileWriter(Logger.errorFile, true);
				toWrite.append("[ERROR - ");
				break;

			case Logger.WARNING:
				writer = new FileWriter(Logger.warningFile, true);
				toWrite.append("[WARNING - ");
				break;
				
			case Logger.INFO:
				writer = new FileWriter(Logger.infoFile, true);
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
			if(Config.isVerbose()) {
				System.out.print(toWrite.toString());
			}
			
			// Close the file writer
			writer.close();
			
		} catch(IOException e) {
			
			e.printStackTrace();
			
		}
	}
	
	/**
	 * Write an integer in the log file
	 * 
	 * @param message The integer to write
	 * @param level The log level
	 */
	public static void log(int message, int level) {
		Logger.log(String.valueOf(message), level);
	}
	
	/**
	 * Write a long integer in the log file
	 * 
	 * @param message The long to write
	 * @param level The log level
	 */
	public static void log(long message, int level) {
		Logger.log(String.valueOf(message), level);
	}
	
	/**
	 * Write a float in the log file
	 * 
	 * @param message The float to write in the log file
	 * @param level The log level
	 */
	public static void log(float message, int level) {
		Logger.log(String.valueOf(message), level);
	}
	
	/**
	 * Write a double in the log file
	 * 
	 * @param message The double to write in the log file
	 * @param level The log level
	 */
	public static void log(double message, int level) {
		Logger.log(String.valueOf(message), level);
	}
	
	/**
	 * Write a boolean in the log file
	 * 
	 * @param message The boolean to write in the log file
	 * @param level The log level
	 */
	public static void log(boolean message, int level) {
		Logger.log(String.valueOf(message), level);
	}
	
	/**
	 * Write an Exception in the log file
	 * 
	 * @param e The Exception to write in the log file
	 * @param level The log level
	 */
	public static void log(Exception e, int level) {
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
        for(int i = 0; i < e.getStackTrace().length; i++) {
        	StackTraceElement ste = e.getStackTrace()[i];
        	message.append("    at ");
        	message.append(ste.toString());
        	if(i < e.getStackTrace().length - 1) {
        		message.append("\n");
        	}
        }

        // Log the error
        Logger.log(message.toString(), level);
	}

}
