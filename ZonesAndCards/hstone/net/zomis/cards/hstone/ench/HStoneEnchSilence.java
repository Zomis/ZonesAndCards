package net.zomis.cards.hstone.ench;

import net.zomis.cards.hstone.HStoneCard;

public class HStoneEnchSilence extends HStoneEnchFromModel {

	private HStoneCard	card;

	public HStoneEnchSilence(HStoneCard card) {
		this.card = card;
	}
	
	@Override
	public boolean appliesTo(HStoneCard card) {
		return this.card == card;
	}

}
