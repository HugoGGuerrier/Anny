package tools.models;

/**
 * This is the wrapper class for a user. It contains all informations about an user
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class User {
	
	/**	Database id of the user */
	private String idUser;
	
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
	public User(String idUser) {
		this.idUser = idUser;
	}
	
	/**
	 * Create a new User with the default database ID null
	 */
	public User() {
		this(null);
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
	
	public boolean isAdmin() {
		return this.admin;
	}


	// ----- Setters -----
	
	
	public void setIdUser(String idUser) {
		this.idUser = idUser;
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
	
	
	// ----- Class methods -----
	
	
	/**
	 * Verify the user ID with a regexp
	 * 
	 * @param userId The ID to validate
	 * @return True if the ID is valid
	 */
	public static boolean isValidId(String userId) {
		// TODO : Faire la vérification de l'id par une expression régulière
		
		return true;
	}
	
	/**
	 * Verify the email address with a regexp
	 * 
	 * @param userMail The mail to verify
	 * @return True if the mail is valid
	 */
	public static boolean isValidMail(String userMail) {
		// TODO : Faire la vérification du mail
		
		return true;
	}
	
	/**
	 * Get the correctly hashed password with the defined algorithm
	 * 
	 * @param nonHashedPassword The password to hash
	 * @return The hashed password
	 */
	public static String hashPassword(String nonHashedPassword) {
		// TODO : Hasher le mot de passe avec l'algorithme sha-512
		String res = "";
		
		return res;
	}
	
	
	// ----- Standards methods -----
	

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof User)) {
			return false;
		} else {
			User user = (User) obj;
			
			return this.idUser.equals(user.idUser);
		}
	}
	
}
