package test.net.zomis.cards;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map.Entry;

import net.zomis.ZomisList;
import net.zomis.cards.cwars2.CWars2AI;
import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.cwars2.CWars2PlayAction;
import net.zomis.cards.cwars2.CWars2Player;
import net.zomis.cards.cwars2.CWars2Res;
import net.zomis.cards.cwars2.CWars2Res.Producers;
import net.zomis.cards.cwars2.CWars2Res.Resources;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.util.IResource;
import net.zomis.cards.util.ResourceData;
import net.zomis.cards.util.ResourceMap;
import net.zomis.cards.util.StackActionAllowedFilter;
import net.zomis.custommap.CustomFacade;

import org.junit.After;
import org.junit.Test;

public class CWarsTest extends CardsTest<CWars2Game> {

//	@Test(timeout = 10000)
	@Test
	public void play() {
		assertFalse(game.isGameOver());
		assertZoneSize(game.getCurrentPlayer().getHandSize(), game.getCurrentPlayer().getHand());
		assertTrue(game.getCurrentPlayer().getCards().size() >= CWars2Game.MIN_CARDS_IN_DECK);
		assertTrue(game.getCurrentPlayer().getDeck().size() + " expected at least " + CWars2Game.MIN_CARDS_IN_DECK,
				game.getCurrentPlayer().getDeck().size() + game.getCurrentPlayer().getHand().size() >= CWars2Game.MIN_CARDS_IN_DECK);
		
		while (!game.isGameOver()) {
			CWars2Player player = game.getCurrentPlayer();
			List<StackAction> available = game.getActionHandler().getAvailableActions(game.getCurrentPlayer());
			ResourceMap beforeMe = new ResourceMap(game.getCurrentPlayer().getResources());
			ResourceMap beforeOther = new ResourceMap(game.getCurrentPlayer().getNextPlayer().getResources());
			ZomisList.filter(available, new StackActionAllowedFilter(true));
			assertFalse("No available allowed actions for " + game.getCurrentPlayer() + "\nDiscard count is " + game.getDiscarded(), available.isEmpty());
			
			assertResources();
			if (beforeMe.getResources(Resources.CRYSTALS) == 5 && beforeMe.getResources(Resources.BRICKS) == 3) {
				CustomFacade.getLog().i("XXXXXXXXXXX " + player.getResources().toString());
			}
			StackAction act = game.callPlayerAI();
			CustomFacade.getLog().i(player.getResources().toString());
			CustomFacade.getLog().i("Act is " + act);
			assertResources();
			
			assertTrue("Action Not performed: " + act, act.actionIsPerformed());
			if (act instanceof CWars2PlayAction) {
				assertNotSame("Player not changed when playing " + act, player, game.getCurrentPlayer());
			}
			for (Entry<IResource, ResourceData> ee : game.getCurrentPlayer().getResources().getData().entrySet()) {
				if (ee.getKey() instanceof Resources)
					assertTrue(ee.getValue() + " of " + ee.getKey() + " after " + act 
							+ " previous " + beforeMe + " vs. " + beforeOther + " current " + game.getCurrentPlayer().getResources(), ee.getValue().getRealValueOrDefault() >= 0);
				if (ee.getKey() instanceof Producers)
					assertTrue(ee.getValue() + " of " + ee.getKey() + " after " + act 
							+ " previous " + beforeMe + " vs. " + beforeOther + " current " + game.getCurrentPlayer().getResources(), ee.getValue().getRealValueOrDefault() >= 1);
				if (ee.getKey() == CWars2Res.WALL)
					assertTrue(ee.getValue() + " of " + ee.getKey() + " after " + act 
							+ " previous " + beforeMe + " vs. " + beforeOther + " current " + game.getCurrentPlayer().getResources(), ee.getValue().getRealValueOrDefault() >= 0);
			}
		}
		assertTrue(game.isGameOver());
		game = null;
	}
	
	private void assertResources() {
		for (Player player : game.getPlayers()) {
			for (Entry<IResource, ResourceData> ee : player.getResources().getData().entrySet()) {
				if (ee.getKey() instanceof Resources)
					assertTrue(ee.getValue() + " of " + ee.getKey() + player.getResources(), ee.getValue().getRealValueOrDefault() >= 0);
				if (ee.getKey() instanceof Producers)
					assertTrue(ee.getValue() + " of " + ee.getKey() + player.getResources(), ee.getValue().getRealValueOrDefault() >= 1);
				if (ee.getKey() == CWars2Res.WALL)
					assertTrue(ee.getValue() + " of " + ee.getKey() + player.getResources(), ee.getValue().getRealValueOrDefault() >= 0);
			}
		}
	}

	@After
	public void after() {
		if (game == null)
			return;
		
//		for (Player pl : game.getPlayers()) {
//			CWars2Player player = (CWars2Player) pl;
//			CustomFacade.getLog().i("Player:\n" + player);
//			CustomFacade.getLog().i("\n");
//			CustomFacade.getLog().i("Cards:\n" + player.getCards());
//			CustomFacade.getLog().i("---------------");
//		}
		
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
