package net.zomis.cards.util;

import net.zomis.ZomisList.FilterInterface;
import net.zomis.cards.model.StackAction;

public class StackActionAllowedFilter implements FilterInterface<StackAction> {

	private boolean	allowed;

	public StackActionAllowedFilter(boolean allowed) {
		this.allowed = allowed;
	}
	
	@Override
	public boolean shouldKeep(StackAction obj) {
		return obj.actionIsAllowed() == this.allowed;
	}

}
