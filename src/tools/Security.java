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
	 * Encode a string with the html entities
	 * 
	 * @param stringToClean The string to encode
	 * @return The encoded string
	 */
	public String htmlEncode(String stringToEncode) {
		String res = StringEscapeUtils.escapeHtml4(stringToEncode);
		return res;
	}
	
	/**
	 * Decode the html entities in a string
	 * 
	 * @param stringToDecode The string to decode
	 * @return The decoded string
	 */
	public String htmlDecode(String stringToDecode) {
		String res = StringEscapeUtils.unescapeHtml4(stringToDecode);
		return res;
	}

}
