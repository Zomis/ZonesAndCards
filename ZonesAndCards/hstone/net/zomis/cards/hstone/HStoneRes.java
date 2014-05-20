package net.zomis.cards.hstone;

import net.zomis.cards.resources.IResource;
import net.zomis.cards.resources.ResourceData;

public enum HStoneRes implements IResource {
	// both minions and players
	HEALTH, MAX_HEALTH,
	ATTACK,
	ACTION_POINTS_USED,
	AWAITING_DAMAGE, AWAITING_HEAL, 
	
	// Minions only
	SPELL_DAMAGE,
	
	// Players only
	CARDS_PLAYED,
	MANA_AVAILABLE,
	MANA_TOTAL,
	MANA_OVERLOAD,
	ARMOR,
	
	/**
	 * Number of times player has attempted to draw a card without having cards in library
	 */
	EMPTY_DRAWS, MANA_COST
	;

	@Override
	public ResourceData createData(IResource resource) {
		return ResourceData.forResource(resource);
	}
}
