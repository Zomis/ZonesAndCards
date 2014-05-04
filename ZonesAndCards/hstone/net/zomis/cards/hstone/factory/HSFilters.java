package net.zomis.cards.hstone.factory;

import net.zomis.cards.hstone.HSFilter;
import net.zomis.cards.hstone.HStoneCard;

public class HSFilters {

	public static HSFilter thisCard() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return searcher == target;
			}
		};
	}

	public static HSFilter minionWithDivineShield() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return target.isMinion() && target.hasAbility(HSAbility.DIVINE_SHIELD);
			}
		};
	}

	public static HSFilter all() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return true;
			}
		};
	}

	public static HSFilter minionIsMurloc() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				if (!target.isMinion())
					return false;
				HStoneCard card = (HStoneCard) target;
				return card.getModel().isOfType(HStoneMinionType.MURLOC);
			}
		};
	}

	public static HSFilter samePlayer() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return searcher.getPlayer() == target.getPlayer();
			}
		};
	}

}
