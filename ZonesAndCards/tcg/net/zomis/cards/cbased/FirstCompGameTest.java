package net.zomis.cards.cbased;

import static org.junit.Assert.*;
import net.zomis.cards.components.DeckComponent;
import net.zomis.cards.components.HandComponent;
import net.zomis.custommap.ZomisSwing;

import org.junit.Before;
import org.junit.Test;

public class FirstCompGameTest {

	private FirstCompGame	game;

	@Before
	public void setup() {
		ZomisSwing.setup();
		this.game = CompGameFactory.simple();
		game.startGame();
	}
	
	@Test
	public void playAGame() {
		
		assertTrue(game.getCurrentPlayer().getComponent(DeckComponent.class).getDeck().size() > 20);
		assertEquals(6, game.getCurrentPlayer().getComponent(HandComponent.class).getHand().size());
		
		assertTrue(game.nextPhase());
		
		game.getPublicZones().stream().forEach(zone -> System.out.println(zone + ": " + zone.cardList()));
		
	}
	
}
