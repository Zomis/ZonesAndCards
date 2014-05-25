package net.zomis.cards;

import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.List;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.resources.ResourceMap;
import net.zomis.cards.resources.ResourceType;

import org.junit.Test;


public class SimpleGameTest extends CardsTest<SimpleGame> {

	@Test
	public void topAndBottom() {
		game.a.createCardOnBottom(game.model[0]);
		game.a.createCardOnBottom(game.model[1]);
		game.a.createCardOnTop(game.model[2]);
		assertCards(game.a, new int[]{ 2, 0, 1 });
		
		game.b.createCardOnTop(game.model[3]);
		game.b.createCardOnTop(game.model[4]);
		assertCards(game.b, new int[]{ 4, 3 });
		
		game.c.createCardOnTop(game.model[3]);
		game.c.createCardOnTop(game.model[4]);
		game.c.createCardOnBottom(game.model[2]);
		assertCards(game.c, new int[]{ 4, 3, 2 });
		
		game.c.moveToTopOf(game.a);
		assertCards(game.c, new int[]{ });
		assertCards(game.a, new int[]{ 4, 3, 2, 2, 0, 1 });
		
		game.a.extractTopCards(3).moveToBottomOf(game.d);
		assertCards(game.a, new int[]{ 2, 0, 1 });
		assertCards(game.d, new int[]{ 4, 3, 2 });
		
		game.b.moveToBottomOf(game.d);
		assertCards(game.b, new int[]{ });
		assertCards(game.d, new int[]{ 4, 3, 2, 4, 3 });
		
		game.a.moveToTopOf(game.d);
		assertCards(game.a, new int[]{ });
		assertCards(game.b, new int[]{ });
		assertCards(game.c, new int[]{ });
		assertCards(game.d, new int[]{ 2, 0, 1, 4, 3, 2, 4, 3 });
		
		game.d.sort(new Comparator<Card<CardModel>>() {
			@Override
			public int compare(Card<CardModel> c1, Card<CardModel> c2) {
				return c1.getModel().getName().compareTo(c2.getModel().getName());
			}
		});
		
		assertCards(game.d, new int[]{ 0, 1, 2, 2, 3, 3, 4, 4 });
		game.d.extractBottomCards(2).moveToTopOf(game.d);
		assertCards(game.d, new int[]{ 4, 4, 0, 1, 2, 2, 3, 3 });
		
		game.d.extractBottomCards(3).moveToTopOf(game.d);
		assertCards(game.d, new int[]{ 2, 3, 3, 4, 4, 0, 1, 2 });
		
		game.d.moveToTopOf(game.d);
		assertCards(game.d, new int[]{ 2, 3, 3, 4, 4, 0, 1, 2 });
		game.d.moveToBottomOf(game.d);
		assertCards(game.d, new int[]{ 2, 3, 3, 4, 4, 0, 1, 2 });
		
		game.d.shuffle();
		assertCards(game.d, new int[]{ 3, 1, 4, 3, 4, 2, 2, 0 });
		
		game.d.extractTopCards(5).reverse().moveToTopOf(game.d);
		assertCards(game.d, new int[]{ 4, 3, 4, 1, 3, 2, 2, 0 });
		
		game.d.getBottomCard().zoneMoveOnTop(game.a);
		game.d.extractTopCards(3).moveToBottomOf(game.d);
		game.d.getTopCard().zoneMoveOnBottom(game.a);
		assertCards(game.a, new int[]{ 0, 1 });
		assertCards(game.d, new int[]{ 3, 2, 2, 4, 3, 4 });
		game.d.extractTopCards(3).reverse().moveToBottomOf(game.a);
		assertCards(game.a, new int[]{ 0, 1, 2, 2, 3 });
		assertCards(game.d, new int[]{ 4, 3, 4 });
		game.d.extractTopCards(2).reverse().moveToTopOf(game.d);
		game.d.moveToBottomOf(game.a);
		assertCards(game.a, new int[]{ 0, 1, 2, 2, 3, 3, 4, 4 });
		assertCards(game.d, new int[]{ });
	}
	
	private void assertCards(CardZone<Card<CardModel>> zone, int[] models) {
		SimpleGame game = (SimpleGame) zone.getGame();
		assertEquals("Incorrect zone size", models.length, zone.size());
		for (int i = 0; i < zone.size(); i++) {
			Card<CardModel> card = zone.cardList().get(i);
			assertEquals("Real zone is: " + zone.cardList(), game.model[models[i]].getName(), card.getModel().getName());
		}
	}
	
	@Test
	public void uselessAIHandler() {
		List<Card<?>> list = game.getUseableCards(game.getCurrentPlayer());
		assertTrue("Available cards: " + list, list.isEmpty());
		game.a.createCardOnBottom(game.model[0]);
		assertFalse(game.click(game.a.getBottomCard()));
	}
	
	@Test
	public void testResource() {
		ResourceType type = new ResourceType("Test Resource", 42);
		ResourceMap map = new ResourceMap(true);
		assertEquals(42, map.getResources(type));
		map.set(type, 15);
		assertEquals(15, map.getResources(type));
	}
	
	@Override
	protected void onBefore() {
		game = new SimpleGame();
		game.setRandomSeed(42);
		game.startGame();
	}
	
	
}
