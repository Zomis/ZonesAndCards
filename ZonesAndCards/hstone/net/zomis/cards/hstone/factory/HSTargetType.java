package net.zomis.cards.hstone.factory;

import net.zomis.cards.hstone.HSFilter;
import net.zomis.cards.hstone.HStoneCard;

public enum HSTargetType implements HSFilter { // TODO: Deprecate HSTargetType class, use HSFilters instead
	PLAYER, MINION;

	@Override
	public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
		switch (this) {
			case PLAYER:
				return target.isType(CardType.PLAYER);
			case MINION:
				return target.isType(CardType.MINION);
		}
		return false;
	}
}
