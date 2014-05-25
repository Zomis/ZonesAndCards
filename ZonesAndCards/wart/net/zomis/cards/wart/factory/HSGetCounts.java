package net.zomis.cards.wart.factory;

import net.zomis.cards.wart.HSFilter;
import net.zomis.cards.wart.HSGetCount;
import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.HStoneRes;

public class HSGetCounts {
	
	public static HSGetCount fixed(final int count) {
		return new HSGetCount() {
			@Override
			public int determineCount(HStoneCard source, HStoneCard target) {
				return count;
			}
		};
	}
	
	public static final HSGetCount calcWeaponDamage = (src, dst) -> src.getPlayer().getWeapon() != null ? src.getPlayer().getWeapon().getAttack() : 0;

	public static final HSGetCount calcCombo = (src, dst) -> src.getPlayer().getResources().get(HStoneRes.CARDS_PLAYED);

	public static final HSGetCount edwinBonus = (src, dst) -> (calcCombo.determineCount(src, dst) - 1) * 2;

	public static final HSFilter isCombo = (src, dst) -> calcCombo.determineCount(src, dst) > 1;
	
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

	public static final HSGetCount calcHeroArmor = (src, dst) -> src.getPlayer().getArmor();

	public static final HSGetCount myHeroAttack = (src, dst) -> src.getPlayer().getPlayerCard().getAttack();

}
