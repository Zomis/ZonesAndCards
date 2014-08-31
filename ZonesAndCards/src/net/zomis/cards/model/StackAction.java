package net.zomis.cards.model;




public class StackAction {
	private int	performCounter;
	private String	message;

	/**
	 * Given this game state, is the action allowed?
	 * 
	 * @return True if this action is currently allowed, false otherwise
	 */
	public boolean actionIsAllowed() {
		return true;
	}
	/**
	 * Perform this action -- This should be called by the {@link CardGame}, normally you don't need to call this.
	 */
	protected void onPerform() {} // throw IllegalStateException if this is called directly? It's possible, but is it good design?
	
	protected final boolean setErrorMessage(String string) {
		this.message = string;
		return false;
	}
	protected final boolean setOKMessage(String string) {
		this.message = string;
		return true;
	}
	protected final boolean setMixedMessage(boolean returnCondition, String string) {
		this.message = string;
		return returnCondition;
	}
	public final String getMessage() {
		return message;
	}
	
	void internalPerform() {
		this.onPerform();
		++this.performCounter;
	}
	
	public final int getPerformCounter() {
		return performCounter;
	}
	
	public final boolean actionIsPerformed() {
		return performCounter > 0;
	}
	
	protected void onFailedPerform() {	}
}
