package net.zomis.cards.mdjq.events;

import net.zomis.cards.events.PhaseChangeEvent;
import net.zomis.cards.mdjq.MDJQGame;

public class MDJQPhaseChangeEvent extends MDJQEvent {

	private PhaseChangeEvent	event;

	public MDJQPhaseChangeEvent(PhaseChangeEvent ev) {
		super((MDJQGame) ev.getGame());
		this.event = ev;
	}
	
	public PhaseChangeEvent getEvent() {
		return event;
	}

}
