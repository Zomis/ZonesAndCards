package net.zomis.cards.systems;

import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.events.card.CardPlayedEvent;
import net.zomis.cards.events.game.PhaseChangeEvent;
import net.zomis.custommap.view.ZomisLog;
import net.zomis.events.EventExecutorGWT;

public class LimitedPlaysPerTurnSystem implements GameSystem {

	private int cardsPlayedThisTurn;
	private final int limit;
	
	public LimitedPlaysPerTurnSystem(int limit) {
		this.limit = limit;
	}
	
	@Override
	public void onStart(FirstCompGame game) {
		game.registerHandler(CardPlayedEvent.class, this::onCardPlayed, EventExecutorGWT.POST);
		game.registerHandler(PhaseChangeEvent.class, this::onNewTurn);
	}
	
	private void onNewTurn(PhaseChangeEvent event) {
		// TODO: This is technically not turn-dependant, only phase-dependant. One turn can consist of many phases
		this.cardsPlayedThisTurn = 0;
	}
	
	private void onCardPlayed(CardPlayedEvent event) {
		ZomisLog.info(this + " on card played " + cardsPlayedThisTurn);
		this.cardsPlayedThisTurn++;
		if (this.cardsPlayedThisTurn >= limit)
			event.getGame().nextPhase();
	}
	
	

}
