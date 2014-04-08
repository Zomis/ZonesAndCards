package net.zomis.cards.hstone;

import net.zomis.cards.resources.IResource;
import net.zomis.cards.resources.ResourceData;

public enum HStoneRes implements IResource {
	HEALTH, // both minions and players
	ATTACK, // both minions and players
	MANA_AVAILABLE,
	MANA_TOTAL,
	MANA_OVERLOAD,
	ACTION_POINTS,
	/**
	 * Number of times player has attempted to draw a card without having cards in library
	 */
	EMPTY_DRAWS, AWAITING_DAMAGE, AWAITING_HEAL, ARMOR,
	;

	@Override
	public ResourceData createData(IResource resource) {
		return ResourceData.forResource(resource);
	}
}
