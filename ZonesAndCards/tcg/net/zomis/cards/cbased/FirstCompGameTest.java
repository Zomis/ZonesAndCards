package net.zomis.cards.cbased;

import static org.junit.Assert.*;
import net.zomis.cards.systems.RPSCardsSystem;
import net.zomis.cards.systems.RPSCardsSystem.RPS;
import net.zomis.custommap.ZomisSwing;
import net.zomis.custommap.view.ZomisLog;
import net.zomis.fight.ext.WinResult;

import org.junit.Before;
import org.junit.Test;

public class FirstCompGameTest {

	private FirstCompGame	game;

//	@Test
	public void testRPS() {
		for (RPS rps : RPS.values()) {
			for (RPS other : RPS.values()) {
				System.out.println(rps + " vs. " + other + ": " + fight(rps, other));
			}
		}
		
		assertEquals(WinResult.WIN, fight(RPS.ROCK, RPS.SCISSORS));
		assertEquals(WinResult.WIN, fight(RPS.SCISSORS, RPS.PAPER));
		assertEquals(WinResult.WIN, fight(RPS.PAPER, RPS.ROCK));
		
		assertEquals(WinResult.DRAW, fight(RPS.ROCK, RPS.ROCK));
		assertEquals(WinResult.DRAW, fight(RPS.SCISSORS, RPS.SCISSORS));
		assertEquals(WinResult.DRAW, fight(RPS.PAPER, RPS.PAPER));
		
		assertEquals(WinResult.LOSS, fight(RPS.SCISSORS, RPS.ROCK));
		assertEquals(WinResult.LOSS, fight(RPS.PAPER, RPS.SCISSORS));
		assertEquals(WinResult.LOSS, fight(RPS.ROCK, RPS.PAPER));
	}
	
	private Object fight(RPS rock, RPS scissors) {
		return RPSCardsSystem.fight(rock, scissors);
	}

	@Before
	public void setup() {
		ZomisSwing.setup();
		this.game = CompGameFactory.simple();
		game.startGame();
	}
	
	@Test
	public void playAGame() {
//		assertTrue(game.getCurrentPlayer().getComponent(DeckComponent.class).getDeck().size() > 20);
//		assertEquals(6, game.getCurrentPlayer().getComponent(HandComponent.class).getHand().size());
//		
//		assertTrue(game.nextPhase());
		CompPlayer cp = game.getCurrentPlayer();
		assertNotNull(cp);
		ZomisLog.info("Next");
		
		game.nextPhase();
		assertNotSame(cp, game.getCurrentPlayer());
		
		game.getPublicZones().stream().forEach(zone -> System.out.println(zone + ": " + zone.cardList()));
		
	}
	
}
