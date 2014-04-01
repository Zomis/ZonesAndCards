package net.zomis.cards.util;

import net.zomis.cards.model.StackAction;
import net.zomis.utils.ZomisList.FilterInterface;

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
