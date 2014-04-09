package net.zomis.cards.hearts;

import java.util.List;

import net.zomis.cards.classics.CardPlayer;
import net.zomis.cards.classics.ClassicCard;
import net.zomis.cards.classics.ClassicCardFilter;
import net.zomis.cards.classics.ClassicCardZone;
import net.zomis.cards.classics.Suite;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.actions.ZoneMoveAction;
import net.zomis.custommap.CustomFacade;
import net.zomis.utils.ZomisList;

public class HeartsPlayAction extends ZoneMoveAction {

	private ClassicCard cardModel;

	public HeartsPlayAction(Card<ClassicCard> card) {
		super(card);
		this.cardModel = (ClassicCard) card.getModel();
		this.setSendToTop();
		this.setDestination(getGame().getPile());
	}
	
	@Override
	public boolean actionIsAllowed() {
		CardPlayer player = getGame().findPlayerWithHand((ClassicCardZone) getCard().getCurrentZone());
		if (player == null && getGame().getCurrentPlayer() != null) {
			return setErrorMessage("Card is not in a hand. Current player is " + getGame().getCurrentPlayer());
		}
		
		if (player != getGame().getCurrentPlayer()) {
			return setErrorMessage("Player is not current player " + this);
		}
		
		if (getGame().getPile().isEmpty()) {
			return pileEmptyAllowed(player);
		}
		
		ClassicCard bottom = (ClassicCard) getGame().getPile().getBottomCard().getModel();
		List<Card<ClassicCard>> list = ZomisList.getAll(player.getHand(), new ClassicCardFilter(bottom.getSuite()));
		
		if (list.isEmpty()) {
			return noMatchingSuiteAllowed(player);
		}
		else {
			return cardModel.getSuite().equals(bottom.getSuite());
		}
	}
	
	private boolean noMatchingSuiteAllowed(CardPlayer player) {
		// Don't have a card of the requested suite, can play any card.
		if (player.getHand().size() == ZomisList.getAll(player.getHand(), new ClassicCardFilter(Suite.HEARTS)).size()) {
			return true; // Have only HEARTS left, then it doesn't matter.
		}
		if (player.getHand().size() == 13) {
			// Not allowed to play point cards on the first round
			CustomFacade.getLog().i("First hand, is point card? " + cardModel + isPoints());
			return !isPoints();
		}
		return true;
	}

	private boolean pileEmptyAllowed(CardPlayer player) {
		if (player.getHand().size() == ZomisList.getAll(player.getHand(), new ClassicCardFilter(Suite.HEARTS)).size()) {
			return setOKMessage("Only hearts left");
		}
		if (player.getHand().size() == HeartsGame.RANKS_PER_SUITE) {
			return setMixedMessage(cardModel.getSuite() == Suite.CLUBS && cardModel.getRank() == 2, "First hand, must be 2 of clubs");
		}
		// Can only play hearts if hearts has been broken (played before)
		return cardModel.getSuite() != Suite.HEARTS || getGame().isHeartsBroken();
	}

	private boolean isPoints() {
		return this.cardModel.getSuite() == Suite.HEARTS || isQueenOfSpades();
	}

	private boolean isQueenOfSpades() {
		return this.cardModel.getSuite() == Suite.SPADES && this.cardModel.getRank() == ClassicCard.RANK_QUEEN;
	}

	@Override
	protected void onPerform() {
		super.onPerform();
		getGame().nextPhase();
	}

	private HeartsGame getGame() {
		return (HeartsGame) this.getCard().getGame();
	}
}
