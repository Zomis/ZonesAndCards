package test.net.zomis.cards.jackson;

import static org.junit.Assert.*;
import net.zomis.cards.cwars2.CWars2AI;
import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.jackson.CardsIO;
import net.zomis.cards.util.ResourceMap;
import net.zomis.custommap.CustomFacade;

import org.junit.Test;

import test.net.zomis.cards.CardsUntypedTest;

public class CardsLoadSaveTest extends CardsUntypedTest {
	@Test
	public void cw2game() {
		CWars2Game game = new CWars2Game();
		game.setRandomSeed(42);
		game.startGame();
		CWars2AI ai = new CWars2AI();
		game.callPlayerAI(ai);
		game.callPlayerAI(ai);
		game.callPlayerAI(ai);
		game.callPlayerAI(ai);
		ResourceMap res = game.getCurrentPlayer().getResources();

		String data = CardsIO.save(game);
		CustomFacade.getLog().i("Data is: " + data);
		CWars2Game loaded = CardsIO.load(data, CWars2Game.class);

		assertEquals(game.getCurrentPlayer().getName(), loaded.getCurrentPlayer().getName());
		assertEquals(game.getAvailableCards().size(), loaded.getAvailableCards().size());
		assertEquals(game.getActivePhase().getClass(), loaded.getActivePhase().getClass());
		assertNotSame(game.getActivePhase(), loaded.getActivePhase());
		assertNotSame(game.getCurrentPlayer(), loaded.getCurrentPlayer());
		assertNotSame(res, loaded.getCurrentPlayer().getResources());
		assertResourcesEqual(res, loaded.getCurrentPlayer().getResources());
	}
	
}
