package net.zomis.cards.model;

import java.util.LinkedList;
import java.util.List;

public class StackMultiAction extends StackAction {

	private final List<StackAction>	list;
	
	public StackMultiAction() {
		this.list = new LinkedList<StackAction>();
	}
	
	@Override
	public boolean actionIsAllowed() {
		boolean allowed = true;
		for (StackAction act : this.list)
			allowed = act.actionIsAllowed() && allowed;
		return allowed;
	}
	@Override
	protected void onPerform() {
		for (StackAction act : this.list)
			act.onPerform();
	}

	public void addAction(StackAction action) {
		this.list.add(action);
	}
	
}
