package tools;

/**
 * This class contains all standards variables
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class StdVar {
	
	// --- Environment
	public static final int DEVELOPMENT_ENV = 0;
	public static final int PRODUCTION_ENV = 1;
	
	// --- Error code
	public static final int WEB_ERROR = -1;
	public static final int JSON_ERROR = 100;
	public static final int SQL_ERROR = 1000;
	public static final int JAVA_ERROR = 10000;
	
	// --- Paths
	public static final String configFile = "/WEB-INF/config.json";
	public static final String testConfigFile = "src/test/config_test.json";

}
