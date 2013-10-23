package test.net.zomis.cards;

import java.util.Arrays;

import junit.framework.Assert;
import net.zomis.cards.cwars2.CWars2AI;
import net.zomis.cards.cwars2.CWars2AI_InstantWin;
import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.cwars2.CWars2Res;
import net.zomis.cards.model.ai.CardAI;
import net.zomis.cards.util.ResourceMap;
import net.zomis.custommap.CustomFacade;

import org.junit.Test;

public class CWars2AIFight extends CardsTest<CWars2Game> {

	@Override
	protected void onBefore() {
		game = new CWars2Game();
		game.startGame();
	}
	
	@Test
	public void fight() {
		int wins[] = new int[game.getPlayers().size()];
		CardAI[] ais = new CardAI[]{ new CWars2AI_InstantWin(), new CWars2AI() };
		for (int i = 0; i < 100; i++) {
			onBefore();
			
			game.getPlayers().get(0).setAI(ais[0]);
			game.getPlayers().get(1).setAI(ais[1]);
			
			while (!game.isGameOver()) {
//				CustomFacade.getLog().i("Call AI: " + game.getCurrentPlayer());
				game.callPlayerAI();
			}
			
			wins[getWinIndex()]++;
		}
		CustomFacade.getLog().i("Wins " + Arrays.toString(wins));
	}

	private int getWinIndex() {
		int winner = -1;
		for (int i = 0; i < game.getPlayers().size(); i++) {
			ResourceMap res = game.getPlayers().get(i).getResources();
			Boolean winStatus = winStatus(res);
			if (winStatus != null) {
				int newWin = -1;
				if (winStatus == false)
					newWin = game.getPlayers().size() - 1 - i; // winner is the other player
				else newWin = i;
				
				if (winner >= 0 && winner != newWin)
					throw new IllegalStateException("Two winners");
				
				winner = newWin;
			}
		}
		Assert.assertNotSame(game.getPlayers().toString(), -1, winner);
//		CustomFacade.getLog().i("Winner is: " + game.getPlayers().get(winner));
		return winner;
	}

	private Boolean winStatus(ResourceMap res) {
		if (res.getResources(CWars2Res.CASTLE) <= 0)
			return false;
		if (res.getResources(CWars2Res.CASTLE) >= 100)
			return true;
		return null;
	}
	
}
