package net.zomis.cards.model;

import net.zomis.custommap.CustomFacade;



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
	
	protected boolean setErrorMessage(String string) {
		this.message = string;
		return false;
	}
	protected boolean setOKMessage(String string) {
		this.message = string;
		return true;
	}
	public String getMessage() {
		return message;
	}
	
	void internalPerform() {
		this.onPerform();
		++this.performCounter;
		if (this.performCounter > 1) {
			CustomFacade.getLog().w("StackAction performed " + this.performCounter + " times: " + this);
		}
	}
	
	public int getPerformCounter() {
		return performCounter;
	}
	
	public boolean actionIsPerformed() {
		return performCounter > 0;
	}
}
