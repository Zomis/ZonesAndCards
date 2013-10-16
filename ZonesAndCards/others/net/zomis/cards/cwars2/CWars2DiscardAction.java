package net.zomis.cards.cwars2;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.actions.ZoneMoveAction;

public class CWars2DiscardAction extends ZoneMoveAction {

	private CWars2Game	game;

	public CWars2DiscardAction(Card card) {
		super(card);
		this.game = (CWars2Game) card.getGame();
		this.setDestination(game.getDiscard());
	}

	@Override
	public boolean isAllowed() {
		if (!game.getCurrentPlayer().getHand().cardList().contains(getCard()))
			return false;
		
		return game.getDiscarded() < game.getDiscardsPerTurn();
	}
	
	@Override
	protected void perform() {
		super.perform();
		game.discarded();
		if (game.getDiscarded() == game.getDiscardsPerTurn())
			game.nextPhase();
	}
	
}