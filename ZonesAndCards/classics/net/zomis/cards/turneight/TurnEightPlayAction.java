package net.zomis.cards.turneight;

import net.zomis.cards.classics.CardPlayer;
import net.zomis.cards.classics.ClassicCard;
import net.zomis.cards.classics.ClassicCardZone;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.actions.ZoneMoveAction;

public class TurnEightPlayAction extends ZoneMoveAction {
	public TurnEightPlayAction(Card<ClassicCard> card) {
		super(card);
		TurnEightGame game = (TurnEightGame) card.getGame();
		this.setDestination(game.getDiscard());
	}
	@Override
	public boolean actionIsAllowed() {
		TurnEightGame game = (TurnEightGame) getCard().getGame();
		ClassicCardZone discard = game.getDiscard();
		if (discard.isEmpty())
			return false;
		ClassicCard topCard = (ClassicCard) discard.getBottomCard().getModel();
		ClassicCard myCard = getModel();
		
		if (!TurnEightController.finishAllowed(myCard, game.getCurrentPlayer())) {
			return false;
		}
		
		boolean bothSpecial = TurnEightController.isSpecial(topCard, game.getAceValue()) && TurnEightController.isSpecial(myCard, game.getAceValue());
//		boolean sameSuite = myCard.getSuite().equals(topCard.getSuite());
		boolean sameSuite = myCard.getSuite().equals(getGame().getCurrentSuite());
		boolean sameRank = myCard.getRank() == topCard.getRank();
		
//		CustomFacade.getLog().d("Is playable? " + topCard + " vs " + myCard + " : " + sameSuite + " " + sameRank);
		if (myCard.getRank() == TurnEightController.EIGHT) {
			sameSuite = true;
//			CustomFacade.getLog().d("Same Suite set to true because of eight.");
		}
		if (game.hasPlayed()) {
			sameSuite = false;
//			CustomFacade.getLog().d("Same Suite set to false because of player.");
		}
		
		boolean playable = (sameSuite || sameRank) && !bothSpecial;
		
		if (!playable)
			return false;
		@SuppressWarnings("unchecked")
		Card<ClassicCard> card = (Card<ClassicCard>) getCard();
		if (!game.getCurrentPlayer().getHand().contains(card)) {
//			CustomFacade.getLog().i("Current player is: " + game.getCurrentPlayer());
//			CustomFacade.getLog().i(myCard + " card not in list: " + game.getCurrentPlayer().getHand().cardList());
			return false;
		}
		return true;
	}
	public TurnEightGame getGame() {
		return (TurnEightGame) this.getCard().getGame();
	}
	@Override
	protected void onPerform() {
		super.onPerform();
		getGame().setCurrentSuite(getModel().getSuite());
		if (getModel().getRank() == TurnEightController.EIGHT) {
			// Välj en färg. GamePhase eller något annat? Kan jämföras med "target color" för diverse andra kortspel
			getGame().setCurrentSuite(getGame().getPlayerChoice());
		}
		if (getModel().getRank() == getGame().getAceValue()) {
			Player currentPlayer = this.getGame().getCurrentPlayer();
//			CustomFacade.getLog().i("All others pick up a card. Everyone except " + currentPlayer);
			for (Player player : currentPlayer.getOpponents()) {
				CardPlayer pl2 = (CardPlayer) player;
				if (!pl2.getHand().isEmpty())
					this.getGame().playerForceDraw((CardPlayer) player);
			}
		}
		TurnEightGame agame = (TurnEightGame) this.getCard().getGame();
//		CustomFacade.getLog().i("Set last player to " + getPlayer());
		agame.setHasPlayed(getModel());
	}
	public ClassicCard getModel() {
		return (ClassicCard) getCard().getModel();
	}
}
