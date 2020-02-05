package tools.models;

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
	
	
	// ----- Constructors -----
	
	
	public User(String idUser) {
		this.idUser = idUser;
	}
	
	
	// ----- Getters -----

	
	public String getIdUser() {
		return idUser;
	}


	public String getPseudo() {
		return pseudo;
	}


	public String getName() {
		return name;
	}


	public String getSurname() {
		return surname;
	}


	public String getEmail() {
		return email;
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
	
	
	
	
}
