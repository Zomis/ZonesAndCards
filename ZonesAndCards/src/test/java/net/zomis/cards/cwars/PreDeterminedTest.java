package net.zomis.cards.cwars;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import net.zomis.cards.CardsTest;
import net.zomis.cards.ai.CGController;
import net.zomis.cards.cwars2.CWars2Card;
import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.cwars2.CWars2Setup;
import net.zomis.cards.cwars2.ais.CWars2AI_InstantWin;
import net.zomis.cards.cwars2.ais.CWars2AI_Random;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.StackAction;

import org.junit.Test;

public class PreDeterminedTest extends CardsTest<CWars2Game> {

	@Override
	protected void onBefore() {
		game = CWars2Setup.defaultGame(null);
	}
	
	private final List<String> cards = new ArrayList<String>();
	private final List<String> temp = new ArrayList<String>();
	private boolean finalized;
	
	@Test
	public void randomGeneration() {
		for (int i = 0; i < 10; i++) {
			game = CWars2Setup.defaultGame(42L);
			game.setRandomSeed(42);
			CGController controller = new CGController(game);
			controller.setAIs(new CWars2AI_Random(), new CWars2AI_InstantWin());
			assertEquals(30, game.getRandom().nextInt(100));
			game.startGame();
//			CustomFacade.getLog().i("Hand: " + game.getCurrentPlayer().getHand().cardList());
//			CustomFacade.getLog().i("Deck: " + game.getCurrentPlayer().getDeck().cardList());
			addOrCheck("Random is " + game.getRandom().nextInt(100));
			for (Card<CWars2Card> card : game.getCurrentPlayer().getHand()) {
				addOrCheck(card.getModel().getName());
			}
			addOrCheck("---");
			for (Card<CWars2Card> card : game.getCurrentPlayer().getHand()) {
				addOrCheck(card.getModel().getName());
			}
			
			while (!game.isGameOver()) {
				StackAction act = controller.play();
				addOrCheck(act.toString());// TODO: We're dependent on the toString implementation of ToggleDiscardAction!
			}
			
			
			listFilled();
		}
	}
	
	private void listFilled() {
		finalized = true;
		temp.clear();
	}

	private void addOrCheck(String value) {
//		CustomFacade.getLog().i(value);
		if (finalized) {
			assertEquals(cards.get(temp.size()), value);
			temp.add(value);
		}
		else {
			cards.add(value);
		}
	}
}
