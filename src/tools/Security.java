package tools;

import org.apache.commons.text.StringEscapeUtils;

public class Security {
	
	// ----- Attributes -----
	
	
	private static Security instance;
	
	
	// ----- Constructors -----
	
	
	private Security() {
		
	}
	
	public static Security getInstance() {
		if(Security.instance == null) {
			Security.instance = new Security();
		}
		return Security.instance;
	}
	
	
	// ----- Getters -----
	
	
	// ----- Setters -----
	
	
	// ----- Class methods -----
	
	
	/**
	 * Clean the string from the xss injections
	 * 
	 * @param stringToClean The string to clean
	 * @return The cleaned string to insert it in database
	 */
	public String xssClean(String stringToClean) {
		String res = StringEscapeUtils.escapeHtml4(stringToClean);
		return res;
	}
	
	/**
	 * Clean the string from the sql injections
	 * 
	 * @param stringToClean The string to clean
	 * @return The cleaned string
	 */
	public String sqlClean(String stringToClean) {
		return null;
	}

}
