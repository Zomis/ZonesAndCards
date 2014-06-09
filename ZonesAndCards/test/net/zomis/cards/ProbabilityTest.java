package net.zomis.cards;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.ToIntFunction;

import net.zomis.custommap.CustomFacade;
import net.zomis.utils.ZomisList.FilterInterface;
import net.zomis.utils.ZomisUtils;

import org.junit.Test;

public class ProbabilityTest extends CardsTest<SimpleGame> {
	@Test
	public void fds() {
		assertEquals(2, ZomisUtils.deckCombos(1, 2, 3));
		assertEquals(3, ZomisUtils.deckCombos(2, 2, 3));
		assertEquals(4, ZomisUtils.deckCombos(3, 2, 3));
		assertEquals(3, ZomisUtils.deckCombos(4, 2, 3));
		assertEquals(12, ZomisUtils.deckCombos(4, 3, 3));

		assertEquals(3, ZomisUtils.deckCombos(5, 3, 2));
		assertEquals(1, ZomisUtils.deckCombos(4, 2, 2));
		assertEquals(3, ZomisUtils.deckCombos(2, 2, 2));

		// Not sure about the result here:
		assertEquals(2930455, ZomisUtils.deckCombos(20, 10, 5));
		assertEquals(651    , ZomisUtils.deckCombos(10, 5, 5));
	}
	
	@Test
	public void fact() {
		assertEquals(1,  fact(0));
		assertEquals(1,  fact(1));
		assertEquals(2,  fact(2));
		assertEquals(6,  fact(3));
		assertEquals(24, fact(4));
		assertEquals(120, fact(5));
		assertEquals(720, fact(6));
	}
	
	private static class StringForbid implements FilterInterface<String> {
		private int	lowIndex;
		private int	count;
		private String	forbidden;
		public StringForbid(int lowIndex, int count, String forbidChars) {
			this.lowIndex = lowIndex;
			this.count = count;
			this.forbidden = forbidChars;
		}
		@Override
		public boolean shouldKeep(String obj) {
			for (int i = 0; i < forbidden.length(); ++i)
				if (forbidden(obj, lowIndex, count, forbidden.charAt(i))) return false;
			return true;
		}
	}
	
	@Test
	public void probability2() {
		List<String> permutations = produceList(6);
//		countResults(permutations, new int[]{ 0, 1, 2 }, "45", new StringForbid[]{ new StringForbid(0, 3, "01"), new StringForbid(3, 2, "23") });
		
	}
	
	@Test
	public void permutList() {
		List<Character> indexes = new ArrayList<Character>();
		indexes.add('a');
		indexes.add('b');
		indexes.add('c');
		indexes.add('d');
		int totalPermutations = fact(indexes.size()); // This is the number of possible permutations
		List<List<Character>> results = new ArrayList<List<Character>>(totalPermutations);
		for (int i = 0; i < totalPermutations; i++) {
			// Since permutationI modifies the list we send, we copy the list
			List<Character> newList = permutationI(new ArrayList<Character>(indexes), i);
			results.add(newList);
//			System.out.println(newList);
		}
		assertEquals(24, results.size());
		assertEquals(totalPermutations, results.size());
	}
	
	public String charListToString(List<Character> list) {
		String result = "";
		for (Character ch : list) {
			result += ch;
		}
		return result;
	}
	
	@Test
	public void probability() {
		game.a.createCardOnBottom(game.model[1]);
		game.a.createCardOnBottom(game.model[2]);
		
		// game.b should not have any 0.
		// game.d should not have any 3.
		// game.c can have anything.
		// what is the probabilities of 3s in game.b?
		
		game.global.createCardOnBottom(game.model[0]);
		game.global.createCardOnBottom(game.model[0]);
		game.global.createCardOnBottom(game.model[1]);
		game.global.createCardOnBottom(game.model[2]);
		game.global.createCardOnBottom(game.model[3]);
		game.global.createCardOnBottom(game.model[3]);
		
		List<String> permutations = produceList(game.global.size());
		countResults(permutations, new int[]{ 0, 1 }, "45", new StringForbid[]{ new StringForbid(0, 2, "01"), new StringForbid(4, 2, "45") });
		
////			str = str.replace('0', 'a');
////			str = str.replace('1', 'a');
////			str = str.replace('2', 'b');
////			str = str.replace('3', 'c');
////			str = str.replace('4', 'd');
////			str = str.replace('5', 'd');
////			CustomFacade.getLog().i(str + " was ok and returned " + cnt + " making the array now " + Arrays.toString(count));
	}
	
	private int[] countResults(List<String> permutations, final int[] is, final String string, StringForbid[] stringForbids) {
		int[] result = countResults(permutations, e -> {
				int count = 0;
				for (int i : is) {
					for (int idx = 0; idx < string.length(); idx++)
						if (string.charAt(idx) == e.charAt(i)) count++;
				}
				return count;
			}, stringForbids);
		CustomFacade.getLog().i("Count results: " + Arrays.toString(result) + " when looked for " + 
			string + " in " + is.length + " indexes. " + stringForbids.length + " Forbiddens.");
		return result;
	}
	private int[] countResults(List<String> permutations, ToIntFunction<String> conversionInterface, StringForbid[] stringForbids) {
		int[] results = new int[permutations.get(0).length()];
		outer:
		for (String str : permutations) {
			for (StringForbid forbid : stringForbids) {
				if (!forbid.shouldKeep(str))
					continue outer;
			}
			
			results[conversionInterface.applyAsInt(str)]++;
		}
		
		return results;
	}
	private List<String> produceList(int factNum) {
		int limit = fact(factNum);
		List<String> permutations = new ArrayList<String>(limit);
		for (int i = 0; i < limit; i++) {
			List<Integer> numbers = new ArrayList<Integer>();
			for (int xx = 0; xx < factNum; xx++)
				numbers.add(xx);
			
			List<Integer> list = permutationI(numbers, i);
			String at = "";
			for (int value : list) {
				assertEquals("Error producing list for " + i + ": " + list, -1, at.indexOf(String.valueOf(value)));
				at = at + value;
			}
			
			assertFalse("Contains " + permutations + " when adding " + at + " for i " + i, permutations.contains(at));
			permutations.add(at);
		}
		return permutations;
	}
	private static boolean forbidden(String str, int fromIndex, int countIndexes, char c) {
		for (int i = fromIndex; i < fromIndex + countIndexes; i++) {
			if (str.charAt(i) == c) {
//				CustomFacade.getLog().i(str + " is forbidden because of " + i + " = " + c);
				return true;
			}
		}
		return false;
	}

	public static <E> List<E> permutationI(List<E> values, int iteration) {
		if (values.size() == 0)
			return new ArrayList<E>();
		List<E> list = new ArrayList<E>(values.size());
		int divisor = fact(values.size() - 1);
		int pos = iteration / divisor;
		list.add(values.remove((int)pos));
		list.addAll(permutationI(values, iteration % divisor));
		return list;
	}
	public static int fact(int x) {
		if (x < 0) throw new IllegalArgumentException("x cannot be negative: " + x);
		if (x <= 1) return 1;
		return x * fact(x - 1);
	}

	@Override
	protected void onBefore() {
		game = new SimpleGame();
		game.setRandomSeed(42);
		game.startGame();
	}

}
