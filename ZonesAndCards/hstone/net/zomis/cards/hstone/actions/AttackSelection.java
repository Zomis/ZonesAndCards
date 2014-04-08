package net.zomis.cards.hstone.actions;

import net.zomis.cards.hstone.HSFilter;
import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStonePlayer;
import net.zomis.cards.hstone.factory.CardType;
import net.zomis.cards.hstone.factory.HSAbility;

public class AttackSelection implements HSFilter {

	private final HStoneCard source;

	public AttackSelection(HStoneCard source) {
		this.source = source;
	}

	@Override
	public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
		if (source.getPlayer() == target.getPlayer())
			return false;
		
		HStonePlayer cardOwner = target.getPlayer();
		if (target.isType(CardType.PLAYER)) {
			if (!cardOwner.hasTauntMinions())
				return true;
		}
		
		return target.hasAbility(HSAbility.TAUNT) || !cardOwner.hasTauntMinions();
	}

}
