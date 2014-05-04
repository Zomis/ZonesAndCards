package net.zomis.cards.hstone.ench;

import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneRes;

public abstract class HStoneEnchPT extends HStoneEnchantment {
	
	@Override
	public abstract boolean appliesTo(HStoneCard card);
	
	@Override
	public Integer getResource(HStoneCard card, HStoneRes resource, Integer resources) {
		if (resource == HStoneRes.MAX_HEALTH)
			return health(card, resources);
		if (resource == HStoneRes.ATTACK)
			return attack(card, resources);
		return resources;
	}

	protected abstract Integer attack(HStoneCard card, Integer attack);

	protected abstract Integer health(HStoneCard card, Integer health);
	
}
