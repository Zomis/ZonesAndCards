package net.zomis.cards.events2;

import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.events.CardGameEvent;

public class CompGameEvent extends CardGameEvent {

	public CompGameEvent(FirstCompGame game) {
		super(game);
	}
	
	@Override
	public final FirstCompGame getGame() {
		return (FirstCompGame) super.getGame();
	}

}
