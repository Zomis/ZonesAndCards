package net.zomis.cards.classic;

import static org.junit.Assert.*;
import net.zomis.cards.CardsTest;
import net.zomis.cards.ai.CGController;
import net.zomis.cards.classics.CardPlayer;
import net.zomis.cards.classics.ClassicCard;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.ai.CardAI_Random;
import net.zomis.cards.turneight.TurnEightController;
import net.zomis.cards.turneight.TurnEightGame;
import net.zomis.cards.turneight.TurnEightScorers;
import net.zomis.custommap.CustomFacade;

import org.junit.Test;

public class TurnEightTest extends CardsTest<TurnEightGame> {
	private CGController	controller;

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
		
		int it = 0;
		
		for (int i = 0; i < 42; i++) {
			System.out.println(i);
			StackAction act = controller.play();
			assertNotNull(act);
			if (game.getCurrentPlayer() != game.getPlayers().iterator().next())
				break;
		}
		assertNotSame("Not switching current player!", game.getCurrentPlayer(), game.getPlayers().get(0));
		
		do {
			CardPlayer player = game.getCurrentPlayer();
			while (game.getCurrentPlayer() == player) {
				StackAction act = controller.play();
				assertNotNull(act);
				++it;
			}
			if (player.getHand().cardList().isEmpty())
				CustomFacade.getLog().i("Player " + player + " is out after " + it);
		}
		while (!game.getCurrentPlayer().getHand().isEmpty());
		
		for (CardPlayer player : game.getPlayers()) {
			assertTrue("Hand for " + player + " is " + player.getHand(), player.getHand().isEmpty());
		}
		assertTrue(game.isGameOver());
	}

	@Override
	protected TurnEightGame newTestObject() {
		TurnEightGame game = new TurnEightGame();
		controller = new CGController(game);
		game.setRandomSeed(42);
		
		game.addPlayer("#AI_Idiot");
		controller.setAI(0, new CardAI_Random(game.getRandom()));
		
		game.addPlayer("#AI_TurnEight");
		controller.setAI(1, new TurnEightController.TurnEightAISkilled());
		
		game.addPlayer("Bubu");
		controller.setAI(2, new TurnEightController.TurnEightAISkilled());
		
		game.startGame();
		return game;
	}
	
}
