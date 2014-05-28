package net.zomis.cards.components;

import net.zomis.cards.systems.RPSCardsSystem;
import net.zomis.cards.systems.RPSCardsSystem.RPS;

public class RPSComponent implements Component {

	private final RPS	rps;

	public RPSComponent(RPS rps) {
		this.rps = rps;
	}
	
	public RPS getRps() {
		return rps;
	}
	
}
