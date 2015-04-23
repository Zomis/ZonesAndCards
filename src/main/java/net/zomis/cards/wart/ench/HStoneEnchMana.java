package net.zomis.cards.wart.ench;

import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.HStoneRes;

public abstract class HStoneEnchMana extends HStoneEnchantment {
	
	private final int change;

	public HStoneEnchMana(int change) {
		this.change = change;
	}
	
	@Override
	public abstract boolean appliesTo(HStoneCard card);
	
	@Override
	public Integer getResource(HStoneCard card, HStoneRes resource, Integer resources) {
		if (resource == HStoneRes.MANA_COST)
			return mana(card, resources);
		return resources;
	}

	protected Integer mana(HStoneCard card, Integer mana) {
		return mana + change;
	}
	
}
