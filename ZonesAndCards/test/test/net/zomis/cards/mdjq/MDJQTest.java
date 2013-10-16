package test.net.zomis.cards.mdjq;

import net.zomis.cards.mdjq.MDJQRes;
import net.zomis.cards.mdjq.MDJQRes.MColor;
import net.zomis.cards.util.ResourceMap;

import org.junit.Assert;
import org.junit.Test;

public class MDJQTest {

	public MDJQTest() {
	}
	
	@Test
	public void test() {
		ResourceMap manaPool = new ResourceMap();
		ResourceMap manaCost = new ResourceMap();
		manaPool.set(MDJQRes.getMana(MColor.BLACK), 4);
		manaCost.set(MDJQRes.getMana(MColor.BLACK), 2);
		manaCost.set(MDJQRes.getMana(MColor.COLORLESS), 2);
		
		Assert.assertTrue("Not enough resources: " + manaPool + " cost " + manaCost, MDJQRes.hasResources(manaPool, manaCost));
		System.out.println("Bwakkit");
		manaCost.set(MDJQRes.getMana(MColor.COLORLESS), 3);
		Assert.assertFalse("Has enough resources: " + manaPool + " cost " + manaCost, MDJQRes.hasResources(manaPool, manaCost));
		
		MDJQRes.changeResources(manaPool, manaCost, -1);
		System.out.println(manaPool);
		System.out.println(manaCost);
	}
	
}
