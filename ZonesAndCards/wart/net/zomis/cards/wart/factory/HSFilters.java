package net.zomis.cards.wart.factory;

import net.zomis.cards.wart.HSFilter;
import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.HStonePlayer;
import net.zomis.cards.wart.HStoneRes;

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
				return target.isMinion() || target.isPlayer();
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

	public static HSFilter canTakeDamage(final int i) {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return target.getHealth() - target.getResources().getResources(HStoneRes.AWAITING_DAMAGE) >= i;
			}
		};
	}

	public static HSFilter enemy() {
		return not(samePlayer());
	}

	public static HSFilter enrage() {
		return thisCard().and(isDamaged());
	}

	public static HSFilter isDamaged() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return target.getHealth() < target.getResources().get(HStoneRes.MAX_HEALTH);
			}
		};
	}

	public static HSFilter isSpell() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return target.isType(CardType.SPELL);
			}
		};
	}
	
	public static HSFilter opponent() {
		return not(samePlayer());
	}

	public static HSFilter opponentMinions() {
		return allMinions().and(opponent());
	}

	public static HSFilter targetPlayerHasWeapon() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return target.getPlayer().getWeapon() != null;
			}
		};
	}

	public static HSFilter isActiveSecret() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				boolean correctZone = searcher.getCurrentZone() == searcher.getPlayer().getSecrets();
				boolean opponentsTurn = searcher.getGame().getCurrentPlayer() != searcher.getPlayer();
				return correctZone && opponentsTurn;
			}
		};
	}
	
	public static HSFilter haveBeast() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				for (HStoneCard card : searcher.getPlayer().getBattlefield()) {
					if (card.getModel().isOfType(HStoneMinionType.BEAST)) {
						return true;
					}
				}
				return false;
			}
		};
	}

	public static final HSFilter haveWeapon = (src, dst) -> src.getPlayer().getWeapon() != null;

	public static final HSFilter isWeapon = (src, dst) -> dst.getPlayer().getWeapon() == dst;

	public static final HSFilter isTargetAlive = (src, dst) -> dst.isAlive();

	public static final HSFilter haveSpaceOnBattleField = (src, dst) -> src.getPlayer().getBattlefield().size() < HStonePlayer.MAX_BATTLEFIELD_SIZE;

	public static final HSFilter opponentHasMinions(int count) {
		return (src, dst) -> src.getPlayer().getNextPlayer().getBattlefield().size() >= count;
	}

	public static final HSFilter undamaged = (src, target) -> target.getHealth() == target.getHealthMax();
	
	public HSFilter countAtLeast(HSFilter filter, int moreThanOrEqualTo) {
		return (src, dst) -> src.getGame().findAll(src, filter).size() >= moreThanOrEqualTo;
	}

}
