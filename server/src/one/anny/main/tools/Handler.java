package one.anny.main.tools;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * A simple handler to help generating JSON response for the client
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class Handler {

	// ----- Attributes -----

	
	/** Web error code, if there is an error in the web protocol */
	public static final int WEB_ERROR = -1;
	
	/** JSON error code if there is an error in a JSON */
	public static final int JSON_ERROR = 100;
	
	/** SQL error code if there is an error in the database */
	public static final int SQL_ERROR = 1000;
	
	/** MongoDB error code */
	public static final int MONGO_ERROR = 10000;
	
	/** Java error code if there is an error in Java */
	public static final int JAVA_ERROR = 100000;


	// ----- Class methods -----


	/**
	 * Get the default success JSON
	 * 
	 * @return The JSON for a success
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject getDefaultResponse() {
		JSONObject res = new JSONObject();
		
		res.put("result", "SUCCESS");
		
		return res;
	}
	
	/**
	 * Get the JSON of the refused service with a message
	 * 
	 * @param msg The wanted message
	 * @param code The error code
	 * @return The JSONObject you want to return to debug
	 */
	@SuppressWarnings({"unchecked"})
	public static JSONObject handleException(Exception e, int code) {
		JSONObject res = new JSONObject();

		// Set the result if the environment is development or production
		if(Config.getEnv() == StdVar.DEVELOPMENT_ENV) {

			res.put("result", "FAIL");
			res.put("errorCode", code);
			res.put("errorType", e.getClass().toString());
			res.put("errorMessage", e.getMessage());
			
			// Create the stack trace
			JSONArray trace = new JSONArray();
			for(StackTraceElement element : e.getStackTrace()) {
				trace.add(element.toString());
			}
			res.put("errorStackTrace", trace);

		} else {

			res.put("result", "FAIL");
			res.put("errorCode", 500);
			res.put("errorMessage", "Internal server error");

		}

		return res;
	}

}
