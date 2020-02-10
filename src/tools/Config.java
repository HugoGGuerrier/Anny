package tools;

import java.io.IOException;
import java.io.Reader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This class contains all application's configuration attributes as version, environment, database, etc...
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class Config {
	
	// ----- Attributes -----
	
	
	/** Variable that say if the application was initialize */
	private static boolean initialize = false;
	
	// --- Application's configuration
	
	/** The application's version (release.development.database) */
	private static String version;
	
	/** The application's environment (0 = development, 1 = production) */
	private static int env;
	
	/** Max number of sessions in the same time */
	private static long maxSessions;
	
	/** Sessions time to live */
	private static long sessionTimeToLive;
	
	
	// ----- Getters -----
	
	
	public static boolean isInitialize() {
		return Config.initialize;
	}
	
	public static String getVersion() {
		return Config.version;
	}
	
	public static int getEnv() {
		return Config.env;
	}
	
	public static long getMaxSessions() {
		return Config.maxSessions;
	}
	
	public static long getSessionTimeToLive() {
		return Config.sessionTimeToLive;
	};
	
	// ----- Class method -----
	
	
	/**
	 * This method read and set the application's configuration
	 * 
	 * @param configReader The configuration file
	 */
	public static void init(Reader configReader) {
		
		// Parse the configuration file
		JSONParser parser = new JSONParser();
		
		try {
			
			// --- Get the general configuration JSON object
			JSONObject jsonObject = (JSONObject) parser.parse(configReader);
			
			// --- Get the application's configuration
			JSONObject appConfig = (JSONObject) jsonObject.get("appConfig");
			
			String version = (String) appConfig.get("version");
			Config.version = version == null ? "unknown" : version;
			
			Long env = (Long) appConfig.get("env");
			Config.env = env == null ? 1 : env.intValue();
			
			Long maxSessions = (Long) appConfig.get("maxSessions");
			Config.maxSessions = maxSessions == null ? 1 : maxSessions;
			
			Long sessionTTL = (Long) appConfig.get("sessionTimeToLive");
			Config.sessionTimeToLive = sessionTTL == null ? 1800 : sessionTTL;
			
			// --- Get the mysql configuration
			
			// --- Get the mongodb configuration
			
		} catch (ParseException e) {
			
			// TODO: handle exception
			
		} catch (IOException e) {
			
			// TODO : handle exception
			
		}
		
		// Set the initialize indicator to true
		Config.initialize = true;
	}
	
	/**
	 * Return the String representation of the config
	 * 
	 * @return The configuration
	 */
	public static String display() {
		StringBuilder res = new StringBuilder();
		
		res.append("Birdy config : {\n");
		
		res.append("  version: " + Config.version + "\n");
		res.append("  environment: " + Config.env + "\n");
		res.append("  maxSessions: " + Config.maxSessions + "\n");
		res.append("  sessionTTL: " + Config.sessionTimeToLive + "\n");
		
		res.append("}");
		
		return res.toString();
	}
	
}
