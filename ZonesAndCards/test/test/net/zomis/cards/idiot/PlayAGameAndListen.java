package test.net.zomis.cards.idiot;

import net.zomis.aiscores.ParamAndField;
import net.zomis.cards.idiot.IdiotGame;
import net.zomis.cards.idiot.IdiotHandler.IdiotGameAI;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.custommap.CustomFacade;
import net.zomis.custommap.view.Log4jLog;
import net.zomis.custommap.view.swing.ZomisSwingLog4j;
import net.zomis.fizzbuzz.DividableFizz;
import net.zomis.fizzbuzz.FizzBuzz;

import org.junit.BeforeClass;
import org.junit.Test;

public class PlayAGameAndListen { // Deckard Cain reference just for fun
	@BeforeClass
	public static void bfe() {
		ZomisSwingLog4j.addConsoleAppender(null);
		new CustomFacade(new Log4jLog("Cards"));
	}
	
	public static class MyFizzBuzz extends FizzBuzz {
		@Override
		protected void handleNumber(int i) {
//			if (i <= 6) {
				super.handleNumber(i);
//			}
		}
	}
	
	@Test
	public void play1000() {
		FizzBuzz buzzer = new MyFizzBuzz();
//		buzzer.addFizz(new ExactFizz("YAY!", 4));
//		buzzer.addFizz(new ExactFizz("Not bad!", 5));
//		buzzer.addFizz(new ExactFizz("Bahf!", 6));
		buzzer.addFizz(new DividableFizz(3, "Fizz"));
		buzzer.addFizz(new DividableFizz(5, "Buzz"));
		
		int win = 0;
		int almost = 0;
		int close = 0;
		for (int i = 0; i < 10000; i++) {
			int value = play();
			if (value == 4) win++;
			if (value == 5) almost++;
			if (value == 6) close++;
			buzzer.perform(value, value);
		}
		System.out.println("4 is " + win);
		System.out.println("5 is " + almost);
		System.out.println("6 is " + close);
	}
	
	public int play() {
		
		IdiotGame game = new IdiotGame();
		
		IdiotGameAI ai = new IdiotGameAI(game);
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
