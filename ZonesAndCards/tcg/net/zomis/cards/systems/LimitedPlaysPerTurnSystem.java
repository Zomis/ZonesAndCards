package net.zomis.cards.systems;

import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.events.card.CardPlayedEvent;
import net.zomis.cards.events.game.PhaseChangeEvent;

public class LimitedPlaysPerTurnSystem implements GameSystem {

	private int cardsPlayedThisTurn;
	private final int limit;
	
	public LimitedPlaysPerTurnSystem(int limit) {
		this.limit = limit;
	}
	
	@Override
	public void onStart(FirstCompGame game) {
		game.registerHandler(CardPlayedEvent.class, this::onCardPlayed);
		game.registerHandler(PhaseChangeEvent.class, this::onNewTurn);
	}
	
	private void onNewTurn(PhaseChangeEvent event) {
		// TODO: This is technically not turn-dependant, only phase-dependant. One turn can consist of many phases
		this.cardsPlayedThisTurn = 0;
	}
	
	private void onCardPlayed(CardPlayedEvent event) {
		this.cardsPlayedThisTurn++;
		if (this.cardsPlayedThisTurn >= limit)
			event.getGame().nextPhase();
	}
	
	

}
