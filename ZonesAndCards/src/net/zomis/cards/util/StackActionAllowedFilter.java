package net.zomis.cards.util;

import net.zomis.cards.model.Card;
import net.zomis.utils.ZomisList.FilterInterface;

public class StackActionAllowedFilter implements FilterInterface<Card<?>> {

	private final boolean allowed;

	public StackActionAllowedFilter() {
		this(true);
	}
	public StackActionAllowedFilter(boolean allowed) {
		this.allowed = allowed;
	}
	
	@Override
	public boolean shouldKeep(Card<?> obj) {
		return obj.clickAction().actionIsAllowed() == this.allowed;
	}

}
