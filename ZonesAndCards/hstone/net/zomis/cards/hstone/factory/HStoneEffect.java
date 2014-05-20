package net.zomis.cards.hstone.factory;

import net.zomis.cards.hstone.HSAction;
import net.zomis.cards.hstone.HSFilter;
import net.zomis.cards.hstone.HStoneCard;

public abstract class HStoneEffect implements HSFilter, HSAction {
	private HSFilter filters;

	public HStoneEffect() {
		this((HSFilter) null);
	}
	public HStoneEffect(HSFilter filters) {
		this.filters = filters;
	}
	
	public boolean needsTarget() {
		return filters != null;
	}
	
	public boolean hasAnyAvailableTargets(HStoneCard searcher) {
		return needsTarget() ? searcher.getGame().findAll(searcher, filters).size() > 0 : true;
	}
	
	@Override
	public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
		return filters.shouldKeep(searcher, target);
	}

	@Override
	public abstract void performEffect(HStoneCard source, HStoneCard target);
	
	
}
