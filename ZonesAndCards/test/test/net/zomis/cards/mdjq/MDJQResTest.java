package test.net.zomis.cards.mdjq;

import static org.junit.Assert.*;
import net.zomis.cards.mdjq.MDJQGame;
import net.zomis.cards.mdjq.MDJQRes;
import net.zomis.cards.mdjq.MDJQRes.MColor;
import net.zomis.cards.util.ResourceMap;

import org.junit.Ignore;
import org.junit.Test;

public class MDJQResTest extends MDJQTest {

	@Test
	@Ignore
	public void resourceFlexible() {
		ResourceMap manaPool = new ResourceMap();
//		ResourceMap manaCost = new ResourceMap();
		manaPool.set(MDJQRes.getMana(MColor.BLACK), 2);
//		manaCost.set(MDJQRes.flexibleMana(MColor.BLACK, MColor.WHITE), 1);
		fail("Unable to pay for B/W using B mana.");
	}
	@Test
	public void resourceTest() {
		ResourceMap manaPool = new ResourceMap();
		ResourceMap manaCost = new ResourceMap();
		manaPool.set(MDJQRes.getMana(MColor.BLACK), 4);
		
		manaCost.set(MDJQRes.getMana(MColor.BLACK), 2);
		manaCost.set(MDJQRes.getMana(MColor.COLORLESS), 2);
		
		assertTrue("Not enough resources: " + manaPool + " cost " + manaCost, MDJQRes.hasResources(manaPool, manaCost));
		manaCost.set(MDJQRes.getMana(MColor.COLORLESS), 3);
		assertFalse("Has enough resources: " + manaPool + " cost " + manaCost, MDJQRes.hasResources(manaPool, manaCost));
		
		MDJQRes.changeResources(manaPool, manaCost, -1);
	}

	@Override
	protected void onBefore() {
		game = new MDJQGame();
	}
	
	
}
