package net.zomis.cards.hearts;

import java.util.LinkedList;

import net.zomis.ZomisList;
import net.zomis.cards.classics.CardPlayer;
import net.zomis.cards.classics.ClassicCard;
import net.zomis.cards.classics.ClassicCardFilter;
import net.zomis.cards.classics.Suite;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.actions.ZoneMoveAction;

public class HeartsPlayAction extends ZoneMoveAction {

	private ClassicCard cardModel;

	public HeartsPlayAction(Card card) {
		super(card);
		this.cardModel = (ClassicCard) card.getModel();
		this.setSendToTop();
		this.setDestination(getGame().getPile());
	}
	
	@Override
	public boolean isAllowed() {
		CardPlayer player = getGame().findPlayerWithHand(getCard().getCurrentZone());
		if (player != getGame().getCurrentPlayer())
			return false;
		
		if (getGame().getPile().isEmpty()) {
			if (player.getHand().size() == ZomisList.filter2(player.getHand().cardList(), new ClassicCardFilter(Suite.HEARTS)).size()) {
				return true; // Have only HEARTS left, then it doesn't matter.
			}
			if (player.getHand().size() == 13) {
				// First hand, must play 2 of clubs.
				return cardModel.getSuite() == Suite.CLUBS && cardModel.getRank() == 2;
			}
			// Can only play hearts if hearts has been broken (played before)
			return cardModel.getSuite() != Suite.HEARTS || getGame().isHeartsBroken();
		}
		
		ClassicCard bottom = (ClassicCard) getGame().getPile().getBottomCard().getModel();
		LinkedList<Card> list = ZomisList.filter2(player.getHand().cardList(), new ClassicCardFilter(bottom.getSuite()));
		
		if (list.isEmpty()) {
			// Don't have a card of the requested suite, can play any card.
			if (player.getHand().size() == ZomisList.filter2(player.getHand().cardList(), new ClassicCardFilter(Suite.HEARTS)).size()) {
				return true; // Have only HEARTS left, then it doesn't matter.
			}
			if (player.getHand().size() == 13) {
				// Not allowed to play point cards on the first round
				return !isPoints();
			}
			return true;
		}
		else {
			return cardModel.getSuite().equals(bottom.getSuite());
		}
	}
	
	private boolean isPoints() {
		return this.cardModel.getSuite() == Suite.HEARTS || isQueenOfSpades();
	}

	private boolean isQueenOfSpades() {
		Suite.valueOf("");
		return this.cardModel.getSuite() == Suite.SPADES && this.cardModel.getRank() == 12;
	}

	@Override
	protected void perform() {
		super.perform();
		getGame().nextPhase();
	}

	private HeartsGame getGame() {
		return (HeartsGame) this.getCard().getGame();
	}
}
