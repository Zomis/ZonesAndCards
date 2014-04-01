package net.zomis.cards.hstone.actions;

import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStonePlayer;
import net.zomis.cards.hstone.HStoneTarget;
import net.zomis.cards.hstone.factory.HSAbility;
import net.zomis.utils.ZomisList.FilterInterface;

public class AttackSelection implements FilterInterface<HStoneTarget> {

//	private HStoneTarget source;

	public AttackSelection(HStoneTarget source) {
//		this.source = source;
	}

	@Override
	public boolean shouldKeep(HStoneTarget target) {
		if (target instanceof HStoneCard) {
			HStoneCard card = (HStoneCard) target;
			HStonePlayer cardOwner = (HStonePlayer) card.getCurrentZone().getOwner();
			if (!card.hasAbility(HSAbility.TAUNT) && cardOwner.hasTauntMinions())
				return false;
			else return true;
		}
		if (target instanceof HStonePlayer) {
			HStonePlayer player = (HStonePlayer) target;
			if (!player.hasTauntMinions())
				return true;
		}
		return false;
	}

}
