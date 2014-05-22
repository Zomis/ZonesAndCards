package net.zomis.cards.hstone.factory;

import net.zomis.cards.hstone.HSFilter;
import net.zomis.cards.hstone.HSGetCount;
import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneRes;

public class HSGetCounts {
	
	public static HSGetCount fixed(final int count) {
		return new HSGetCount() {
			@Override
			public int determineCount(HStoneCard source, HStoneCard target) {
				return count;
			}
		};
	}
	
	public static HSGetCount calcWeaponDamage() {
		return (src, dst) -> src.getPlayer().getWeapon() != null ? src.getPlayer().getWeapon().getAttack() : 0;
	}

	public static HSGetCount calcCombo() {
		return (src, dst) -> src.getPlayer().getResources().get(HStoneRes.CARDS_PLAYED);
	}

	public static HSGetCount edwinBonus() {
		return (src, dst) -> (calcCombo().determineCount(src, dst) - 1) * 2;
	}

	public static HSFilter isCombo() {
		return (src, dst) -> calcCombo().determineCount(src, dst) > 1;
	}
	
	public static HSGetCount oppWeaponDurability() {
		return new HSGetCount() {
			@Override
			public int determineCount(HStoneCard source, HStoneCard target) {
				HStoneCard weapon = source.getPlayer().getNextPlayer().getWeapon();
				if (weapon == null)
					return 0;
				return weapon.getHealth();
			}
		};
	}
	
	public static HSGetCount oppHandSizeMinusMyHandSize() {
		//   "Draw cards until you have as many in hand as your opponent"
		return new HSGetCount() {
			@Override
			public int determineCount(HStoneCard source, HStoneCard target) {
				int myHand = source.getPlayer().getHand().size(); // TODO: Do not count the card itself!
				int oppHand = source.getPlayer().getNextPlayer().getHand().size();
				return oppHand - myHand;
			}
		};
	}

	public static HSGetCount calcHeroArmor() {
		return (src, dst) -> src.getPlayer().getArmor();
	}

	public static HSGetCount myHeroAttack() {
		return (src, dst) -> src.getPlayer().getPlayerCard().getAttack();
	}

}
