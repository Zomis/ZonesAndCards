package net.zomis.cards.wart.factory;

import net.zomis.cards.wart.HSAction;
import net.zomis.cards.wart.HSFilter;
import net.zomis.cards.wart.HStoneCard;

public class HStoneConditionalEffect extends HStoneEffect {
	private final HSFilter condition;
	private final HSAction action;
	private final HSFilter target;

	public HStoneConditionalEffect(HSFilter condition, HSAction action) {
		this(condition, new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				action.performEffect(source, target);
			}
		}, null);
	}
	
	public HStoneConditionalEffect(HSFilter condition, HStoneEffect action, HSFilter target) {
		this.condition = condition;
		this.action = action;
		this.target = target;
	}

	@Override
	public boolean needsTarget() {
		return target != null;
	}
	
	@Override
	public boolean hasAnyAvailableTargets(HStoneCard searcher) {
		return condition.shouldKeep(searcher, searcher);
	}
	
	@Override
	public void performEffect(HStoneCard source, HStoneCard target) {
		action.performEffect(source, target);
	}
	
	@Override
	public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
		return this.target.shouldKeep(searcher, target);
	}
}
