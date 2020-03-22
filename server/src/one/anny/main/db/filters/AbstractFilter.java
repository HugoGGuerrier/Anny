package one.anny.main.db.filters;

/**
 * General information for database filter
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public abstract class AbstractFilter {
	
	// ----- Attributes -----
	
	
	/** The column to order the result by */
	private String orderColumn = null;
	
	/** If the order has to be reversed */
	private boolean reverseOrder = false;
	
	
	// ----- Getters -----
	
	
	public String getOrderColumn() {
		return this.orderColumn;
	}
	
	public boolean isOrderReversed() {
		return this.reverseOrder;
	}
	
	
	// ----- Setters -----
	
	
	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}
	
	public void setReverseOrder(boolean reverseOrder) {
		this.reverseOrder = reverseOrder;
	}

}
