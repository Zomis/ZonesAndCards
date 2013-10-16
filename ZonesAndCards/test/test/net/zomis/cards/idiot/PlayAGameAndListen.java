package test.net.zomis.cards.idiot;

import junit.framework.Assert;
import net.zomis.aiscores.ParamAndField;
import net.zomis.cards.idiot.IdiotGame;
import net.zomis.cards.idiot.IdiotHandler.IdiotGameAI;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.custommap.CustomFacade;
import net.zomis.custommap.view.Log4jLog;
import net.zomis.custommap.view.swing.ZomisSwingLog4j;

import org.junit.BeforeClass;
import org.junit.Test;

public class PlayAGameAndListen { // Deckard Cain reference just for fun
	@BeforeClass
	public static void bfe() {
		ZomisSwingLog4j.addConsoleAppender(null);
		new CustomFacade(new Log4jLog("Cards"));
	}
	
	@Test
	public void playMany() {
		int win = 0;
		int almost = 0;
		int close = 0;
		for (int i = 0; i < 100; i++) {
			int value = play(i);
			if (value == 4) win++;
			if (value == 5) almost++;
			if (value == 6) close++;
		}
		System.out.println("4 is " + win);
		System.out.println("5 is " + almost);
		System.out.println("6 is " + close);
	}
	public int play(long seed) {
		
		IdiotGame game = new IdiotGame();
		game.setRandomSeed(seed);
		game.startGame();
		
		IdiotGameAI ai = new IdiotGameAI(game);
		Assert.assertNotNull(game.getCurrentPlayer());
		ParamAndField<Player, StackAction> move;
		do {
			move = ai.play();
			if (move != null) {
				game.addAndProcessStackAction(move.getField());
			}
		}
		while (move != null);
		
		return game.getCardsLeft();
//		System.out.println("Cards left: " + game.getCardsLeft());
	}

}
