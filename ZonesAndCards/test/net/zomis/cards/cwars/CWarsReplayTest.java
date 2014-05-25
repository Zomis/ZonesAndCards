package net.zomis.cards.cwars;

import static org.junit.Assert.*;
import net.zomis.cards.CardsTest;
import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.cwars2.CWars2Setup;
import net.zomis.cards.cwars2.ais.CWars2AI_Better;
import net.zomis.cards.cwars2.ais.CWars2Decks;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardReplay;
import net.zomis.cards.resources.ResourceMap;
import net.zomis.custommap.ZomisSwing;

import org.junit.BeforeClass;
import org.junit.Test;

public class CWarsReplayTest extends CardsTest<Integer> {

	@Override
	protected Integer newTestObject() {
		return 42;
	}
	
	@BeforeClass
	public static void init() {
		ZomisSwing.setup();
	}
	
	@Test
	public void dfas() {
		CWars2Setup setup = CWars2Setup.newSingleplayerGame();
		setup.setDecks(CWars2Decks.zomisSingleplayerBuilding(), CWars2Decks.zomisSingleplayerControl());
		CWars2Game game = setup.build();
		game.startGame();
		String gameInit = game.getReplay().getInitialization();
		
		// Make some moves
		CWars2AI_Better ai = new CWars2AI_Better();
		for (int i = 0; i < 10; i++) {
			Card<?> action = ai.play(game.getCurrentPlayer()).getField();
			assertTrue(game.click(action));
		}
		String moves = game.getReplay().getMoveString();
		ResourceMap player1 = game.getPlayers().get(0).getResources();
		ResourceMap player2 = game.getPlayers().get(1).getResources();
		
		// Output
		System.out.println(gameInit);
		System.out.println(moves);
		
		// Load the replay
		setup = CWars2Setup.newSingleplayerGame();
		game = setup.build();
		CardReplay replay = new CardReplay(game);
		replay.applyInitialization(gameInit);
		assertEquals(gameInit, replay.getInitialization());
		game.startGame();
		replay.performMoves(moves);
		
		assertEquals(moves, replay.getMoveString());
		assertResourcesEqual(player1, game.getPlayers().get(0).getResources());
		assertResourcesEqual(player2, game.getPlayers().get(1).getResources());
		
//		assertFalse("Dummy", true);
	}
	
}
