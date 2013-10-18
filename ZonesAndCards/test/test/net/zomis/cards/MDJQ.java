package test.net.zomis.cards;

import static org.junit.Assert.*;
import net.zomis.cards.mdjq.MDJQRes;
import net.zomis.cards.mdjq.MDJQSelectTargetsPhase;
import net.zomis.cards.mdjq.MDJQRes.MColor;
import net.zomis.cards.mdjq.phases.MDJQMainPhase;
import net.zomis.cards.mdjq.phases.MDJQUntapPhase;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.util.ResourceMap;

import org.junit.Test;

public class MDJQ extends MDJQTest {
	@Test
	public void playPredefined() {
		// Play a land
		assertPhase(MDJQMainPhase.class);
		currentPlayerPlayCard("Plains");
		assertFalse(findActionForCard(game.getCurrentPlayer().getHand(), "Plains").isAllowed());
		assertBattlefieldSize(1);
		
		// Tap land for mana
		StackAction sa = game.getAIHandler().click(game.getBattlefield().getBottomCard());
		assertTrue(sa.isAllowed());
		game.addAndProcessStackAction(sa);
		assertMana(game.getCurrentPlayer(), MColor.WHITE, 1);
		assertFalse(sa.isAllowed());
		
		// Play "Eager Cadet"
		currentPlayerPlayCard("Eager Cadet");
		assertFalse(game.isEmptyStack());
		game.processStackAction();
		assertBattlefieldSize(2);
		assertTrue(game.isEmptyStack());
		
		nextPhaseUntil(MDJQUntapPhase.class);
		nextPhaseUntil(MDJQMainPhase.class);
		log("Player 2");
		
		assertZoneSize(8, game.getCurrentPlayer().getHand());
		currentPlayerPlayCard("Swamp");
		assertBattlefieldSize(3);
		game.addAndProcessStackAction(game.getAIHandler().click(game.getBattlefield().getBottomCard()));
		assertMana(game.getCurrentPlayer(), MColor.BLACK, 1);
		
		nextPhaseUntil(MDJQUntapPhase.class);
		nextPhaseUntil(MDJQMainPhase.class);
		logHand();
		log("Player 1 again");
		
		// Play a card which will trigger an ability
		assertZoneSize(6, game.getCurrentPlayer().getHand());
		currentPlayerPlayCard("Test");
		assertStackSize(1);
		log("Now processing special");
		game.processStackAction();
		assertStackSize(1);
		logStack();
		game.processStackAction();
		assertPhase(MDJQSelectTargetsPhase.class);
		game.processStackAction();
		assertPhase(MDJQSelectTargetsPhase.class);
		assertFalse(game.isEmptyStack());
		chooseRandomTargets();
		game.processStackAction();
		assertTrue(game.isEmptyStack());
		assertLife(22, game.getCurrentPlayer());
		assertLife(18, game.getCurrentPlayer().getNextPlayer());
		
		assertPhase(MDJQMainPhase.class);
		
	}

	@Test
	public void resourceTest() {
		ResourceMap manaPool = new ResourceMap();
		ResourceMap manaCost = new ResourceMap();
		manaPool.set(MDJQRes.getMana(MColor.BLACK), 4);
		manaCost.set(MDJQRes.getMana(MColor.BLACK), 2);
		manaCost.set(MDJQRes.getMana(MColor.COLORLESS), 2);
		
		assertTrue("Not enough resources: " + manaPool + " cost " + manaCost, MDJQRes.hasResources(manaPool, manaCost));
		manaCost.set(MDJQRes.getMana(MColor.COLORLESS), 3);
		assertFalse("Has enough resources: " + manaPool + " cost " + manaCost, MDJQRes.hasResources(manaPool, manaCost));
		
		MDJQRes.changeResources(manaPool, manaCost, -1);
	}

}
