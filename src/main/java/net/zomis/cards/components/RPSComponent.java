package net.zomis.cards.components;

import net.zomis.cards.iface.Component;
import net.zomis.cards.sets.RPSCardsSystem.RPS;

public class RPSComponent implements Component {

	private final RPS	rps;

	public RPSComponent(RPS rps) {
		this.rps = rps;
	}
	
	public RPS getRps() {
		return rps;
	}
	
}
