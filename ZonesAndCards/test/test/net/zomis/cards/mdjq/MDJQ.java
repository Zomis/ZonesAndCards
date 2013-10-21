package test.net.zomis.cards.mdjq;

import static org.junit.Assert.*;
import net.zomis.cards.mdjq.MDJQRes.MColor;
import net.zomis.cards.mdjq.MDJQSelectTargetsPhase;
import net.zomis.cards.mdjq.phases.MDJQMainPhase;
import net.zomis.cards.mdjq.phases.MDJQUntapPhase;
import net.zomis.cards.model.StackAction;

import org.junit.Test;

public class MDJQ extends MDJQTest {
	@Test
	public void playPredefined() {
		// Play a land
		assertPhase(MDJQMainPhase.class);
		currentPlayerPlayCard("Plains");
		assertFalse(findActionForCard(game.getCurrentPlayer().getHand(), "Plains").actionIsAllowed());
		assertBattlefieldSize(1);
		
		// Tap land for mana
		StackAction sa = game.getAIHandler().click(game.getBattlefield().getBottomCard());
		assertTrue(sa.actionIsAllowed());
		game.addAndProcessStackAction(sa);
		assertMana(game.getCurrentPlayer(), MColor.WHITE, 1);
		assertFalse(sa.actionIsAllowed());
		
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

}
