package net.zomis.cards.mdjq;

import net.zomis.cards.model.StackAction;

public class MDJQStackAction extends StackAction {

	public static enum ActionType {
		LAND, SPELL, TRIGGERED, ACTIVATED;
	}
	protected boolean counterable = true;
	protected boolean useStack = true;
	protected final ActionType type;
	
	public MDJQStackAction(ActionType type) {
		this.type = type;
	}
	
	public void onAddToStack() {
		
	}
	
	@Override
	public void onPerform() { // TODO: Don't make onPerform public for MDJQ actions
	}
	
	public boolean isUseStack() {
		return useStack;
	}
	public boolean isCounterable() {
		return counterable;
	}
	public ActionType getType() {
		return type;
	}
	
}
