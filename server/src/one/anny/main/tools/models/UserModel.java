package one.anny.main.tools.models;

import java.sql.Date;

import org.json.simple.JSONObject;

/**
 * This is the wrapper class for a user. It contains all informations about an user
 *
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class UserModel {

	// ----- Attributes -----


	/**	Database id of the user (DB key) */
	private String userId;

	/** Public pseudo of the user */
	private String userPseudo;

	/** User's email address */
	private String userEmail;

	/** User's password */
	private String userPassword;

	/** User's inscription date */
	private Date userDate;

	/** True if the user in an administrator */
	private Boolean userAdmin;


	// ----- Constructors -----


	/**
	 * Create a new empty user
	 */
	public UserModel() {
		this.userId = null;
		this.userPseudo = null;
		this.userEmail = null;
		this.userPassword = null;
		this.userDate = null;
		this.userAdmin = null;
	}


	// ----- Getters -----


	public String getUserId() {
		return this.userId;
	}


	public String getUserPseudo() {
		return this.userPseudo;
	}

	public String getUserEmail() {
		return this.userEmail;
	}

	public String getUserPassword() {
		return this.userPassword;
	}

	public Date getUserDate() {
		return this.userDate;
	}

	public Boolean isUserAdmin() {
		return this.userAdmin;
	}
	
	/**
	 * Get the JSON representation of an user
	 * 
	 * @return The user JSON
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getJSON(boolean adminRequest) {
		// Place the user information in a JSON
		JSONObject res = new JSONObject();
		
		res.put("userId", this.userId);
		res.put("userPseudo", this.userPseudo);
		res.put("userDate", this.userDate.toString());
		if(adminRequest) {
			res.put("userAdmin", this.userAdmin);
		}
		
		// Return the result
		return res;
	}


	// ----- Setters -----


	public void setUserId(String id) {
		this.userId = id;
	}

	public void setUserPseudo(String pseudo) {
		this.userPseudo = pseudo;
	}

	public void setUserEmail(String email) {
		this.userEmail = email;
	}

	public void setUserPassword(String password) {
		this.userPassword= password;
	}

	public void setUserDate(Date date) {
		this.userDate = date;
	}

	public void setUserAdmin(Boolean admin) {
		this.userAdmin = admin;
	}


	// ----- Standards methods -----


	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof UserModel)) {
			return false;
		} else {
			UserModel e = (UserModel) obj;
			return this.userId.equals(e.userId);
		}
	}	

}
