package tools.models;

/**
 * This is the wrapper class for a user. It contains all informations about an user
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class UserModel {
	
	/**	Database id of the user */
	private String userId;
	
	/** Public pseudo of the user */
	private String pseudo;
	
	/** User's name */
	private String name;
	
	/** User's surname */
	private String surname;
	
	/** User's email address */
	private String email;
	
	/** User's password */
	private String password;
	
	/** True if the user in an administrator */
	private boolean admin;
	
	
	// ----- Constructors -----
	
	
	/**
	 * Create a new User with the wanted database ID
	 * 
	 * @param idUser
	 */
	public UserModel(String idUser) {
		this.userId = idUser;
	}
	
	/**
	 * Create a new User with the default database ID null
	 */
	public UserModel() {
		this(null);
	}
	
	
	// ----- Getters -----

	
	public String getIdUser() {
		return this.userId;
	}


	public String getPseudo() {
		return this.pseudo;
	}


	public String getName() {
		return this.name;
	}


	public String getSurname() {
		return this.surname;
	}


	public String getEmail() {
		return this.email;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public boolean isAdmin() {
		return this.admin;
	}


	// ----- Setters -----
	
	
	public void setIdUser(String idUser) {
		this.userId = idUser;
	}
	
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setSurname(String surname) {
		this.surname = surname;
	}


	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
	
	// ----- Standards methods -----
	

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof UserModel)) {
			return false;
		} else {
			UserModel user = (UserModel) obj;
			
			return this.userId.equals(user.userId);
		}
	}
	
}
