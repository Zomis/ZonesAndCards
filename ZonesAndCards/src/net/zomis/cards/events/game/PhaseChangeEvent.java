package net.zomis.cards.events.game;

import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.phases.GamePhase;
import net.zomis.events.IEvent;

public class PhaseChangeEvent implements IEvent {

	private final CardGame	game;
	private final GamePhase	from;
	private final GamePhase	to;

	public PhaseChangeEvent(CardGame game, GamePhase from, GamePhase to) {
		this.game = game;
		this.from = from;
		this.to = to;
	}
	
	public GamePhase getFrom() {
		return from;
	}
	public CardGame getGame() {
		return game;
	}
	public GamePhase getTo() {
		return to;
	}
	@Override
	public String toString() {
		return game + " phase " + getFrom() + " --> " + getTo();
	}
	
}
