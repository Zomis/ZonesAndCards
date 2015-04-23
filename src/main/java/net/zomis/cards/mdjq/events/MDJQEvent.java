package net.zomis.cards.mdjq.events;

import net.zomis.cards.mdjq.MDJQGame;
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
