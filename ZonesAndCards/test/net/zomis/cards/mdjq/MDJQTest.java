package net.zomis.cards.mdjq;

import static org.junit.Assert.*;

import java.util.List;

import net.zomis.cards.CardsTest;
import net.zomis.cards.mdjq.MDJQGame;
import net.zomis.cards.mdjq.MDJQObject;
import net.zomis.cards.mdjq.MDJQPermanent;
import net.zomis.cards.mdjq.MDJQPhase;
import net.zomis.cards.mdjq.MDJQPlayer;
import net.zomis.cards.mdjq.MDJQRes.MColor;
import net.zomis.cards.mdjq.MDJQStackAction;
import net.zomis.cards.mdjq.MDJQTarget;
import net.zomis.cards.mdjq.MDJQZone;
import net.zomis.cards.mdjq.actions.MDJQTargetAction;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.resources.ResourceMap;
import net.zomis.custommap.CustomFacade;
import net.zomis.utils.ZomisList;

public class MDJQTest extends CardsTest<MDJQGame> {

	
	protected void assertLife(int expected, MDJQPlayer player) {
		assertEquals("Unexpected life for " + player, expected, player.getLife());
	}

	protected void chooseRandomTargets() {
		for (MDJQStackAction act : game.getStackZone()) {
			if (act instanceof MDJQTargetAction) {
				this.chooseTargetsFor((MDJQTargetAction) act);
			}
		}
	}

	protected void chooseTargetsFor(MDJQTargetAction act) {
		for (MDJQTarget target : act.getTargets().getTargets()) {
			if (!target.isTargetChosen()) {
				List<MDJQObject> targets = target.findLegalTargets(act);
//				log("Legal targets for " + act + ": " + targets.toString());
				target.setChosenTarget(ZomisList.getRandom(targets, game.getRandom()));
				assertTrue(target.isTargetChosen());
			}
		}
		assertTrue(act.getTargets().isAllChosen());
	}

	protected void logStack() {
		CustomFacade.getLog().i("Stack Contains: " + game.getStackZone());
//		for (MDJQStackAction act : game.getStackZone()) {
//		}
	}

	protected void assertStackSize(int i) {
		assertEquals("Unexpected stack size: " + game.getStackZone(), i, game.getStackZone().size());
	}

	protected void assertPhase(Class<? extends MDJQPhase> class1) {
		assertEquals(class1, game.getActivePhase().getClass());
	}

	protected static StackAction findActionForCard(MDJQZone zone, String cardName) {
		MDJQPermanent card = zone.findCardWithName(cardName);
		assertNotNull("Could not find card by name " + cardName + " in " + zone, card);
		return zone.getGame().getActionFor(card);
	}
	protected void currentPlayerPlayCard(String cardName) {
		game.addStackAction(findActionForCard(game.getCurrentPlayer().getHand(), cardName));
	}
	
	protected void logHand() {
		CustomFacade.getLog().i("Player Hand: " + game.getCurrentPlayer().getHand());
	}

	protected void assertMana(ResourceMap res, MColor color, int i) {
		assertEquals(i, res.getResources(color));
	}
	protected void assertMana(MDJQPlayer player, MColor color, int i) {
		assertEquals(i, player.getManaPool().getResources(color));
	}

	protected void nextPhaseUntil(Class<? extends MDJQPhase> class1) {
		while (game.getActivePhase().getClass() != class1) {
			assertTrue("Could not enter next phase from " + game.getActivePhase(), game.nextPhase());
		}
	}

	protected void assertBattlefieldSize(int i) {
		assertZoneSize(i, game.getBattlefield());
	}

	
	@Override
	protected void onBefore() {
		game = new MDJQGame();
		game.setRandomSeed(22);
		game.startGame();
	}
	
}
