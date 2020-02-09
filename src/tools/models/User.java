package tools.models;

/**
 * This is the wrapper class for a user. It contains all informations about an user
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class User {
	
	/**	Database id of the user */
	private final String idUser;
	
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
	
	
	// ----- Constructors -----
	
	
	public User(String idUser) {
		this.idUser = idUser;
	}
	
	
	// ----- Getters -----

	
	public String getIdUser() {
		return this.idUser;
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


	// ----- Setters -----
	
	
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
	
}
