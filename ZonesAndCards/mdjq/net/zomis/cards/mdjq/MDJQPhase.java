package net.zomis.cards.mdjq;

import net.zomis.cards.model.GamePhase;

public class MDJQPhase extends GamePhase {

	private String	id;

	public MDJQPhase(MDJQPlayer pl, String identifier) {
		super(pl);
		this.id = identifier;
	}
	
	@Override
	public MDJQPlayer getPlayer() {
		return (MDJQPlayer) super.getPlayer();
	}
	
	@Override
	public String toString() {
		return super.toString() + "-" + id;
	}

}
