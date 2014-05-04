package net.zomis.cards.mdjq;

import net.zomis.cards.mdjq.cards.TriggeredAbility;
import net.zomis.cards.mdjq.events.MDJQEvent;
import net.zomis.cards.model.CardZone;

public class MDJQZone extends CardZone<MDJQPermanent> {

	public static enum ZoneType {
		LIBRARY, HAND, BATTLEFIELD, GRAVEYARD, EXILE, TEMPORARY;
	}
	
	private ZoneType	zoneType;
	private MDJQPlayer	owner;
	
	public MDJQZone(String zoneName, ZoneType type, MDJQPlayer owner) {
		super(zoneName, owner);
		this.zoneType = type;
		this.owner = owner;
	}
	
	public MDJQPlayer getZoneOwner() {
		return owner;
	}
	
	@Override
	public MDJQGame getGame() {
		return (MDJQGame) super.getGame();
	}

	public boolean isHand() {
		return zoneType == ZoneType.HAND;
	}

	public MDJQPermanent findCardWithName(String string) {
		for (MDJQPermanent card : this) {
			if (card.getModel().getName().equals(string))
				return (MDJQPermanent) card;
		}
		return null;
	}

	void trigger(MDJQEvent event) {
		for (MDJQPermanent perm : this) {
			MDJQPermanent permanent = (MDJQPermanent) perm;
			for (TriggeredAbility ability : permanent.getModel().getTriggeredAbilities()) {
				ability.trigger(permanent, event);
			}
		}
	}

	
}
