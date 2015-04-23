package net.zomis.cards.wart;

import static org.junit.Assert.*;
import net.zomis.cards.wart.factory.HStoneChars;
import net.zomis.custommap.ZomisSwing;

import org.junit.Ignore;
import org.junit.Test;

public class WSReplayTest {

	@Test
	@Ignore
	public void rep() {
		
		ZomisSwing.setup();
		HStoneGame game = new HStoneGame(HStoneChars.jaina("Zomis"), HStoneChars.jaina("Bubu"));
		game.startGame();
		game.nextPhase();
		
		String init = game.getReplay().getInitialization();
		System.out.println(init);
		
		fail();
		
		// TODO: Test HStone with replays
	}
	
}
