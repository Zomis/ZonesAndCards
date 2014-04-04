package net.zomis.cards.hstone.factory;

import net.zomis.cards.hstone.HSFilter;
import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStonePlayer;
import net.zomis.cards.hstone.HStoneRes;
import net.zomis.cards.hstone.HStoneTarget;
import net.zomis.cards.model.StackAction;

public class Battlecry {

	private static final HSFilter	ANY_TARGET	= combined(HSTargetType.MINION, HSTargetType.PLAYER);

	public static HSFilter combined(final HSTargetType... target) {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneTarget obj) {
				for (HSTargetType tar : target) {
					if (tar.shouldKeep(obj))
						return true;
				}
				return false;
			}
			
		};
	}
	
	public static HStoneEffect damage(final int damage, HSTargetType... targetType) {
		return new HStoneEffect(combined(targetType)) {
			@Override
			public void performEffect(HStoneTarget source, HStoneTarget target) {
				target.damage(damage);
			}
		};
	}

	public static HStoneEffect heal(final int healing, HSTargetType... target) {
		return new HStoneEffect(combined(target)) {
			@Override
			public void performEffect(HStoneTarget source, HStoneTarget target) {
				target.heal(healing);
			}
		};
	}

	public static HStoneEffect destroyOppWeapon() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneTarget source, HStoneTarget target) {
				HStonePlayer player = source.getPlayer();
				player.getNextPlayer().removeWeapon();
			}
		};
	}

	public static HStoneEffect freezeOrDamage(final int damage) {
		return new HStoneEffect(ANY_TARGET) {
			@Override
			public void performEffect(HStoneTarget source, HStoneTarget target) {
				if (target.isFrozen())
					target.damage(damage);
				else target.addAbility(HSAbility.FROZEN);
			}
		};
	}

	public static HStoneEffect tempMana(final int additionalMana) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneTarget source, HStoneTarget target) {
				HStoneCard card = (HStoneCard) source;
				card.getPlayer().getResources().changeResources(HStoneRes.MANA_AVAILABLE, additionalMana);
			}
		};
	}

	public static HStoneEffect damage(int damage) {
		return damage(damage, HSTargetType.MINION, HSTargetType.PLAYER);
	}

	public static HStoneEffect tempBoost(int i, int j) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneTarget source, HStoneTarget target) {
			}
		};
	}

	public static Object selfPT(int i, int j) {
		return null;
	}

	public static HStoneEffect removeDurabilityOppWeapon(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Object typePTBonus(HStoneMinionType murloc, int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	public static HStoneEffect silencer() {
		// TODO Auto-generated method stub
		return null;
	}

	public static HStoneEffect summon(final String minion) {
		return new HStoneEffect() {
			@Override
			public void performEffect(final HStoneTarget source, HStoneTarget target) {
				final HStoneCardModel model = source.getGame().getCardModel(minion);
				StackAction sa = new StackAction() {
					@Override
					protected void onPerform() {
						source.getPlayer().getBattlefield().createCardOnBottom(model);
					}
				};
				source.getGame().addStackAction(sa);
			}
		};
	}

	public static HStoneEffect drawCard() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneTarget source, HStoneTarget target) {
				source.getPlayer().drawCard();
			}
		};
	}

}
