package net.zomis.cards.hstone.ench;

import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneRes;
import net.zomis.cards.hstone.factory.HSAbility;

public class HStoneEnchFromModel extends HStoneEnchantment {
	@Override
	public boolean appliesTo(HStoneCard card) {
		return true;
	}

	@Override
	public Integer getResource(HStoneCard card, HStoneRes resource, Integer resources) {
		if (resource == HStoneRes.ATTACK)
			return card.getResources().get(resource);
		if (resource == HStoneRes.MAX_HEALTH)
			return card.getResources().get(resource);
		if (resource == HStoneRes.MANA_COST)
			return card.getResources().get(resource);
		return resources;
	}

	@Override
	public boolean hasAbility(HStoneCard card, HSAbility ability, boolean hasAbility) {
		return card.hasOwnAbility(ability);
	}
}
