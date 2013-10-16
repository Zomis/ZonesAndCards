package net.zomis.cards.hearts;

import java.util.LinkedList;
import java.util.List;

import net.zomis.aiscores.ParamAndField;
import net.zomis.cards.classics.CardPlayer;
import net.zomis.cards.classics.ClassicGame;
import net.zomis.cards.model.AIHandler;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.InvalidStackAction;
import net.zomis.cards.model.ai.RandomAI;

public class HeartsHandler implements AIHandler {

	private final StackAction illegal = new InvalidStackAction();

	@Override
	public void move(CardGame game) {
		ParamAndField<Player, StackAction> result = new RandomAI(game).play();
		if (result != null)
			game.addAndProcessStackAction(result.getField());
	}

	@Override
	public StackAction click(Card card) {
		ClassicGame game = (ClassicGame) card.getGame();
		Player player = game.findPlayerWithHand(card.getCurrentZone());
		if (player == null)
			player = game.findPlayerWithBoard(card.getCurrentZone());
		
		if (card.getGame().getCurrentPlayer() == null) {
			// give phase
			return new HeartsGiveAction(card);
		}
		else {
			if (player != card.getGame().getCurrentPlayer())
				return illegal;
			return new HeartsPlayAction(card);
		}
	}

	@Override
	public List<StackAction> getAvailableActions(Player pl) {
		CardPlayer player = (CardPlayer) pl;
		List<StackAction> list = new LinkedList<StackAction>();
		CardPlayer currentPlayer = player.getGame().getCurrentPlayer();
		if (currentPlayer == null) {
			for (Card card : player.getHand().cardList()) {
				list.add(new HeartsGiveAction(card));
			}
			for (Card card : player.getBoard().cardList()) {
				list.add(new HeartsGiveAction(card));
			}
		}
		else if (player.getGame().getCurrentPlayer() == player) {
			for (Card card : player.getHand().cardList()) {
				list.add(new HeartsPlayAction(card));
			}
		}
		else {
			// Player is not current player and it's not the give cards phase: There's nothing you can do!
		}
		
		return list;
	}

}
