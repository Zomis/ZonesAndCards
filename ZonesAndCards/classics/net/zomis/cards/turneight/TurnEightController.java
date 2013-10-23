package net.zomis.cards.turneight;

import java.util.LinkedList;
import java.util.List;

import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.scorers.SubclassFixedScorer;
import net.zomis.cards.classics.CardPlayer;
import net.zomis.cards.classics.ClassicCard;
import net.zomis.cards.classics.ClassicGame;
import net.zomis.cards.classics.Suite;
import net.zomis.cards.model.ActionHandler;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.NextTurnAction;
import net.zomis.cards.model.ai.CardAI;


public class TurnEightController implements ActionHandler {
	static final int EIGHT = 8;

	@Override
	public List<StackAction> getAvailableActions(Player player) {
		List<StackAction> result = new LinkedList<StackAction>();
		for (Card card : ((CardPlayer)player).getHand().cardList()) {
			TurnEightPlayAction action = new TurnEightPlayAction(card);
			if (action.actionIsAllowed()) {
				result.add(action);
			}
		}
		TurnEightGame game = (TurnEightGame) player.getGame();
		result.add(new NextTurnAction(game));
		result.add(new DrawCardAction(game));
		
		for (Suite suite : Suite.values()) {
			if (!suite.isWildcard())
				result.add(new SetColorAction(game, suite));
		}
		return result;
	}
	
	public static class TurnEightAISkilled extends CardAI {
		public TurnEightAISkilled() {
			ScoreConfigFactory<Player, StackAction> config = new ScoreConfigFactory<Player, StackAction>();
			config.withScorer(new TurnEightScorers.NeedSuiteChange(), 10);
			config.withScorer(new TurnEightScorers.IsAce(), 1);
			config.withScorer(new SubclassFixedScorer<Player, StackAction, TurnEightPlayAction>(TurnEightPlayAction.class), 1);
			config.withScorer(new TurnEightScorers.IsNextTurn(), 0.5);
			config.withScorer(new TurnEightScorers.IsEight(), -0.2); // eight also gets points from the SubclassFixedScorer
			config.withScorer(new TurnEightScorers.IsDrawCard(), 0.1);
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
		
		LinkedList<Card> list = player.getHand().cardList();
		if (list.isEmpty())
			return true;
		ClassicCard card1 = (ClassicCard) list.getFirst().getModel();
		ClassicCard card2 = (ClassicCard) list.getLast().getModel();
		int ace = game.getAceValue();
		
		if (player.getHand().cardList().size() <= 2) {
			boolean hasAce = (card1.getRank() == ace) || (card2.getRank() == ace);
			if (hasAce)
				return false;
			
			boolean card1_special = isSpecial(card1, game.getAceValue());
			boolean card2_special = isSpecial(card2, game.getAceValue());
			return !(card1_special && card2_special);
		}
		return true;
	}

	@Override
	public StackAction click(Card card) {
		TurnEightGame game = (TurnEightGame) card.getGame();
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
			return new TurnEightPlayAction(card);
		}
	}

	private static boolean isDrawCard(Card card) {
		return getGame(card).drawCard == card.getModel();
	}

	private static boolean isNextTurn(Card card) {
		return getGame(card).nextTurn == card.getModel();
	}
	private static TurnEightGame getGame(Card card) {
		return (TurnEightGame) card.getGame();
	}
	
}
