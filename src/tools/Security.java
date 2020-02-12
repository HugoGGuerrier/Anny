package tools;

import org.apache.commons.text.StringEscapeUtils;

/**
 * A security utils class to make sure the applciation is safe
 * 
 * @author Emiie Siau
 * @author Hugo Guerrier
 */
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
	
	/**
	 * Get the correctly hashed string with the sha-512 algorithm
	 * 
	 * @param stringToHash The string to hash
	 * @return The hashed string
	 */
	public String hashString(String stringToHash) {
		// TODO : Hasher le mot de passe avec l'algorithme sha-512
		String res = "";
		
		return res;
	}
	
	
	// ----- Verify methods -----
	
	
	/**
	 * Verify the user ID with a regexp
	 * 
	 * @param userId The ID to validate
	 * @return True if the ID is valid
	 */
	public boolean isValidUserId(String userId) {
		// TODO : Faire la vérification de l'id utilisateur
		
		return true;
	}
	
	/**
	 * Verify the message ID
	 * 
	 * @param messageId The message ID to validate
	 * @return True if the message ID is valid
	 */
	public boolean idValidMessageId(long messageId) {
		return messageId > 5;
	}
	
	/**
	 * Verify the email address with a regexp
	 * 
	 * @param userMail The mail to verify
	 * @return True if the mail is valid
	 */
	public boolean isValidMail(String userMail) {
		// TODO : Faire la vérification du mail
		
		return true;
	}

}
