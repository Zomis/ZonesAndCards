package net.zomis.cards.hstone.ench;

import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneRes;

@Deprecated
public class HStoneEnchSetTo extends HStoneEnchantment {
	
	private HStoneRes	resource;
	private int	value;
	private HStoneCard	card;

	public HStoneEnchSetTo(HStoneCard card, HStoneRes resource, int value) {
		this.card = card;
		this.resource = resource;
		this.value = value;
	}
	
	@Override
	public boolean appliesTo(HStoneCard card) {
		return this.card == card;
	}
	
	@Override
	public Integer getResource(HStoneCard card, HStoneRes resource, Integer resources) {
		if (this.resource == resource)
			return value;
		return resources;
	}

}
