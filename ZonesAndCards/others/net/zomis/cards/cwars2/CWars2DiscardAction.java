package net.zomis.cards.cwars2;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.actions.ZoneMoveAction;

public class CWars2DiscardAction extends ZoneMoveAction {

	private CWars2Game	game;

	public CWars2DiscardAction(Card<CWars2Card> card) {
		super(card);
		this.game = (CWars2Game) card.getGame();
		this.setDestination(game.getCurrentPlayer().getDiscard());
	}

	@Override
	public boolean actionIsAllowed() {
		if (!game.getCurrentPlayer().getHand().cardList().contains(getCard()))
			return false;
		
		return game.getDiscarded() < game.getDiscardsPerTurn(game.getCurrentPlayer());
	}
	
	@Override
	protected void onPerform() {
		super.onPerform();
		game.discarded();
		if (game.getDiscarded() == game.getDiscardsPerTurn(game.getCurrentPlayer()))
			game.nextPhase();
	}
	
	@Override
	public String toString() {
		return "Discard " + this.getCard().getModel().getName();
	}
	
}
