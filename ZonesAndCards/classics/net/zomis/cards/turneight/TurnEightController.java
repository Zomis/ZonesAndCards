package net.zomis.cards.turneight;

import java.util.LinkedList;
import java.util.List;

import net.zomis.aiscores.ParamAndField;
import net.zomis.aiscores.ScoreConfig;
import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.scorers.RandomScorer;
import net.zomis.aiscores.scorers.SubclassFixedScorer;
import net.zomis.cards.classics.CardPlayer;
import net.zomis.cards.classics.ClassicCard;
import net.zomis.cards.classics.ClassicGame;
import net.zomis.cards.classics.Suite;
import net.zomis.cards.model.AIHandler;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.NextTurnAction;
import net.zomis.cards.model.ai.CardAI;


public class TurnEightController implements AIHandler {
	static final int EIGHT = 8;

	@Override
	public List<StackAction> getAvailableActions(Player player) {
		List<StackAction> result = new LinkedList<>();
		for (Card card : ((CardPlayer)player).getHand().cardList()) {
			TurnEightPlayAction action = new TurnEightPlayAction(card);
			if (action.isAllowed()) {
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

	public static class TurnEightAIRandom extends CardAI {

		public TurnEightAIRandom(CardGame game) {
			super(game);
			ScoreConfig<Player, StackAction> config = new ScoreConfigFactory<Player, StackAction>().withScorer(new RandomScorer<Player, StackAction>()).build();
			this.setConfig(config);
		}
		
	}
	
	public static class TurnEightAISkilled extends CardAI {

		public TurnEightAISkilled(CardGame game) {
			super(game);
			ScoreConfigFactory<Player, StackAction> config = new ScoreConfigFactory<Player, StackAction>();
			config.withScorer(new TurnEightScorers.NeedSuiteChange(), 10);
			config.withScorer(new TurnEightScorers.IsAce(), 1);
			config.withScorer(new SubclassFixedScorer<Player, StackAction, TurnEightPlayAction>(TurnEightPlayAction.class), 1);
			config.withScorer(new TurnEightScorers.IsNextTurn(), 0.5);
			config.withScorer(new TurnEightScorers.IsEight(), -0.2);
			config.withScorer(new TurnEightScorers.IsDrawCard(), 0.1);
			this.setConfig(config.build());
		}
		
	}
	
	@Override
	public void move(CardGame game) {
		CardAI ai;
//		if (game.getCurrentPlayer().getName().equals("BUBU")) {
//			ai = new TurnEightAIRandom(game);
//		}
//		else 
			ai = new TurnEightAISkilled(game);
		
		ParamAndField<Player, StackAction> action = ai.play();
		if (action == null)
			throw new IllegalStateException("No possible moves.");
		StackAction field = action.getField();
		game.addAndProcessStackAction(field);
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
			return new SetColorAction(game, (SuiteModel)card.getModel());
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
		return getGame(card).getDrawCardModel() == card.getModel();
	}

	private static boolean isNextTurn(Card card) {
		return getGame(card).getNextTurnModel() == card.getModel();
	}
	private static TurnEightGame getGame(Card card) {
		return (TurnEightGame) card.getGame();
	}
	
}
