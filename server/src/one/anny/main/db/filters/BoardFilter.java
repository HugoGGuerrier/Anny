package one.anny.main.db.filters;

import java.util.HashSet;
import java.util.Set;

/**
 * The filter to query the BOARD table in the database
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class BoardFilter extends AbstractFilter {
	
	// ----- Attributes -----
	
	
	/** Possible board names */
	private Set<String> boardNameSet;
	
	/** Possible board description */
	private Set<String> boardDescriptionSet;
	
	/** Creator id to look for */
	private Set<String> boardCreatorIdSet;
	
	
	// ----- Constructors -----
	
	
	/**
	 * Construct a new board filter with default void value
	 */
	public BoardFilter() {
		this.boardNameSet = new HashSet<String>();
		this.boardDescriptionSet = new HashSet<String>();
		this.boardCreatorIdSet = new HashSet<String>();
	}
	
	
	// ----- Getters -----
	
	
	public Set<String> getBoardNameSet() {
		return this.boardNameSet;
	}
	
	public Set<String> getBoardDescriptionSet() {
		return this.boardDescriptionSet;
	}
	
	public Set<String> getBoardCreatorIdSet() {
		return this.boardCreatorIdSet;
	}
	
	
	// ----- Setters -----
	
	
	// --- Board name
	
	public void setBoardNameSet(Set<String> boardNameSet) {
		this.boardNameSet = boardNameSet;
	}
	
	public void addBoardName(String boardName) {
		this.boardNameSet.add(boardName);
	}
	
	// --- Board description
	
	public void setBoardDescriptionSet(Set<String> boardDescriptionSet) {
		this.boardDescriptionSet = boardDescriptionSet;
	}
	
	public void addBoardDescription(String boardDescription) {
		this.boardDescriptionSet.add(boardDescription);
	}
	
	// --- Board creator id
	
	public void setBoardCreatorIdSet(Set<String> boardCreatorIdSet) {
		this.boardCreatorIdSet = boardCreatorIdSet;
	}
	
	public void addBoardCreatorId(String boardCreatorId) {
		this.boardCreatorIdSet.add(boardCreatorId);
	}

}
