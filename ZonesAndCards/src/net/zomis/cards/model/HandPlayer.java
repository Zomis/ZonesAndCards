package net.zomis.cards.model;

import net.zomis.cards.resources.ResourceMap;

public interface HandPlayer {

	CardZone getHand();
	ResourceMap getResources();
	String getName();
	
}
