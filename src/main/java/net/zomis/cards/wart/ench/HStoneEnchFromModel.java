package net.zomis.cards.wart.ench;

import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.HStoneRes;
import net.zomis.cards.wart.factory.CardType;
import net.zomis.cards.wart.factory.HSAbility;

public class HStoneEnchFromModel extends HStoneEnchantment {
	@Override
	public boolean appliesTo(HStoneCard card) {
		return true;
	}

	@Override
	public Integer getResource(HStoneCard card, HStoneRes resource, Integer resources) {
		if (resource == HStoneRes.ATTACK) {
			int value = card.getResources().get(resource);
			if (card.isType(CardType.PLAYER)) {
				HStoneCard weapon = card.getPlayer().getWeapon();
				if (weapon != null && card.getGame().getCurrentPlayer() == card.getPlayer()) {
					value += card.getPlayer().getWeapon().getAttack();
				}
			}
			
			return value;
		}
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
