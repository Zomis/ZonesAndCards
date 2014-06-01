package net.zomis.cards.wart.factory;

import net.zomis.cards.wart.HSFilter;
import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.HStonePlayer;
import net.zomis.cards.wart.HStoneRes;

public class HSFilters {

	private static final HSGetCounts g = new HSGetCounts();
	private static final Battlecry e = new Battlecry();
	
	public HSFilter shamanCanPlayTotem() {
		return (src, dst) -> e.summonableTotems(src.getPlayer()).size() > 0;
	}

	public HSFilter thisCard() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return searcher == target;
			}
		};
	}

	public HSFilter minionWithDivineShield() {
		return minionHasAbility(HSAbility.DIVINE_SHIELD);
	}

	public HSFilter minionHasAbility(final HSAbility ability) {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return target.isMinion() && target.hasAbility(ability);
			}
		};
	}

	public HSFilter all() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return target.isMinion() || target.isPlayer();
			}
		};
	}

	public HSFilter allMinions() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return target.isMinion();
			}
		};
	}

	public HSFilter allPlayers() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return target.isPlayer();
			}
		};
	}

	public HSFilter minionIsMurloc() {
		return minionIs(HStoneMinionType.MURLOC);
	}

	public HSFilter samePlayer() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return searcher.getPlayer() == target.getPlayer();
			}
		};
	}

	public HSFilter not(final HSFilter filter) {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return !filter.shouldKeep(searcher, target);
			}
		};
	}

	public HSFilter anotherCard() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return searcher != target;
			}
		};
	}

	public HSFilter withAttackLess(final int thisOrLess) {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return target.getAttack() <= thisOrLess;
			}
		};
	}

	public HSFilter withAttackMore(final int thisOrMore) {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return target.getAttack() >= thisOrMore;
			}
		};
	}

	public HSFilter adjacents() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return searcher.getLeftAdjacent() == target || searcher.getRightAdjacent() == target;
			}
		};
	}

	public HSFilter minionIs(final HStoneMinionType minionType) {
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

	public HSFilter canTakeDamage(final int i) {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return target.getHealth() - target.getResources().getResources(HStoneRes.AWAITING_DAMAGE) >= i;
			}
		};
	}

	public HSFilter enemy() {
		return not(samePlayer());
	}

	public HSFilter enrage() {
		return thisCard().and(isDamaged());
	}

	public HSFilter isDamaged() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return target.getHealth() < target.getResources().get(HStoneRes.MAX_HEALTH);
			}
		};
	}

	public HSFilter isSpell() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return target.isType(CardType.SPELL);
			}
		};
	}
	
	public HSFilter opponent() {
		return not(samePlayer());
	}

	public HSFilter opponentMinions() {
		return allMinions().and(opponent());
	}

	public HSFilter targetPlayerHasWeapon() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return target.getPlayer().getWeapon() != null;
			}
		};
	}

	public HSFilter isActiveSecret() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				boolean correctZone = searcher.getCurrentZone() == searcher.getPlayer().getSecrets();
				boolean opponentsTurn = searcher.getGame().getCurrentPlayer() != searcher.getPlayer();
				return correctZone && opponentsTurn;
			}
		};
	}
	
	public HSFilter haveBeast() {
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

	public final HSFilter haveWeapon = (src, dst) -> src.getPlayer().getWeapon() != null;

	public final HSFilter isWeapon = (src, dst) -> dst.getPlayer().getWeapon() == dst;

	public final HSFilter isTargetAlive = (src, dst) -> dst.isAlive();

	public final HSFilter haveSpaceOnBattleField = (src, dst) -> src.getPlayer().getBattlefield().size() < HStonePlayer.MAX_BATTLEFIELD_SIZE;

	public final HSFilter opponentHasMinions(int count) {
		return (src, dst) -> src.getPlayer().getNextPlayer().getBattlefield().size() >= count;
	}

	public final HSFilter undamaged = (src, target) -> target.getHealth() == target.getHealthMax();
	
	public HSFilter countAtLeast(HSFilter filter, int moreThanOrEqualTo) {
		return (src, dst) -> src.getGame().findAll(src, filter).size() >= moreThanOrEqualTo;
	}

	public final HSFilter isCombo = (src, dst) -> g.calcCombo.determineCount(src, dst) > 1;
	public final HSFilter canDrawCard = (src, dst) -> !src.getPlayer().getDeck().isEmpty();
	
	public HSFilter playerControlsSecret() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return !searcher.getPlayer().getSecrets().isEmpty();
			}
		};
	}

}
