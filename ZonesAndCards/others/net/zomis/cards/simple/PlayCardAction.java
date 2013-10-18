package net.zomis.cards.simple;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.actions.ZoneMoveAction;
import net.zomis.cards.model.phases.GamePhase;
import net.zomis.cards.model.phases.IPlayerPhase;
import net.zomis.cards.simple.cardeffects.PublicStackAction;

public class PlayCardAction extends ZoneMoveAction {

	private Card card;

	private PublicStackAction myAction;
	
	public PlayCardAction(Card card) {
		super(card);
		this.card = card;
		SimpleGame game = (SimpleGame) card.getGame();
		this.setDestination(game.getPlayedCards());
		SimpleCard model = (SimpleCard) card.getModel();
		myAction = model.getAction().createAction(model, card, (SimplePlayer) game.getCurrentPlayer());
	}

	@Override
	public boolean isAllowed() {
		SimpleGame game = (SimpleGame) card.getCurrentZone().getGame();
		GamePhase phase = game.getActivePhase();
		if (card.getCurrentZone().equals(game.getPlayedCards()))
			return false;
		
		if (phase.toString().contains("Upkeep"))
			return false;
		
		if (phase instanceof IPlayerPhase) {
			Player player = ((IPlayerPhase)phase).getPlayer();
			return card.getCurrentZone().isKnown(player);
		}
		return false;
	}
	@Override
	protected void onPerform() {
		super.onPerform();
		myAction.onPerform();
	}
}
