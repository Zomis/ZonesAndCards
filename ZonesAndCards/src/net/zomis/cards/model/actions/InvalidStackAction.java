package net.zomis.cards.model.actions;

import net.zomis.cards.model.StackAction;

public class InvalidStackAction extends StackAction {

	private final String	description;

	public InvalidStackAction(String description) {
		this.description = description;
	}
	
	public InvalidStackAction() {
		this.description = super.toString();
	}
	
	@Override
	public boolean actionIsAllowed() {
		return false;
	}
	@Override
	protected void onPerform() {
		throw new AssertionError("Action is not allowed and should therefore never be performed.");
	}
	
	@Override
	public String toString() {
		return this.getClass().getCanonicalName() + ": " + this.description;
	}
	
}
