package one.anny.main.tools;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.text.StringEscapeUtils;

/**
 * A security tool class to make sure the application is safe
 * 
 * @author Emiie Siau
 * @author Hugo Guerrier
 */
public class Security {

	// ----- Class methods -----


	/**
	 * Encode a string with the HTML entities
	 * 
	 * @param stringToClean The string to encode
	 * @return The encoded string
	 */
	public static String htmlEncode(String stringToEncode) {
		String res = StringEscapeUtils.escapeHtml4(stringToEncode);
		return res;
	}

	/**
	 * Decode the HTML entities in a string
	 * 
	 * @param stringToDecode The string to decode
	 * @return The decoded string
	 */
	public static String htmlDecode(String stringToDecode) {
		String res = StringEscapeUtils.unescapeHtml4(stringToDecode);
		return res;
	}

	/**
	 * Get the correctly hashed string with the SHA-512 algorithm
	 * 
	 * @param stringToHash The string to hash
	 * @return The hashed string
	 */
	public static String hashString(String stringToHash) {
		return DigestUtils.sha512Hex(stringToHash);
	}

	/**
	 * Generate a CSRF token to verify forms
	 * 
	 * @return The generated CSRF token
	 */
	public static String generateCSRFToken() {
		// Define needed variables
		String possibleChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		Random random = new Random();
		StringBuilder res = new StringBuilder();

		// Generate the token
		for (int i = 0; i < 64; i++) {
			int selectedChar = random.nextInt(possibleChars.length());
			res.append(possibleChars.charAt(selectedChar));
		}

		// Return the generated token
		return res.toString();
	}


	// ----- Verify methods -----


	// --- General verification

	/**
	 * Verify the date with a regexp
	 * 
	 * @param userDate The date to verify
	 * @return True if the date is valid
	 */
	public static boolean isValidDate(String userDate) {
		Pattern pattern = Pattern.compile("^2[0-9]{3}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[0-1])$");
		Matcher matcher = pattern.matcher(userDate);

		return matcher.matches();
	}

	/**
	 * Verify the board description
	 * 
	 * @param boardDescription The board description
	 * @return True if the board description is valid
	 */
	public static boolean isStringNotEmpty(String string) {
		Pattern pattern = Pattern.compile("^.+$");
		Matcher matcher = pattern.matcher(string);

		return matcher.matches();
	}

	// --- User verification

	/**
	 * Verify the user ID with a regexp
	 * 
	 * @param userId The ID to validate
	 * @return True if the ID is valid
	 */
	public static boolean isValidUserId(String userId) {
		Pattern pattern = Pattern.compile("^@[a-z0-9_]+$");
		Matcher matcher = pattern.matcher(userId);

		return matcher.matches();
	}

	/**
	 * Verify the email address with a regexp
	 * 
	 * @param userMail The mail to verify
	 * @return True if the mail is valid
	 */
	public static boolean isValidEmail(String userMail) {
		Pattern pattern = Pattern.compile("^.*@[a-z0-9\\-]+\\.[a-z]+$");
		Matcher matcher = pattern.matcher(userMail);

		return matcher.matches();
	}

	/**
	 * Verify if a password is correctly hashed and not empty
	 * 
	 * @param hashedPassword The hashed password to verify
	 * @return True if the password is valid
	 */
	public static boolean isValidPassword(String hashedPassword) {
		Pattern pattern = Pattern.compile("^([abcdef]|[0-9]){128}$");
		Matcher matcher = pattern.matcher(hashedPassword);

		return matcher.matches() && hashedPassword != "cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc83f4a921d36ce9ce47d0d13c5d85f2b0ff8318d2877eec2f63b931bd47417a81a538327af927da3e";
	}

	// --- Board verification

	/**
	 * Verify the board name (board id)
	 * 
	 * @return True if the board name is correct
	 */
	public static boolean isValidBoardName(String boardName) {
		Pattern pattern = Pattern.compile("^[A-Za-z0-9]+$");
		Matcher matcher = pattern.matcher(boardName);

		return matcher.matches();
	}

	// --- Message verification

	/**
	 * Verify the message ID
	 * 
	 * @param messageId The message ID to verify
	 * @return True if the ID iv valid
	 */
	public static boolean isValidMessageId(String messageId) {
		Pattern pattern = Pattern.compile("^([0-9]\\.)*[0-9]$");
		Matcher matcher = pattern.matcher(messageId);

		return matcher.matches();
	}

}
