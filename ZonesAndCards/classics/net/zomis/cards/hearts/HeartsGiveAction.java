package net.zomis.cards.hearts;

import net.zomis.cards.classics.CardPlayer;
import net.zomis.cards.classics.ClassicCardZone;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.actions.ZoneMoveAction;

public class HeartsGiveAction extends ZoneMoveAction {

	public static final int GIVE_COUNT = 3;

	public HeartsGiveAction(Card card) {
		super(card);
		setDestination(isGiven() ? getPlayer().getHand() : getPlayer().getBoard());
	}
	
	@Override
	public boolean isAllowed() {
		return isGiven() ? true : getPlayer().getBoard().size() < GIVE_COUNT;
	}
	private CardPlayer getPlayer() {
		CardPlayer pl = this.getGame().findPlayerWithZone(getCard().getCurrentZone());
		return pl;
	}
	private HeartsGame getGame() {
		return (HeartsGame) this.getCard().getGame();
	}

	private boolean isGiven() {
		return this.getCard().getCurrentZone().size() <= GIVE_COUNT;
	}

	@Override
	protected void onPerform() {
		getGame().sort((ClassicCardZone) this.getCard().getCurrentZone());
		super.onPerform();
		getGame().sort((ClassicCardZone) this.getDestination());
	}
	
}
