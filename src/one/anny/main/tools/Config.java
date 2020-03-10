package one.anny.main.tools;

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
	
	/** JSON object of the configuration */
	private static JSONObject configJSON;

	// --- Application's configuration

	/** The application's version (release.development.database) */
	private static String version;
	
	/** The database version for the application */
	private static int databaseVersion;

	/** The base path of the application */
	private static String basePath;

	/** The application's environment (0 = development, 1 = production) */
	private static int env;

	/** Number of sessions insertions before perform a cleaning */
	private static long cacheCleaningInterval;

	/** Sessions time to live */
	private static long sessionTimeToLive;
	
	/** If the server is in verbose mode */
	private static boolean verbose;

	// --- Mysql configuration

	/** If the mysql's driver should use pooling */
	private static boolean mysqlPooling;

	/** Mysql's host name */
	private static String mysqlHost;

	/** Mysql's database name */
	private static String mysqlDatabase;

	/** Mysql's login */
	private static String mysqlLogin;

	/** Mysql's password */
	private static String mysqlPassword;
	
	// --- MongoDB configuration
	
	/** MongoDB database name */
	private static String mongoDatabase;
	
	/** MongoDB message collection name */
	private static String mongoMessageCollection;


	// ----- Getters -----


	public static boolean isInitialize() {
		return Config.initialize;
	}

	public static JSONObject getJSON() {
		return Config.configJSON;
	}

	// --- Application getters

	public static String getVersion() {
		return Config.version;
	}
	
	public static int getDatabaseVersion() {
		return Config.databaseVersion;
	}

	public static String getBasePath() {
		return Config.basePath;
	}

	public static int getEnv() {
		return Config.env;
	}

	public static long getCacheCleaningInterval() {
		return Config.cacheCleaningInterval;
	}

	public static long getSessionTimeToLive() {
		return Config.sessionTimeToLive;
	}
	
	public static boolean isVerbose() {
		return Config.verbose;
	}

	// --- Mysql getters

	public static boolean isMysqlPooling() {
		return Config.mysqlPooling;
	}

	public static String getMysqlHost() {
		return Config.mysqlHost;
	}

	public static String getMysqlDatabase() {
		return Config.mysqlDatabase;
	}

	public static String getMysqlLogin() {
		return Config.mysqlLogin;
	}

	public static String getMysqlPassword() {
		return Config.mysqlPassword;
	}
	
	// --- MongoDB getters
	
	public static String getMongoDatabase() {
		return Config.mongoDatabase;
	}
	
	public static String getMongoMessageCollection() {
		return Config.mongoMessageCollection;
	}


	// ----- Setters -----


	public static void setBasePath(String basePath) {
		Config.basePath = basePath;
	}


	// ----- Class method -----


	/**
	 * This method read and set the application's configuration
	 * 
	 * @param configReader The configuration file
	 */
	public static void init(Reader configReader) {
		// Get the logger
		Logger logger = Logger.getInstance();

		// Parse the configuration file
		JSONParser parser = new JSONParser();

		try {

			// --- Get the general configuration JSON object
			JSONObject configJSON = (JSONObject) parser.parse(configReader);
			
			Config.configJSON = configJSON;

			// --- Get the application's configuration
			JSONObject appConfig = (JSONObject) configJSON.get("appConfig");

			String version = (String) appConfig.get("version");
			Config.version = version == null ? "unknown" : version;
			
			if(Config.version != "unknown") {
				String[] versions = Config.version.split("\\.");
				Config.databaseVersion = Integer.parseInt(versions[2]);
			}

			Long env = (Long) appConfig.get("env");
			Config.env = env == null ? 1 : env.intValue();

			Long cacheCleaningInterval = (Long) appConfig.get("cacheCleaningInterval");
			Config.cacheCleaningInterval = cacheCleaningInterval == null ? 1 : cacheCleaningInterval;

			Long sessionTTL = (Long) appConfig.get("sessionTimeToLive");
			Config.sessionTimeToLive = sessionTTL == null ? 1800 : sessionTTL;
			
			Boolean verbose = (Boolean) appConfig.get("verbose");
			Config.verbose = verbose != true ? false : true;

			// --- Get the mysql configuration
			JSONObject mysqlConfig = (JSONObject) configJSON.get("mysqlConfig");

			Boolean mysqlPooling = (Boolean) mysqlConfig.get("mysqlPooling");
			Config.mysqlPooling = mysqlPooling == null ? false : mysqlPooling;

			String mysqlHost = (String) mysqlConfig.get("mysqlHost");
			Config.mysqlHost = mysqlHost == null ? "unknown" : mysqlHost;

			String mysqlDatabase = (String) mysqlConfig.get("mysqlDatabase");
			Config.mysqlDatabase = mysqlDatabase == null ? "unknown" : mysqlDatabase;

			String mysqlLogin = (String) mysqlConfig.get("mysqlLogin");
			Config.mysqlLogin = mysqlLogin == null ? "unknown" : mysqlLogin;

			String mysqlPassword = (String) mysqlConfig.get("mysqlPassword");
			Config.mysqlPassword = mysqlPassword == null ? "unknown" : mysqlPassword;

			// --- Get the mongodb configuration
			JSONObject mongoConfig = (JSONObject) configJSON.get("mongoConfig");
			
			String mongoDatabase = (String) mongoConfig.get("mongoDatabase");
			Config.mongoDatabase = mongoDatabase;
			
			String mongoMessageCollection = (String) mongoConfig.get("mongoMessageCollection");
			Config.mongoMessageCollection = mongoMessageCollection;

			// Set the initialize indicator to true
			Config.initialize = true;

		} catch (ParseException e) {

			logger.log("Error during configuration file parsing", Logger.ERROR);
			logger.log(e, Logger.ERROR);

		} catch (IOException e) {

			logger.log("Error during configuration file reading", Logger.ERROR);
			logger.log(e, Logger.ERROR);

		}
	}

	/**
	 * Return the String representation of the config
	 * 
	 * @return The configuration
	 */
	public static String display() {
		StringBuilder res = new StringBuilder();

		res.append("Anny config : {\n");

		res.append("  version: " + Config.version + "\n");
		res.append("  environment: " + Config.env + "\n");
		res.append("  cacheCleaningInterval: " + Config.cacheCleaningInterval + "\n");
		res.append("  sessionTTL: " + Config.sessionTimeToLive + "\n");
		res.append("  verbose: " + Config.isVerbose() + "\n\n");

		res.append("  mysqlPooling: " + Config.mysqlPooling + "\n");
		res.append("  mysqlHost: " + Config.mysqlHost + "\n");
		res.append("  mysqlDatabase: " + Config.mysqlDatabase + "\n");
		res.append("  mysqlLogin: " + Config.mysqlLogin + "\n");
		res.append("  mysqlPassword: " + Config.mysqlPassword + "\n\n");
		
		res.append("  mongoDatabase: " + Config.mongoDatabase + "\n");
		res.append("  mongoMessageCollection: " + Config.mongoMessageCollection + "\n");

		res.append("}");

		return res.toString();
	}

}
