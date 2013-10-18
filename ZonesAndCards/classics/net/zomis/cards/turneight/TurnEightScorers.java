package net.zomis.cards.turneight;

import net.zomis.aiscores.ScoreParameters;
import net.zomis.aiscores.scorers.SubclassFixedScorer;
import net.zomis.aiscores.scorers.SubclassScorer;
import net.zomis.cards.classics.CardPlayer;
import net.zomis.cards.classics.ClassicCard;
import net.zomis.cards.classics.Suite;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.NextTurnAction;

public enum TurnEightScorers {
;
	public static class IsDrawCard extends SubclassFixedScorer<Player, StackAction, DrawCardAction> {
		public IsDrawCard() {
			super(DrawCardAction.class);
		}
	}
	public static abstract class PlayCardScorer extends SubclassScorer<Player, StackAction, TurnEightPlayAction> {
		public PlayCardScorer() {
			super(TurnEightPlayAction.class);
		}
	}
	
	public static class IsNextTurn extends SubclassFixedScorer<Player, StackAction, NextTurnAction> {
		public IsNextTurn() {
			super(NextTurnAction.class);
		}
	}
	public static class IsEight extends PlayCardScorer {
		@Override
		public double scoreSubclass(TurnEightPlayAction cast, ScoreParameters<Player> scores) {
			return cast.getModel().getRank() == TurnEightController.EIGHT ? 1 : 0;
		}
	}
	public static class NeedSuiteChange extends SubclassScorer<Player, StackAction, SetColorAction> {
		public NeedSuiteChange() {
			super(SetColorAction.class);
		}
		@Override
		public double scoreSubclass(SetColorAction cast, ScoreParameters<Player> scores) {
			Suite suite = getPreferredSuite((CardPlayer) scores.getParameters());
			return (cast.getSuite() == suite && cast.getGame().getCurrentSuite() != suite ? 1 : 0);
		}
		private Suite getPreferredSuite(CardPlayer player) {
			int[] suiteCount = new int[Suite.values().length];
			for (Card card : player.getHand().cardList()) {
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
			
			for (Card card : ((CardPlayer) scores.getParameters()).getHand().cardList()) {
				ClassicCard cm = (ClassicCard) card.getModel();
				if (cm.getRank() == TurnEightController.EIGHT)
					return true;
			}
			return false;
		}
	}
	public static class IsAce extends PlayCardScorer {
		@Override
		public double scoreSubclass(TurnEightPlayAction cast, ScoreParameters<Player> scores) {
			return cast.getModel().getRank() == cast.getGame().getAceValue() ? 1 : 0;
		}
	}

}
