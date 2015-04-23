package net.zomis.cards.jackson;

import static org.junit.Assert.*;
import net.zomis.cards.CardsUntypedTest;
import net.zomis.cards.ai.CGController;
import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.cwars2.CWars2Setup;
import net.zomis.cards.cwars2.ais.CWars2AI_Random;
import net.zomis.cards.resources.ResourceMap;

import org.junit.Ignore;
import org.junit.Test;

public class CardsLoadSaveTest extends CardsUntypedTest {
	@Test
	@Ignore
	public void cw2game() {
		CWars2Game game = CWars2Setup.defaultGame(42L);
		game.startGame();
		CWars2AI_Random ai = new CWars2AI_Random();
		CGController controller = new CGController(game);
		controller.playWithAI(ai);
		controller.playWithAI(ai);
		controller.playWithAI(ai);
		controller.playWithAI(ai);
		ResourceMap res = game.getCurrentPlayer().getResources();

//		String data = CardsIO.save(game);
////		CustomFacade.getLog().i("Data is: " + data);
//		CWars2Game loaded = CardsIO.load(data, CWars2Game.class);
		CWars2Game loaded = new CWars2Setup().build();

		assertEquals(game.getCurrentPlayer().getName(), loaded.getCurrentPlayer().getName());
		assertEquals(game.getCards().size(), loaded.getCards().size());
		assertEquals(game.getActivePhase().getClass(), loaded.getActivePhase().getClass());
		assertNotSame(game.getActivePhase(), loaded.getActivePhase());
		assertNotSame(game.getCurrentPlayer(), loaded.getCurrentPlayer());
		assertNotSame(res, loaded.getCurrentPlayer().getResources());
		assertResourcesEqual(res, loaded.getCurrentPlayer().getResources());
	}
	
}
