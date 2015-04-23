package net.zomis.cards.systems;

import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.events.card.CardPlayedEvent;
import net.zomis.cards.events.game.PhaseChangeEvent;
import net.zomis.cards.iface.GameSystem;
import net.zomis.custommap.view.ZomisLog;
import net.zomis.events.EventExecutorGWT;

/**
 * <p>Functionality for automatically ending phase once x cards has been played that turn. (ActionZone cards not included)</p>
 * <p>Listens for {@link CardPlayedEvent} and {@link PhaseChangeEvent}</p>
 */
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
		if (event.getGame().getActionZone().containsModel(event.getCard().getModel()))
			return;
		ZomisLog.info(this + " on card played " + cardsPlayedThisTurn);
		this.cardsPlayedThisTurn++;
		if (this.cardsPlayedThisTurn >= limit)
			event.getGame().nextPhase();
	}
	
}
