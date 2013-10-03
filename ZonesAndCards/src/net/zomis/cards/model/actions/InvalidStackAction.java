package net.zomis.cards.model.actions;

import net.zomis.cards.model.StackAction;

public class InvalidStackAction extends StackAction {

	@Override
	public boolean isAllowed() {
		return false;
	}
	@Override
	protected void perform() {
		throw new AssertionError("Action is not allowed and should therefore never be performed.");
	}
	
}
