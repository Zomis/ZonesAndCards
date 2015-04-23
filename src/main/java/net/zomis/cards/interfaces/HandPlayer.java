package net.zomis.cards.interfaces;

import net.zomis.cards.model.CardZone;
import net.zomis.cards.resources.ResourceMap;

public interface HandPlayer {

	CardZone<?> getHand();
	ResourceMap getResources();
	String getName();
	
}
