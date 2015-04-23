package net.zomis.cards.classic;

import static org.junit.Assert.*;
import net.zomis.aiscores.extra.ParamAndField;
import net.zomis.cards.CardsTest;
import net.zomis.cards.idiot.IdiotGame;
import net.zomis.cards.idiot.IdiotHandler.IdiotGameAI;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.Player;
import net.zomis.custommap.CustomFacade;

import org.junit.Test;

public class IdiotPlayAGameAndListen extends CardsTest<IdiotGame> { // Deckard Cain reference just for fun
	@Test
	public void playOrganized() {
		game = new IdiotGame();
		game.setRandomSeed(42);
		game.startGame();
		
		game.click(game.getDeck().getTopCard());
		assertEquals(48, game.getDeck().size());
	}
	
	@Test
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
		CustomFacade.getLog().i("WIN+0 is " + win);
		CustomFacade.getLog().i("WIN+1 is " + almost);
		CustomFacade.getLog().i("WIN+2 is " + close);
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
		ParamAndField<Player, Card<?>> move;
		boolean allowed;
		int it = 0;
		do {
			it++;
			move = ai.play(game.getCurrentPlayer());
			if (move.getField() == null)
				throw new NullPointerException("No move: " + move + " seed " + seed + " step " + it);
			allowed = game.click(move.getField());
		}
		while (allowed && it <= 10_000 && !game.isGameOver());
		
		assertTrue(game.isGameOver());
		
		return game.getCardsLeft();
//		System.out.println("Cards left: " + game.getCardsLeft());
	}

}
