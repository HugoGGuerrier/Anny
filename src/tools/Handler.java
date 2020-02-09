package tools;

import org.json.simple.JSONObject;

public class Handler {

	// ----- Attributes -----


	private static Handler instance = null;


	// ----- Constructors -----


	private Handler() {
		// Nothing for now
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
	@SuppressWarnings({ "unchecked"})
	public JSONObject serviceRefused(String msg, int code) {
		JSONObject res = new JSONObject();

		// Set the result if the environment is dev or prod
		if(Config.getEnv() == StdVar.DEVELOPMENT_ENV) {

			res.put("Error code", code);
			res.put("Error message", msg);

		} else {

			res.put("Error 500", "Internal server error");

		}

		return res;
	}

}
