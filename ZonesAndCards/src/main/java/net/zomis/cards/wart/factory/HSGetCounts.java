package net.zomis.cards.wart.factory;

import net.zomis.cards.wart.HSChangeCount;
import net.zomis.cards.wart.HSGetCount;
import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.HStonePlayer;
import net.zomis.cards.wart.HStoneRes;

public class HSGetCounts {
	
	public HSGetCount fixed(final int count) {
		return new HSGetCount() {
			@Override
			public int determineCount(HStoneCard source, HStoneCard target) {
				return count;
			}
		};
	}
	
	public final HSGetCount calcWeaponDamage = (src, dst) -> src.getPlayer().getWeapon() != null ? src.getPlayer().getWeapon().getAttack() : 0;

	public final HSGetCount calcCombo = (src, dst) -> src.getPlayer().getResources().get(HStoneRes.CARDS_PLAYED);

	public final HSGetCount edwinBonus = (src, dst) -> (calcCombo.determineCount(src, dst) - 1) * 2;

	public HSGetCount oppWeaponDurability() {
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
	
	public HSGetCount oppHandSizeMinusMyHandSize() {
		//   "Draw cards until you have as many in hand as your opponent"
		return new HSGetCount() {
			@Override
			public int determineCount(HStoneCard source, HStoneCard target) {
				int myHand = source.getPlayer().getHand().size();
				if (source.getCurrentZone() == source.getPlayer().getHand())
					myHand--;
				int oppHand = source.getPlayer().getNextPlayer().getHand().size();
				return oppHand - myHand;
			}
		};
	}

	public final HSGetCount calcHeroArmor = (src, dst) -> src.getPlayer().getArmor();

	public final HSGetCount myHeroAttack = (src, dst) -> src.getPlayer().getPlayerCard().getAttack();

	public final HSGetCount oneLessPerHeroDamage = (src, dst) -> src.getPlayer().getHealth() - src.getPlayer().getPlayerCard().getHealthMax();

	public final HSGetCount	oneLessPerOtherCardInHand = (src, dst) -> -src.getPlayer().getHand().size() + (src.getCurrentZone() == src.getPlayer().getHand() ? 1 : 0);

	public final HSGetCount oneLessPerOtherMinionOnBattlefield = new HSGetCount() {
		@Override
		public int determineCount(HStoneCard source, HStoneCard target) {
			int total = 0;
			for (HStonePlayer pl : source.getGame().getPlayers()) {
				total -= pl.getBattlefield().size();
				if (source.getCurrentZone() == pl.getBattlefield())
					total++;
			}
			return total;
		}
	};

	public HSChangeCount fixedChange(int i) {
		return (src, dst, input) -> input + i;
	}

}
