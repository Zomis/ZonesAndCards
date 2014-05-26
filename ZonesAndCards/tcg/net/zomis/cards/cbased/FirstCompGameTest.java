package net.zomis.cards.cbased;

import static org.junit.Assert.*;
import net.zomis.custommap.ZomisSwing;

import org.junit.Before;
import org.junit.Test;

public class FirstCompGameTest {

	private FirstCompGame	game;

	@Before
	public void setup() {
		ZomisSwing.setup();
		this.game = new FirstCompGame();
		game.startGame();
	}
	
	@Test
	public void playAGame() {
		
		
		assertTrue(game.nextPhase());
		
		
	}
	
}
