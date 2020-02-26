package tools.models;

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
	
	/** User's name */
	private String userName;
	
	/** User's surname */
	private String userSurname;
	
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
	 * Create a new user from a JSON
	 * 
	 * @param userJSON The user JSON
	 */
	public UserModel(JSONObject userJSON) {
		// TODO : Faire la création de l'utilisateur avec le JSON
	}
	
	/**
	 * Create a new empty user
	 */
	public UserModel() {
		this.userId = null;
		this.userPseudo = null;
		this.userName = null;
		this.userSurname = null;
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


	public String getUserName() {
		return this.userName;
	}


	public String getUserSurname() {
		return this.userSurname;
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


	// ----- Setters -----
	
	
	public void setUserId(String id) {
		this.userId = id;
	}
	
	public void setUserPseudo(String pseudo) {
		this.userPseudo = pseudo;
	}

	public void setUserName(String name) {
		this.userName = name;
	}

	public void setUserSurname(String surname) {
		this.userSurname = surname;
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
