package net.zomis.cards.hearts;

import java.util.ArrayList;
import java.util.List;

import net.zomis.cards.classics.ClassicCard;
import net.zomis.cards.classics.ClassicCardZone;
import net.zomis.cards.classics.ClassicGame;
import net.zomis.cards.model.ActionHandler;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.InvalidStackAction;

public class HeartsHandler implements ActionHandler {

	private final StackAction illegal = new InvalidStackAction();

	@Override
	public StackAction click(Card<?> card) {
		ClassicGame game = (ClassicGame) card.getGame();
		ClassicCardZone zone = (ClassicCardZone) card.getCurrentZone();
		@SuppressWarnings("unchecked")
		Card<ClassicCard> cardM = (Card<ClassicCard>) card;
		Player player = game.findPlayerWithHand(zone);
		if (player == null)
			player = game.findPlayerWithBoard(zone);
		
		
		
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

//	@Override
//	public <E extends CardGame<Player, CardModel>> List<StackAction> getAvailableActions(E cardGame, Player pl) {
//		CardPlayer player = (CardPlayer) pl;
//		List<StackAction> list = new LinkedList<StackAction>();
//		if (pl == null) {
//			for (Player cplayer : cardGame.getPlayers())
//				list.addAll(getAvailableActions(cardGame, cplayer));
//			return list;
//		}
//		CardPlayer currentPlayer = player.getGame().getCurrentPlayer();
//		if (currentPlayer == null) {
//			for (Card<ClassicCard> card : player.getHand()) {
//				list.add(new HeartsGiveAction(card));
//			}
//			for (Card<ClassicCard> card : player.getBoard()) {
//				list.add(new HeartsGiveAction(card));
//			}
//		}
//		else if (player.getGame().getCurrentPlayer() == player) {
//			for (Card<ClassicCard> card : player.getHand()) {
//				list.add(new HeartsPlayAction(card));
//			}
//		}
//		else {
//			CustomFacade.getLog().i("I can do nothing! It is not my turn!");
//			// Player is not current player and it's not the give cards phase: There's nothing you can do!
//		}
//		if (list.isEmpty())
//			CustomFacade.getLog().i("Player " + player + " with hand " + player.getHand() + " can do " + list);
//		return list;
//	}

	@Override
	public List<Card<?>> getUseableCards(CardGame<? extends Player, ? extends CardModel> game, Player player) {
		List<Card<?>> cards = new ArrayList<Card<?>>();
		
		
		return cards;
	}

}
