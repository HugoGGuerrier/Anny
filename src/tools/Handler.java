package tools;

import org.json.simple.JSONObject;

public class Handler {

	// ----- Attributes -----


	private static Handler instance = null;


	// ----- Constructors -----


	private Handler() {

	}

	public static Handler getInstance() {
		if(Handler.instance == null) {
			Handler.instance = new Handler();
		}
		return Handler.instance;
	}


	// ----- Class methods -----


	/**
	 * Get the JSON of the refused service with a message
	 * 
	 * @param msg The wanted message
	 * @param code The error code
	 * @return The JSONObject you want to return to debug
	 */
	@SuppressWarnings({"unchecked"})
	public JSONObject handleException(Exception e, int code) {
		JSONObject res = new JSONObject();

		// Set the result if the environment is dev or prod
		if(Config.getEnv() == StdVar.DEVELOPMENT_ENV) {

			res.put("Error code", code);
			res.put("Error type", e.getClass().toString());
			res.put("Error message", e.getMessage());

		} else {

			res.put("Error code", 500);
			res.put("Error message", "Internal server error");

		}

		return res;
	}

}
