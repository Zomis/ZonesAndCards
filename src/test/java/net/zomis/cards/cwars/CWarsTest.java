package net.zomis.cards.cwars;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map.Entry;

import net.zomis.cards.CardsTest;
import net.zomis.cards.ai.CGController;
import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.cwars2.CWars2PlayAction;
import net.zomis.cards.cwars2.CWars2Player;
import net.zomis.cards.cwars2.CWars2Res;
import net.zomis.cards.cwars2.CWars2Res.Producers;
import net.zomis.cards.cwars2.CWars2Res.Resources;
import net.zomis.cards.cwars2.CWars2Setup;
import net.zomis.cards.cwars2.ais.CWars2AI_Random;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.resources.IResource;
import net.zomis.cards.resources.ResourceData;
import net.zomis.cards.resources.ResourceMap;
import net.zomis.cards.util.StackActionAllowedFilter;
import net.zomis.custommap.CustomFacade;
import net.zomis.utils.ZomisList;

import org.junit.Test;

public class CWarsTest extends CardsTest<CWars2Game> {

//	@Test(timeout = 10000)
	@Test
	public void play() {
		assertFalse(game.isGameOver());
		assertZoneSize(game.getCurrentPlayer().handSize(), game.getCurrentPlayer().getHand());
		assertTrue(game.getCurrentPlayer().getCards().size() >= game.getMinCardsInDeck());
		assertTrue(game.getCurrentPlayer().getDeck().size() + " expected at least " + game.getMinCardsInDeck(),
				game.getCurrentPlayer().getDeck().size() + game.getCurrentPlayer().getHand().size() >= game.getMinCardsInDeck());
		
		CGController controller = new CGController(game);
		for (Player pl : game.getPlayers()) {
			controller.setAI(pl, new CWars2AI_Random());
		}
		
		while (!game.isGameOver()) {
			CWars2Player player = game.getCurrentPlayer();
			List<Card<?>> available = game.getUseableCards(game.getCurrentPlayer());
			ResourceMap beforeMe = new ResourceMap(game.getCurrentPlayer().getResources());
			ResourceMap beforeOther = new ResourceMap(game.getCurrentPlayer().getNextPlayer().getResources());
			ZomisList.filter(available, new StackActionAllowedFilter());
			assertFalse("No available allowed actions for " + game.getCurrentPlayer() + "\nDiscard count is " + game.getDiscarded(), available.isEmpty());
			
			assertResources();
			if (beforeMe.getResources(Resources.CRYSTALS) == 5 && beforeMe.getResources(Resources.BRICKS) == 3) {
				CustomFacade.getLog().i("XXXXXXXXXXX " + player.getResources().toString());
			}
			StackAction act = controller.play();
//			CustomFacade.getLog().i(player.getResources().toString());
//			CustomFacade.getLog().i("Act is " + act);
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
		assertNull(game);
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

	@Override
	protected void onBefore() {
		game = CWars2Setup.defaultGame(42L);
		assertFalse(game.getCards().isEmpty());
		game.startGame();
	}
	
}
