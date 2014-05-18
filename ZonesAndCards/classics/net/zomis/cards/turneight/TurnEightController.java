package net.zomis.cards.turneight;

import java.util.ArrayList;
import java.util.List;

import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.cards.classics.CardPlayer;
import net.zomis.cards.classics.ClassicCard;
import net.zomis.cards.classics.ClassicCardZone;
import net.zomis.cards.classics.ClassicGame;
import net.zomis.cards.model.ActionHandler;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.NextTurnAction;
import net.zomis.cards.model.ai.CardAI;
import net.zomis.cards.model.ai.IsActionClass;


public class TurnEightController implements ActionHandler {
	static final int EIGHT = 8;

	public static class TurnEightAISkilled extends CardAI {
		public TurnEightAISkilled() {
			ScoreConfigFactory<Player, Card<?>> config = new ScoreConfigFactory<Player, Card<?>>();
			config.withScorer(new TurnEightScorers.NeedSuiteChange(), 10);
			config.withScorer(TurnEightScorers.IsAce(), 1);
			config.withScorer(new IsActionClass(TurnEightPlayAction.class), 1);
			config.withScorer(TurnEightScorers.IsNextTurn(), 0.5);
			config.withScorer(TurnEightScorers.IsEight(), -0.2); // eight also gets points from the SubclassFixedScorer
			config.withScorer(TurnEightScorers.IsDrawCard(), 0.1);
			this.setConfig(config.build());
		}
		
	}
	
	public static boolean isSpecial(ClassicCard card, int aceValue) {
		return card.getRank() == EIGHT || card.getRank() == aceValue;
	}

	public static boolean finishAllowed(ClassicCard myCard, CardPlayer player) {
		ClassicGame game = player.getGame();
		if (!isSpecial(myCard, game.getAceValue())) {
			return true;
		}
		
		ClassicCardZone list = player.getHand();
		if (list.isEmpty())
			return true;
		ClassicCard card1 = (ClassicCard) list.getTopCard().getModel();
		ClassicCard card2 = (ClassicCard) list.getBottomCard().getModel();
		int ace = game.getAceValue();
		
		if (player.getHand().size() <= 2) {
			boolean hasAce = (card1.getRank() == ace) || (card2.getRank() == ace);
			if (hasAce)
				return false;
			
			boolean card1_special = isSpecial(card1, game.getAceValue());
			boolean card2_special = isSpecial(card2, game.getAceValue());
			return !(card1_special && card2_special);
		}
		return true;
	}

	private static boolean isDrawCard(Card<?> card) {
		return getGame(card).drawCard == card.getModel();
	}

	private static boolean isNextTurn(Card<?> card) {
		return getGame(card).nextTurn == card.getModel();
	}
	private static TurnEightGame getGame(Card<?> card) {
		return (TurnEightGame) card.getGame();
	}

	@Override
	public StackAction click(Card<?> card) {
		TurnEightGame game = (TurnEightGame) card.getGame();
		@SuppressWarnings("unchecked")
		Card<ClassicCard> realCard = (Card<ClassicCard>) card;
		if (card.getModel() instanceof SuiteModel) {
			return new SetColorAction(game, ((SuiteModel)card.getModel()).getSuite());
		}
		else if (isNextTurn(card)) {
			return new NextTurnAction(game);
		}
		else if (isDrawCard(card)) {
			return new DrawCardAction(game);
		}
		else {
			// It's ok if the action is not allowed, because that is checked when processing stack.
			return new TurnEightPlayAction(realCard);
		}
	}

	@Override
	public List<Card<?>> getUseableCards(CardGame<? extends Player, ? extends CardModel> game, Player player) {
		List<Card<?>> cards = new ArrayList<Card<?>>();
		CardPlayer pl = (CardPlayer) player;
		cards.addAll(pl.getHand().cardList());
		return cards;
	}

}
