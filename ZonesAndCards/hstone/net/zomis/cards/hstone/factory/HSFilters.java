package net.zomis.cards.hstone.factory;

import net.zomis.cards.hstone.HSFilter;
import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneRes;

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
		return minionHasAbility(HSAbility.DIVINE_SHIELD);
	}

	public static HSFilter minionHasAbility(final HSAbility ability) {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return target.isMinion() && target.hasAbility(ability);
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

	public static HSFilter allMinions() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return target.isMinion();
			}
		};
	}

	public static HSFilter allPlayers() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return target.isPlayer();
			}
		};
	}

	public static HSFilter minionIsMurloc() {
		return minionIs(HStoneMinionType.MURLOC);
	}

	public static HSFilter samePlayer() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return searcher.getPlayer() == target.getPlayer();
			}
		};
	}

	public static HSFilter not(final HSFilter filter) {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return !filter.shouldKeep(searcher, target);
			}
		};
	}

	public static HSFilter anotherCard() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return searcher != target;
			}
		};
	}

	public static HSFilter withAttackLess(final int thisOrLess) {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return target.getAttack() <= thisOrLess;
			}
		};
	}

	public static HSFilter withAttackMore(final int thisOrMore) {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return target.getAttack() >= thisOrMore;
			}
		};
	}

	public static HSFilter and(final HSFilter... filters) {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				boolean result = true;
				for (HSFilter filter : filters)
					result = result && filter.shouldKeep(searcher, target);
				return result;
			}
		};
	}

	public static HSFilter adjacents() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return searcher.getLeftAdjacent() == target || searcher.getRightAdjacent() == target;
			}
		};
	}

	public static HSFilter minionIs(final HStoneMinionType minionType) {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				if (!target.isMinion())
					return false;
				HStoneCard card = (HStoneCard) target;
				return card.getModel().isOfType(minionType);
			}
		};
	}

	public static HSFilter undamaged() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return target.getHealth() == target.getHealthMax();
			}
		};
	}

	public static HSFilter canTakeDamage(final int i) {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return target.getHealth() - target.getResources().getResources(HStoneRes.AWAITING_DAMAGE) >= i;
			}
		};
	}

}
