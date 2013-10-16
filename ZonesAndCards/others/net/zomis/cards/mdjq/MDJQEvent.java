package net.zomis.cards.mdjq;

import net.zomis.events.IEvent;

public class MDJQEvent implements IEvent {

	private MDJQGame game;
	
	public MDJQEvent(MDJQGame game) {
		this.game = game;
	}
	
	public MDJQGame getGame() {
		return game;
	}
	
}
