package test.net.zomis.cards;

import static org.junit.Assert.*;
import junit.framework.Assert;
import net.zomis.cards.classics.CardPlayer;
import net.zomis.cards.classics.ClassicCard;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.ai.RandomAI;
import net.zomis.cards.turneight.TurnEightController;
import net.zomis.cards.turneight.TurnEightGame;
import net.zomis.cards.turneight.TurnEightScorers;
import net.zomis.custommap.model.CastedIterator;

import org.junit.Test;

public class TurnEightTest extends CardsTest<TurnEightGame> {
	@Test
	public void reshuffleTest() {
		game = new TurnEightGame();
		game.addPlayer("BUBU");
		game.addPlayer("Bakkit");
		game.setRandomSeed(9); // Hearts-14 is first
		game.startGame();
		assertFalse(TurnEightController.isSpecial((ClassicCard) game.getDiscard().getTopCard().getModel(), game.getAceConfig().getAceValue()));
		
	}
	@Test(timeout = 50000)
	public void testGame() {
		assertEquals(0, TurnEightScorers.values().length);
		
//		int it = 0;
		
		for (int i = 0; i < 42; i++) {
			game.callPlayerAI();
			if (game.getCurrentPlayer() != game.getPlayers().iterator().next())
				break;
		}
		Assert.assertNotSame("Not switching current player!", game.getCurrentPlayer(), game.getPlayers().get(0));
		
		do {
			CardPlayer player = game.getCurrentPlayer();
			while (game.getCurrentPlayer() == player) {
				game.callPlayerAI();
//				++it;
			}
//			if (player.getHand().cardList().isEmpty())
//				CustomFacade.getLog().i("Player " + player + " is out after " + it);
		}
		while (!game.getCurrentPlayer().getHand().cardList().isEmpty());
		
		for (CardPlayer player : new CastedIterator<Player, CardPlayer>(game.getPlayers())) {
			Assert.assertTrue("Hand for " + player + " is " + player.getHand().cardList(), player.getHand().cardList().isEmpty());
		}
		Assert.assertTrue(game.isGameOver());
	}

	@Override
	protected TurnEightGame newTestObject() {
		TurnEightGame game = new TurnEightGame();
		game.setRandomSeed(42);
		game.addPlayer("#AI_Idiot", new RandomAI(game.getRandom()));
		game.addPlayer("#AI_TurnEight", new TurnEightController.TurnEightAISkilled());
		game.addPlayer("Bubu", new TurnEightController.TurnEightAISkilled());
		game.startGame();
		return game;
	}
	
}
