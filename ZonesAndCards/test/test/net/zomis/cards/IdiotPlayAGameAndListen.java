package test.net.zomis.cards;

import static org.junit.Assert.*;
import net.zomis.aiscores.ParamAndField;
import net.zomis.cards.idiot.IdiotGame;
import net.zomis.cards.idiot.IdiotHandler.IdiotGameAI;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.custommap.CustomFacade;

import org.junit.Test;

public class IdiotPlayAGameAndListen extends CardsTest<IdiotGame> { // Deckard Cain reference just for fun
	@Test
	public void playOrganized() {
		game = new IdiotGame();
		game.setRandomSeed(42);
		game.startGame();
		
		game.addAndProcessStackAction(game.getActionHandler().click(game.getDeck().getTopCard()));
		assertEquals(48, game.getDeck().size());
	}
	
	@Test(timeout = 20000)
	public void playMany() {
		int win = 0;
		int almost = 0;
		int close = 0;
		for (int i = 0; i < 200; i++) {
			int value = play(i);
			if (value == 4) win++;
			if (value == 5) almost++;
			if (value == 6) close++;
		}
		CustomFacade.getLog().i("4 is " + win);
		CustomFacade.getLog().i("5 is " + almost);
		CustomFacade.getLog().i("6 is " + close);
	}
	@Override
	protected void onBefore() {
		game = new IdiotGame();
		game.startGame();
	}
	
	public int play(long seed) {
		
		IdiotGame game = new IdiotGame();
		game.setRandomSeed(seed);
		game.startGame();
		
		IdiotGameAI ai = new IdiotGameAI(game);
		assertNotNull(game.getCurrentPlayer());
		ParamAndField<Player, StackAction> move;
		boolean allowed;
		do {
			move = ai.play(game.getCurrentPlayer());
			allowed = move.getField().actionIsAllowed();
			if (allowed) {
				game.addAndProcessStackAction(move.getField());
			}
		}
		while (allowed);
		
		assertTrue(game.isGameOver());
		
		return game.getCardsLeft();
//		System.out.println("Cards left: " + game.getCardsLeft());
	}

}
