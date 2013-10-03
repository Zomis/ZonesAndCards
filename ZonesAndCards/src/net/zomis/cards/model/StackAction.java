package net.zomis.cards.model;



public class StackAction {
	/**
	 * Given this game state, is the action allowed?
	 * 
	 * @return True if this action is currently allowed, false otherwise
	 */
	public boolean isAllowed() {
		return true;
	}
	/**
	 * Perform this action -- This should be called by the {@link CardGame}, normally you don't need to call this.
	 */
	protected void perform() {} // throw IllegalStateException if this is called directly? It's possible, but is it good design?
	
	/**
	 * Performed after this action has been initialized. That is, after it's {@link CardGame} reference has been set.
	 * Currently not used.
	 */
	@Deprecated
	public void init() {}
}
