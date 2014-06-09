package net.zomis.cards.hearts;

import java.util.ArrayList;
import java.util.List;

import net.zomis.cards.classics.CardPlayer;
import net.zomis.cards.classics.ClassicCard;
import net.zomis.cards.classics.ClassicCardZone;
import net.zomis.cards.model.ActionHandler;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.InvalidStackAction;
import net.zomis.custommap.view.ZomisLog;

public class HeartsHandler implements ActionHandler {

	private final StackAction illegal = new InvalidStackAction();

	@Override
	public StackAction click(Card<?> card) {
		ClassicCardZone zone = (ClassicCardZone) card.getCurrentZone();
		@SuppressWarnings("unchecked")
		Card<ClassicCard> cardM = (Card<ClassicCard>) card;
		Player player = zone.getOwner();
		if (player == null)
			player = zone.getOwner();
		
		if (card.getGame().getCurrentPlayer() == null) {
			// give phase
			return new HeartsGiveAction(cardM);
		}
		else {
			if (player != card.getGame().getCurrentPlayer())
				return illegal;
			return new HeartsPlayAction(cardM);
		}
	}

	@Override
	public List<Card<?>> getUseableCards(CardGame<? extends Player, ? extends CardModel> game, Player pl) {
		List<Card<?>> cards = new ArrayList<Card<?>>();
		
		CardPlayer player = (CardPlayer) pl;
//		HeartsGame ga = (HeartsGame) game;
//		ZomisLog.info("Cards " + ga.getGiveDirection() + ga.getActivePhase());
//		ZomisLog.info("Cards " + game + pl + " hand " + player.getHand().cardList());
		if (pl == null) {
			for (Player cplayer : game.getPlayers())
				cards.addAll(getUseableCards(game, cplayer));
			return cards;
		}
		CardPlayer currentPlayer = player.getGame().getCurrentPlayer();
		if (currentPlayer == null) {
//			ZomisLog.info("Curr player null");
			cards.addAll(player.getHand().cardList());
			cards.addAll(player.getBoard().cardList());
		}
		else if (player.getGame().getCurrentPlayer() == player) {
//			ZomisLog.info("Curr player player");
			cards.addAll(player.getHand().cardList());
		}
		else {
			ZomisLog.info("I can do nothing! It is not my turn!");
			// Player is not current player and it's not the give cards phase: There's nothing you can do!
		}
		if (cards.isEmpty())
			ZomisLog.info("Player " + player + " with hand " + player.getHand() + " can do nothing");

		return cards;
	}

}
