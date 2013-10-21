package test.net.zomis.cards;

import static org.junit.Assert.*;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.util.ResourceMap;
import net.zomis.cards.util.ResourceType;

import org.junit.Test;


public class SimpleGameTest extends CardsTest<CardGame> {

	@Test
	public void test() {
		ResourceType type = new ResourceType("Test Resource", 42);
		ResourceMap map = new ResourceMap();
		assertEquals(type.getDefault(), map.getResources(type));
		map.set(type, 15);
		assertEquals(15, map.getResources(type));
	}
	
	@Override
	protected CardGame newTestObject() {
		return new CardGame();
	}
	
}
