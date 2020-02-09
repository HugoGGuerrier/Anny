package tools;

import java.io.IOException;
import java.io.Reader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This class contains all application's configuration attributes like version, base path, etc...
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class Config {
	
	// ----- Attributes -----
	
	
	/** Variable that say if the application was initialize */
	private static boolean initialize = false;
	
	/** The application's version (release.development.database) */
	private static String version;
	
	/** The application's environment (0 = development, 1 = production) */
	private static int env;
	
	
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
	
	// ----- Initialization method -----
	
	
	/**
	 * This method read and set the application's configuration
	 * 
	 * @param configReader The configuration file
	 */
	@SuppressWarnings("unchecked")
	public static void init(Reader configReader) {
		
		// Parse the configuration file
		JSONParser parser = new JSONParser();
		
		try {
			
			// --- Get the general configuration JSON object
			JSONObject jsonObject = (JSONObject) parser.parse(configReader);
			
			// --- Get the application configuration
			JSONObject appConfig = (JSONObject) jsonObject.get("appConfig");
			
			String version = (String) appConfig.get("version");
			Config.version = version == null ? "unknown" : version;
			
			Long env = (Long) appConfig.get("env");
			Config.env = env == null ? 1 : env.intValue();
			
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
	
}
