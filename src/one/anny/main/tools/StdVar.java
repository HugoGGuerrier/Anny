package one.anny.main.tools;

/**
 * This class contains all standards variables
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class StdVar {
	
	// --- App constants
	public static final String APP_ENCODING = "UTF-8";
	
	// --- Environment
	public static final int DEVELOPMENT_ENV = 0;
	public static final int PRODUCTION_ENV = 1;

	// --- Paths
	public static final String CONFIG_FILE = "WEB-INF/config.json";
	public static final String SESSION_CACHE_FILE = "WEB-INF/sessions.json";
	public static final String ERROR_LOG_FILE = "WEB-INF/error.log";
	public static final String WARNING_LOG_FILE = "WEB-INF/warning.log";
	public static final String INFO_LOG_FILE = "WEB-INF/info.log";
	
	public static final String TEST_CONFIG_FILE = "src/test/config_test.json";
	
	// --- Content types
	public static final String JSON_CONTENT_TYPE = "application/json";

}
