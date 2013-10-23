package test.net.zomis.cards;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.util.ResourceMap;
import net.zomis.cards.util.ResourceType;
import net.zomis.custommap.CustomFacade;

import org.junit.Test;


public class SimpleGameTest extends CardsTest<SimpleGame> {

	@Test
	public void probability() {
		assertEquals(1,  fact(0));
		assertEquals(1,  fact(1));
		assertEquals(2,  fact(2));
		assertEquals(6,  fact(3));
		assertEquals(24, fact(4));
		
		game.a.createCardOnBottom(game.model[1]);
		game.a.createCardOnBottom(game.model[2]);
		
		// game.b should not have any 0.
		// game.d should not have any 3.
		// game.c can have anything.
		// what is the probabilities of 3s in game.b?
		// Preliminary Expected: 1/7 for 0. 2/7 for 1. 4/7 for 2.
		
		game.global.createCardOnBottom(game.model[0]);
		game.global.createCardOnBottom(game.model[0]);
		game.global.createCardOnBottom(game.model[1]);
		game.global.createCardOnBottom(game.model[2]);
		game.global.createCardOnBottom(game.model[3]);
		game.global.createCardOnBottom(game.model[3]);
		
		List<String> permutations = new ArrayList<String>(720);
		for (int i = 0; i < 720; i++) {
			List<Integer> numbers = new ArrayList<Integer>();
			for (int xx = 0; xx < 6; xx++)
				numbers.add(xx);
			
			List<Integer> list = listFromI(numbers, i);
			String at = "";
			for (int value : list) {
				assertEquals("Error producing list for " + i + ": " + list, -1, at.indexOf(String.valueOf(value)));
				at = at + value;
			}
			
			assertFalse("Contains " + permutations + " when adding " + at + " for i " + i, permutations.contains(at));
			permutations.add(at);
		}
//		CustomFacade.getLog().i(permutations.toString());
		
		int count[] = new int[3];
		for (String str : permutations) {
			if (forbidden(str, 0, 2, '0')) continue;
			if (forbidden(str, 0, 2, '1')) continue;
			if (forbidden(str, 4, 2, '4')) continue;
			if (forbidden(str, 4, 2, '5')) continue;
			// string is yyzzxx
			// chars is 01=a, 23=bc, 45=d
			int cnt = 0;
			if (str.charAt(0) == '4') cnt++;
			if (str.charAt(1) == '4') cnt++;
			if (str.charAt(0) == '5') cnt++;
			if (str.charAt(1) == '5') cnt++;
			count[cnt]++;
			
//			str = str.replace('0', 'a');
//			str = str.replace('1', 'a');
//			str = str.replace('2', 'b');
//			str = str.replace('3', 'c');
//			str = str.replace('4', 'd');
//			str = str.replace('5', 'd');
			
//			CustomFacade.getLog().i(str + " was ok and returned " + cnt + " making the array now " + Arrays.toString(count));
		}
		CustomFacade.getLog().i("Count results: " + Arrays.toString(count));
		
		/* Each zone has size 2
		 * 
		 * zone y = 0a
		 * zone x = 0d
		 * 
		 * 
		 * 
		 * 
		 * */
		
	}
	
	private boolean forbidden(String str, int fromIndex, int countIndexes, char c) {
		for (int i = fromIndex; i < fromIndex + countIndexes; i++) {
			if (str.charAt(i) == c) {
//				CustomFacade.getLog().i(str + " is forbidden because of " + i + " = " + c);
				return true;
			}
		}
		return false;
	}

	private List<Integer> listFromI(List<Integer> numbers, int iteration) {
		if (numbers.size() == 0)
			return new ArrayList<Integer>();
		
//		1-120, 121-240, 241-360...
		List<Integer> list = new ArrayList<Integer>(numbers.size());
		int divisor = fact(numbers.size() - 1);
		int pos = iteration / divisor;
		list.add(numbers.remove((int)pos));
		list.addAll(listFromI(numbers, iteration % divisor));
		
//		for (int xx = 1; xx <= 6; xx++) {
//			int value = i / (720 / 6); // / intPow(6, xx)
//			list.add(value);
//			i = i - value;
//		}
		return list;
	}
	private int fact(int x) {
		if (x < 0) throw new IllegalArgumentException("x cannot be negative: " + x);
		if (x <= 1) return 1;
		return x * fact(x - 1);
	}

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
		
		game.d.sort(new Comparator<Card>() {
			@Override
			public int compare(Card c1, Card c2) {
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
	}
	
	private void assertCards(CardZone zone, int[] models) {
		SimpleGame game = (SimpleGame) zone.getGame();
		assertEquals(models.length, zone.size());
		for (int i = 0; i < zone.size(); i++) {
			Card card = zone.cardList().get(i);
			assertEquals("Real zone is: " + zone.cardList(), game.model[models[i]].getName(), card.getModel().getName());
		}
	}
	
	@Test
	public void uselessAIHandler() {
		assertEquals(0, game.getActionHandler().getAvailableActions(game.getCurrentPlayer()).size());
		game.a.createCardOnBottom(game.model[0]);
		assertFalse(game.getActionHandler().click(game.a.getBottomCard()).actionIsAllowed());
	}
	@Test
	public void test() {
		ResourceType type = new ResourceType("Test Resource", 42);
		ResourceMap map = new ResourceMap();
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
