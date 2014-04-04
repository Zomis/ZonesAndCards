package net.zomis.cards.hstone.factory;

import net.zomis.cards.hstone.HSFilter;
import net.zomis.cards.hstone.HStoneTarget;

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
	
	public boolean isValidTarget(HStoneTarget target) {
		for (HSFilter filter : filters) {
			if (filter != null && !filter.shouldKeep(target))
				return false;
		}
		return true;
	}
	
	@Override
	public final boolean shouldKeep(HStoneTarget obj) {
		return isValidTarget(obj);
	}

	public abstract void performEffect(HStoneTarget source, HStoneTarget target);
	
	
}
