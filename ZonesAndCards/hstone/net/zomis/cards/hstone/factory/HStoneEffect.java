package net.zomis.cards.hstone.factory;

import net.zomis.cards.hstone.HSFilter;
import net.zomis.cards.hstone.HStoneCard;

public abstract class HStoneEffect implements HSFilter {
	private HSFilter[]	filters;

	public HStoneEffect() {
		this((HSFilter) null);
	}
	public HStoneEffect(HSFilter... filters) {
		this.filters = filters;
	}
	
	public boolean needsTarget() {
		return filters != null && filters.length > 0 && filters[0] != null;
	}
	
	public boolean isValidTarget(HStoneCard searcher, HStoneCard target) {
		for (HSFilter filter : filters) {
			if (filter != null && !filter.shouldKeep(searcher, target))
				return false;
		}
		return true;
	}
	
	@Override
	public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
		return isValidTarget(searcher, target);
	}

	public abstract void performEffect(HStoneCard source, HStoneCard target);
	
	
}
