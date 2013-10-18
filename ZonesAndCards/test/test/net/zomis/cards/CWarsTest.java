package test.net.zomis.cards;

import static org.junit.Assert.*;

import java.util.List;

import net.zomis.ZomisList;
import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.cwars2.CWars2PlayAction;
import net.zomis.cards.cwars2.CWars2Player;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.util.StackActionAllowedFilter;
import net.zomis.custommap.CustomFacade;

import org.junit.After;
import org.junit.Test;

public class CWarsTest extends CardsTest<CWars2Game> {

	@Test(timeout = 10000)
	public void play() {
		assertFalse(game.isGameOver());
		assertZoneSize(game.getCurrentPlayer().getHandSize(), game.getCurrentPlayer().getHand());
		assertTrue(game.getCurrentPlayer().getCards().size() >= CWars2Game.MIN_CARDS_IN_DECK);
		assertTrue(game.getCurrentPlayer().getDeck().size() + " expected at least " + CWars2Game.MIN_CARDS_IN_DECK,
				game.getCurrentPlayer().getDeck().size() + game.getCurrentPlayer().getHand().size() >= CWars2Game.MIN_CARDS_IN_DECK);
		
		while (!game.isGameOver()) {
			CWars2Player player = game.getCurrentPlayer();
			List<StackAction> available = game.getAIHandler().getAvailableActions(game.getCurrentPlayer());
			ZomisList.filter(available, new StackActionAllowedFilter(true));
			assertFalse("No available allowed actions for " + game.getCurrentPlayer() + "\nDiscard count is " + game.getDiscarded(), available.isEmpty());
			StackAction act = game.callPlayerAI();
			assertTrue("Action Not performed: " + act, act.isPerformed());
			if (act instanceof CWars2PlayAction) {
				assertNotSame("Player not changed when playing " + act, player, game.getCurrentPlayer());
			}
		}
		for (Player player : game.getPlayers()) {
			CWars2Player pl = (CWars2Player) player;
			CustomFacade.getLog().i(pl.getResources().toString());
		}
		assertTrue(game.isGameOver());
		game = null;
	}
	
	@After
	public void after() {
		if (game == null)
			return;
		
		for (Player pl : game.getPlayers()) {
			CWars2Player player = (CWars2Player) pl;
			CustomFacade.getLog().i("Player:\n" + player);
			CustomFacade.getLog().i("\n");
			CustomFacade.getLog().i("Cards:\n" + player.getCards());
			CustomFacade.getLog().i("---------------");
		}
		
		assertNull(game);
	}
	
	@Override
	protected void onBefore() {
		game = new CWars2Game();
		game.setRandomSeed(42);
		assertTrue(game.getAvailableCards().size() > 0);
		for (Player pl : game.getPlayers()) {
//			pl.setAI(new RandomAI(game.getRandom()));
			pl.setAI(new CWars2AI());
		}
		game.startGame();
	}
	
}
