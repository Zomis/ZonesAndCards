package test.net.zomis.cards.turneight;

import junit.framework.Assert;
import net.zomis.cards.classics.CardPlayer;
import net.zomis.cards.model.Player;
import net.zomis.cards.turneight.TurnEightGame;
import net.zomis.custommap.CustomFacade;
import net.zomis.custommap.model.CastedIterator;
import net.zomis.custommap.view.SwingLog;

import org.junit.Test;

public class TurnEightTest {

	
	@Test(timeout = 50000)
	public void testGame() {
		new CustomFacade(new SwingLog());
		
		
		TurnEightGame game = new TurnEightGame();
		game.addPlayer("#AI_Idiot");
		game.addPlayer("#AI_TurnEight");
		game.addPlayer("Bubu");
		game.startGame();
		
		int it = 0;
		do {
			CardPlayer player = game.getCurrentPlayer();
			while (game.getCurrentPlayer() == player) {
//				CustomFacade.getLog().i("Current player: " + player + ": " + player.getHand());
				game.getAIHandler().move(game);
				++it;
			}
			if (player.getHand().cardList().isEmpty())
				CustomFacade.getLog().i("Player " + player + " is out after " + it);
//			CustomFacade.getLog().d("Played? " + game.hasPlayed()); 
//			CustomFacade.getLog().d("Drawn: " + game.getDrawnCards()); 
//			CustomFacade.getLog().d("Hand " + game.getCurrentPlayer().getHand());
		}
		while (!game.getCurrentPlayer().getHand().cardList().isEmpty());
		
		for (CardPlayer player : new CastedIterator<Player, CardPlayer>(game.getPlayers())) {
			Assert.assertTrue("Hand for " + player + " is " + player.getHand().cardList(), player.getHand().cardList().isEmpty());
		}
		
		
	}
	
}
