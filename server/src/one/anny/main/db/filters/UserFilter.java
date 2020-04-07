package one.anny.main.db.filters;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Filter to build query for the USER table
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class UserFilter extends AbstractFilter {
	
	// ----- Attributes -----
	
	
	/** Set of possible id to look for */
	private Set<String> userIdSet;
	
	/** Set of pseudo to look for */
	private Set<String> userPseudoSet;
	
	/** Set of email to look for */
	private Set<String> userEmailSet;
	
	/** Set of password to search */
	private Set<String> userPasswordSet;
	
	/** The possible dates */
	private Set<Date> userDateSet;
	
	/** Admin filter */
	private Boolean userAdmin;

	
	// ----- Constructors -----
	
	
	/**
	 * Construct a new filter with default void value
	 */
	public UserFilter() {
		this.userIdSet = new HashSet<String>();
		this.userPseudoSet = new HashSet<String>();
		this.userEmailSet = new HashSet<String>();
		this.userPasswordSet = new HashSet<String>();
		this.userDateSet = new HashSet<Date>();
		this.userAdmin = null;
	}
	
	
	// ----- Getters -----
	
	
	public Set<String> getUserIdSet() {
		return this.userIdSet;
	}
	
	public Set<String> getUserPseudoSet() {
		return this.userPseudoSet;
	}
	
	public Set<String> getUserEmailSet() {
		return this.userEmailSet;
	}
	
	public Set<String> getUserPasswordSet() {
		return this.userPasswordSet;
	}
	
	public Set<Date> getUserDateSet() {
		return this.userDateSet;
	}
	
	public Boolean isUserAdmin() {
		return this.userAdmin;
	}
	
	
	// ----- Setters -----
	
	
	// --- User id
	
	public void setUserIdSet(Set<String> userIdSet) {
		this.userIdSet = userIdSet;
	}
	
	public void addUserId(String userId) {
		this.userIdSet.add(userId);
	}
	
	// --- User pseudo
	
	public void setUserPseudoSet(Set<String> userPseudoSet) {
		this.userPseudoSet = userPseudoSet;
	}
	
	public void addUserPseudo(String userPseudo) {
		this.userPseudoSet.add(userPseudo);
	}
	
	// --- User email
	
	public void setUserEmailSet(Set<String> userEmailSet) {
		this.userEmailSet = userEmailSet;
	}
	
	public void addUserEmail(String userEmail) {
		this.userEmailSet.add(userEmail);
	}
	
	// --- User password
	
	public void setUserPasswordSet(Set<String> userPasswordSet) {
		this.userPasswordSet = userPasswordSet;
	}
	
	public void addUserPassword(String userPassword) {
		this.userPasswordSet.add(userPassword);
	}
	
	// --- User date
	
	public void setUserDateSet(Set<Date> userDateSet) {
		this.userDateSet = userDateSet;
	}
	
	public void addUserDate(Date userDate) {
		this.userDateSet.add(userDate);
	}
	
	// --- User admin
	
	public void setUserAdmin(Boolean userAdmin) {
		this.userAdmin = userAdmin;
	}
	
}
