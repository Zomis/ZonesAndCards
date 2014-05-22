package net.zomis.cards.hstone.factory;

import net.zomis.cards.hstone.HSAction;
import net.zomis.cards.hstone.HSFilter;
import net.zomis.cards.hstone.HStoneCard;

public class HStoneConditionalEffect extends HStoneEffect {
	private final HSFilter condition;
	private final HSAction action;

	public HStoneConditionalEffect(HSFilter condition, HSAction action) {
		super();
		this.condition = condition;
		this.action = action;
	}
	
	@Override
	public boolean needsTarget() {
		return false;
	}
	
	@Override
	public boolean hasAnyAvailableTargets(HStoneCard searcher) {
		return condition.shouldKeep(searcher, null);
	}
	
	@Override
	public void performEffect(HStoneCard source, HStoneCard target) {
		action.performEffect(source, target);
	}
	
	@Override
	public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
		return false;
	}
}
