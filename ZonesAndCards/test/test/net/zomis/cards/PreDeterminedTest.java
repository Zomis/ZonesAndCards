package test.net.zomis.cards;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import net.zomis.cards.cwars2.CWars2AI;
import net.zomis.cards.cwars2.CWars2AI_InstantWin;
import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.StackAction;

import org.junit.Test;

public class PreDeterminedTest extends CardsTest<CWars2Game> {

	@Override
	protected void onBefore() {
		game = new CWars2Game();
	}
	
	private final List<String> cards = new ArrayList<String>();
	private final List<String> temp = new ArrayList<String>();
	private boolean finalized;
	@Test
	public void randomGeneration() {
		for (int i = 0; i < 10; i++) {
			game = new CWars2Game();
			game.setRandomSeed(42);
			game.getPlayers().get(0).setAI(new CWars2AI());
			game.getPlayers().get(1).setAI(new CWars2AI_InstantWin());
			Assert.assertEquals(30, game.getRandom().nextInt(100));
			game.startGame();
//			CustomFacade.getLog().i("Hand: " + game.getCurrentPlayer().getHand().cardList());
//			CustomFacade.getLog().i("Deck: " + game.getCurrentPlayer().getDeck().cardList());
			addOrCheck("Random is " + game.getRandom().nextInt(100));
			for (Card card : game.getCurrentPlayer().getHand().cardList()) {
				addOrCheck(card.getModel().getName());
			}
			addOrCheck("---");
			for (Card card : game.getCurrentPlayer().getHand().cardList()) {
				addOrCheck(card.getModel().getName());
			}
			
			while (!game.isGameOver()) {
				StackAction act = game.callPlayerAI();
				addOrCheck(act.toString());
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
			Assert.assertEquals(cards.get(temp.size()), value);
			temp.add(value);
		}
		else {
			cards.add(value);
		}
	}
}
