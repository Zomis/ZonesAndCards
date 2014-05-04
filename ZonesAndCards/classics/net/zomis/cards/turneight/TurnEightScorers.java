package net.zomis.cards.turneight;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.FScorer;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.aiscores.scorers.Scorers;
import net.zomis.cards.classics.CardPlayer;
import net.zomis.cards.classics.ClassicCard;
import net.zomis.cards.classics.Suite;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.NextTurnAction;
import net.zomis.cards.model.ai.IsActionClass;

public enum TurnEightScorers {
	; // it's an ENUM!
	
	public static FScorer<Player, Card<?>> IsDrawCard() {
		return new IsActionClass(DrawCardAction.class);
	}
	public static FScorer<Player, Card<?>> PlayCardScorer() {
		return new IsActionClass(TurnEightPlayAction.class);
	}
	
	public static FScorer<Player, Card<?>> IsNextTurn() {
		return new IsActionClass(NextTurnAction.class);
	}
	public static FScorer<Player, Card<?>> IsEight() {
		return Scorers.multiplication(PlayCardScorer(), new AbstractScorer<Player, Card<?>>() {
			@Override
			public double getScoreFor(Card<?> field, ScoreParameters<Player> scores) {
				ClassicCard cast = (ClassicCard) field.getModel();
				return cast.getRank() == TurnEightController.EIGHT ? 1 : 0;
			}
		});
	}
	public static class NeedSuiteChange extends AbstractScorer<Player, Card<?>> {
		@Override
		public double getScoreFor(Card<?> field, ScoreParameters<Player> scores) {
			StackAction action = field.clickAction();
			if (!(action instanceof SetColorAction))
				return 0;
			ClassicCard card = (ClassicCard) field.getModel();
			TurnEightGame game = (TurnEightGame) field.getGame();
			Suite suite = getPreferredSuite((CardPlayer) scores.getParameters());
			return card.getSuite() == suite && game.getCurrentSuite() != suite ? 1 : 0;
		}
		
		private Suite getPreferredSuite(CardPlayer player) {
			int[] suiteCount = new int[Suite.values().length];
			for (Card<ClassicCard> card : player.getHand()) {
				ClassicCard model = (ClassicCard) card.getModel();
				suiteCount[model.getSuite().ordinal()] += getIncreaseBy(model, player.getGame().getAceValue());
			}
			
			int max = 0;
			int maxIndex = 0;
			for (int i = 0; i < suiteCount.length; i++) {
				if (suiteCount[i] > max) {
					max = suiteCount[i];
					maxIndex = i;
				}
			}
			return Suite.values()[maxIndex];
		}
		private int getIncreaseBy(ClassicCard model, int aceValue) {
			if (model.getRank() == TurnEightController.EIGHT)
				return 0;
			else if (model.getRank() == aceValue)
				return 2;
			else return 1;
		}
		@Override
		public boolean workWith(ScoreParameters<Player> scores) {
			Suite suite = getPreferredSuite((CardPlayer) scores.getParameters());
			TurnEightGame game = (TurnEightGame) scores.getParameters().getGame();
			if (game.getCurrentSuite() == suite)
				return false;
			if (game.getPlayerChoice() == suite)
				return false;
			
			for (Card<ClassicCard> card : ((CardPlayer) scores.getParameters()).getHand()) {
				ClassicCard cm = (ClassicCard) card.getModel();
				if (cm.getRank() == TurnEightController.EIGHT)
					return true;
			}
			return false;
		}
	}
	public static FScorer<Player, Card<?>> IsAce() {
		return Scorers.multiplication(PlayCardScorer(), new AbstractScorer<Player, Card<?>>() {
			@Override
			public double getScoreFor(Card<?> field, ScoreParameters<Player> scores) {
				ClassicCard card = (ClassicCard) field.getModel();
				TurnEightGame game = (TurnEightGame) field.getGame();
				return card.getRank() == game.getAceValue() ? 1 : 0;
			}
		});
	}

}
