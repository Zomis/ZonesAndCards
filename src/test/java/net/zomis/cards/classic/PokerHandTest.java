package net.zomis.cards.classic;

import static net.zomis.cards.classics.ClassicCard.*;
import static net.zomis.cards.classics.Suite.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.zomis.cards.classics.ClassicCard;
import net.zomis.cards.classics.Suite;
import net.zomis.cards.poker.validation.PokerHandAnalyze;
import net.zomis.cards.poker.validation.PokerHandEval;
import net.zomis.cards.poker.validation.PokerHandResult;
import net.zomis.cards.poker.validation.PokerHandType;
import net.zomis.cards.poker.validation.PokerStraight;
import net.zomis.common.Best;

import org.junit.Test;

public class PokerHandTest {
	private static final ClassicCard WILDCARD = new ClassicCard(Suite.EXTRA, ClassicCard.RANK_WILDCARD);
	
	private int findHighestIndexForStraight(int[] ranks, int wildcards) {
//		int res = PokerStraight.findHighestIndexForStraight(ranks, wildcards);
//		return res == -1 ? res : res - 1; // For testing, I want to know the index and not the "rank"
		return findHighestIndexForStraightNew(ranks, wildcards);
	}

	private static final int[][] WILDCARDS = buildWildCards();

	private static int[][] buildWildCards() {
	    // up to 5 wild cards
	    // that is up to 0x1f or 32 values (counting 0).
	    int[] pos = new int[6];
	    int[][] ret = new int[6][];
	    ret[0] = new int[1];  // one value with no bits set.
	    ret[1] = new int[5];  // 5 with 1 bits set
	    ret[2] = new int[10]; // 10 with 2 bits set
	    ret[3] = new int[10]; // 10 with 3 bits set
	    ret[4] = new int[5];  // 5 with 4 bits set.
	    ret[5] = new int[1];  // one value with all bits set.
	    for (int i = 0; i <= 0x1f; i++) {
	        final int bc = Integer.bitCount(i);
	        ret[bc][pos[bc]++] = i;
	    }
	    return ret;
	}



	/**
	 * Scans an array of ints to search for sequence of 1s. Can fill in gaps by using wildcards
	 * @param ranks An array of the ranks provided by {@link PokerHandAnalyze}
	 * @param wildcards Number of usable wildcards to fill gaps for the straight
	 * @return The highest rank (index + 1) for which the straight started
	 */
	public static int findHighestIndexForStraightNew(int[] ranks, int wildcards) {
	    if (wildcards < 0 || wildcards >= WILDCARDS.length) {
	        throw new IllegalArgumentException("Invalid wild-cards " + wildcards);
	    }

	    int[] wildcardoptions = WILDCARDS[wildcards];
	    int hand = 0;
	    for (int r : ranks) {
	        hand <<= 1;
	        hand |= r != 0 ? 1 : 0;
	    }
	    // OK, the `hand` is set up so that the bits represent the hand.
	    // We now shift off the hand to see if we can make a match;
	    int position = ranks.length;
	    while (position-- >= 5) {
	        for (int wcpattern : wildcardoptions) {
	            // Look at our combinations of wild-card options.
	            if (((hand | wcpattern) & 0x1f) == 0x1f) {
	                // return the position if a wild-card match makes 5-in-a-row.
	                return position;
	            }
	        }
	        // shift the hand ....
	        hand >>>= 1;
	    }
	    return -1;
	}	
	
	
	
	@Test
	public void best() {
		Best<PokerHandResult> best = new Best<PokerHandResult>(new PokerHandComp());
		best.add(new PokerHandResult(PokerHandType.PAIR, 2, 0, null));
		best.add(new PokerHandResult(PokerHandType.FULL_HOUSE, 2, 0, null));
		best.add(new PokerHandResult(PokerHandType.FOUR_OF_A_KIND, 2, 0, null));
		best.add(new PokerHandResult(PokerHandType.PAIR, 2, 0, null));
		
		assertEquals(PokerHandType.FOUR_OF_A_KIND, best.getTheBest().getType());
		
		best.add(new PokerHandResult(PokerHandType.FOUR_OF_A_KIND, 3, 0, null));
		
		assertEquals(PokerHandType.FOUR_OF_A_KIND, best.getTheBest().getType());
		
		List<PokerHandResult> list = best.getAllBest();
		assertEquals(2, list.get(0).getPrimaryRank());
		assertEquals(3, list.get(1).getPrimaryRank());
		
		best = new Best<PokerHandResult>();
		best.add(new PokerHandResult(PokerHandType.PAIR, 2, 0, null));
		best.add(new PokerHandResult(PokerHandType.FULL_HOUSE, 2, 0, null));
		best.add(new PokerHandResult(PokerHandType.FOUR_OF_A_KIND, 2, 0, null));
		best.add(new PokerHandResult(PokerHandType.PAIR, 2, 0, null));
		
		assertEquals(PokerHandType.FOUR_OF_A_KIND, best.getTheBest().getType());
		
	}
	
	private static class PokerHandComp implements Comparator<PokerHandResult> {

		@Override
		public int compare(PokerHandResult o1, PokerHandResult o2) {
			return o1.getType().compareTo(o2.getType());
		}
	}
	
	@Test
	public void straights() {
		assertEquals(7,  findHighestIndexForStraight(new int[]{ 0, 0, 0, 1, 1, 4, 4, 2, 0, 0, 0 }, 0));
		assertEquals(7,  findHighestIndexForStraight(new int[]{ 0, 1, 0, 1, 1, 1, 7, 1, 0, 0, 1 }, 0));
		assertEquals(7,  findHighestIndexForStraight(new int[]{ 0, 1, 0, 1, 2, 3, 1, 9, 0, 1, 1 }, 0));
		assertEquals(-1, findHighestIndexForStraight(new int[]{ 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0 }, 1));
		assertEquals(8,  findHighestIndexForStraight(new int[]{ 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 0 }, 1));
		assertEquals(6,  findHighestIndexForStraight(new int[]{ 0, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0 }, 1));
		assertEquals(10, findHighestIndexForStraight(new int[]{ 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1 }, 1));
		assertEquals(4,  findHighestIndexForStraight(new int[]{ 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0 }, 1));
		assertEquals(8,  findHighestIndexForStraight(new int[]{ 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0 }, 1));
		assertEquals(10, findHighestIndexForStraight(new int[]{ 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0 }, 1));
		assertEquals(-1, findHighestIndexForStraight(new int[]{ 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1 }, 0));
	}
	
	@Test
	public void moreCards() {
		PokerHandEval eval = PokerHandEval.defaultEvaluator();
		assertPoker(PokerHandType.THREE_OF_A_KIND, 2, eval.evaluate(card(DIAMONDS, RANK_JACK), card(HEARTS, RANK_ACE_HIGH), card(SPADES, RANK_7),
				card(DIAMONDS, RANK_2), card(HEARTS, RANK_2), card(DIAMONDS, RANK_4), WILDCARD));
		
		assertPoker(PokerHandType.TWO_PAIR, 12, 10, eval.evaluate(card(DIAMONDS, RANK_3), card(SPADES, RANK_2), card(DIAMONDS, RANK_10),
				card(CLUBS, RANK_10), card(CLUBS, RANK_7), card(SPADES, RANK_5), 
				card(SPADES, RANK_QUEEN), card(HEARTS, RANK_QUEEN), 
				card(HEARTS, RANK_ACE_HIGH), card(DIAMONDS, RANK_7)));
		
		PokerHandResult eq1;
		PokerHandResult eq2;
		eq1 = eval.evaluate(card(CLUBS, RANK_10), card(CLUBS, RANK_7), card(SPADES, RANK_KING), 
				card(SPADES, RANK_QUEEN), card(HEARTS, RANK_QUEEN),	card(HEARTS, RANK_ACE_HIGH), card(DIAMONDS, RANK_ACE_HIGH));
		eq2 = eval.evaluate(card(CLUBS, RANK_JACK), card(CLUBS, RANK_7), card(SPADES, RANK_KING), 
				card(SPADES, RANK_QUEEN), card(HEARTS, RANK_QUEEN), card(HEARTS, RANK_ACE_HIGH), card(DIAMONDS, RANK_ACE_HIGH));
		assertEquals(eq1, eq2);
		
		eq1 = eval.evaluate(WILDCARD, card(SPADES, RANK_QUEEN), card(HEARTS, RANK_QUEEN),	card(HEARTS, RANK_7), card(DIAMONDS, RANK_4));
		eq2 = eval.evaluate(card(DIAMONDS, RANK_QUEEN), card(CLUBS, RANK_QUEEN), card(SPADES, RANK_QUEEN),	card(HEARTS, RANK_7), card(DIAMONDS, RANK_4));
		assertPoker(PokerHandType.THREE_OF_A_KIND, RANK_QUEEN, eq1);
		assertEquals(eq2, eq1);
		
		PokerHandResult result;
		result = eval.evaluate(WILDCARD, WILDCARD, WILDCARD, WILDCARD, card(DIAMONDS, RANK_6));
		assertPoker(PokerHandType.STRAIGHT_FLUSH, result);
		
		result = eval.evaluate(WILDCARD, WILDCARD, WILDCARD, card(HEARTS, RANK_10), card(DIAMONDS, RANK_6));
		assertPoker(PokerHandType.FOUR_OF_A_KIND, result);
		
		result = eval.evaluate(WILDCARD, WILDCARD, WILDCARD, WILDCARD, WILDCARD);
		assertPoker(PokerHandType.ROYAL_FLUSH, result);
		
		result = eval.evaluate(WILDCARD, WILDCARD, WILDCARD, WILDCARD, card(SPADES, RANK_10));
		assertPoker(PokerHandType.ROYAL_FLUSH, result);
	}
	public void assertPoker(PokerHandType type, int primary, PokerHandResult test) {
		assertPoker(type, test);
		assertEquals(primary, test.getPrimaryRank());
	}
	public void assertPoker(PokerHandType type, int primary, int secondary, PokerHandResult test) {
		assertPoker(type, primary, test);
		assertEquals(secondary, test.getSecondaryRank());
	}

	@Test
	public void royalAndFlushStraights() {
		PokerHandEval eval = PokerHandEval.defaultEvaluator();
		PokerHandResult result = eval.evaluate(card(HEARTS, RANK_10), card(HEARTS, RANK_JACK), card(HEARTS, RANK_QUEEN), card(HEARTS, RANK_KING), card(HEARTS, RANK_ACE_LOW));
		assertPoker(PokerHandType.ROYAL_FLUSH, result);
		
		result = eval.evaluate(card(HEARTS, RANK_2), card(HEARTS, RANK_3), card(HEARTS, RANK_4), card(HEARTS, RANK_5), card(HEARTS, RANK_6));
		assertPoker(PokerHandType.STRAIGHT_FLUSH, result);
	}
	
	@Test
	public void testStraights() {
		PokerHandEval eval = PokerHandEval.defaultEvaluator();
		PokerHandResult result;
		result = eval.evaluate(card(HEARTS, RANK_2), card(CLUBS, RANK_3), card(HEARTS, RANK_4), card(HEARTS, RANK_5), card(DIAMONDS, RANK_6));
		assertPoker(PokerHandType.STRAIGHT, result);
		
		ClassicCard[] hand = new ClassicCard[]{ card(HEARTS, RANK_2), card(CLUBS, RANK_3), WILDCARD, card(HEARTS, RANK_5), card(DIAMONDS, RANK_6) };
		PokerHandAnalyze analyze = new PokerHandAnalyze(hand);
		assertPoker(PokerHandType.STRAIGHT, new PokerStraight().resultFor(analyze));
		
		result = eval.evaluate(card(HEARTS, RANK_2), card(CLUBS, RANK_3), WILDCARD, card(HEARTS, RANK_5), card(DIAMONDS, RANK_6));
		assertPoker(PokerHandType.STRAIGHT, result);

		result = eval.evaluate(card(HEARTS, RANK_ACE_HIGH), card(CLUBS, RANK_2), card(HEARTS, RANK_3), card(HEARTS, RANK_4), card(DIAMONDS, RANK_5));
		assertPoker(PokerHandType.STRAIGHT, result);

		analyze = new PokerHandAnalyze(card(HEARTS, RANK_2), card(CLUBS, RANK_7), WILDCARD, card(HEARTS, RANK_5), card(DIAMONDS, RANK_6));
		assertPoker(null, new PokerStraight().resultFor(analyze));
	}

	@Test
	public void rankHands() {
		PokerHandEval eval = PokerHandEval.defaultEvaluator();
		PokerHandResult highCard       = eval.evaluate(card(HEARTS, RANK_7), card(CLUBS, RANK_JACK), card(HEARTS, RANK_6), card(HEARTS, RANK_4), card(DIAMONDS, RANK_2));
		
		PokerHandResult pairLowKicker  = eval.evaluate(card(HEARTS, RANK_7), card(CLUBS, RANK_7), card(HEARTS, RANK_6), card(HEARTS, RANK_4), card(HEARTS, RANK_2));
		PokerHandResult pairHighKicker = eval.evaluate(card(HEARTS, RANK_7), card(CLUBS, RANK_7), card(HEARTS, RANK_KING), card(HEARTS, RANK_4), card(HEARTS, RANK_2));
		PokerHandResult pairHigher     = eval.evaluate(card(HEARTS, RANK_KING), card(CLUBS, RANK_KING), card(HEARTS, RANK_6), card(HEARTS, RANK_4), card(HEARTS, RANK_2));
		
		PokerHandResult twoPair        = eval.evaluate(card(HEARTS, RANK_KING), card(CLUBS, RANK_KING), card(HEARTS, RANK_6), card(DIAMONDS, RANK_6), card(HEARTS, RANK_2));
		PokerHandResult threeOfAKind   = eval.evaluate(card(HEARTS, RANK_KING), card(CLUBS, RANK_KING), card(SPADES, RANK_KING), card(HEARTS, RANK_4), card(HEARTS, RANK_2));
		
		PokerHandResult flush      	  = eval.evaluate(card(HEARTS, RANK_7), card(HEARTS, RANK_2), card(HEARTS, RANK_6), card(HEARTS, RANK_9), card(HEARTS, RANK_QUEEN));
		PokerHandResult fourOfAKind    = eval.evaluate(card(HEARTS, RANK_7), card(SPADES, RANK_7), card(DIAMONDS, RANK_7), card(CLUBS, RANK_7), card(HEARTS, RANK_QUEEN));

		PokerHandResult straight       = eval.evaluate(card(HEARTS, RANK_2), card(CLUBS, RANK_3), card(HEARTS, RANK_4), card(HEARTS, RANK_5), card(DIAMONDS, RANK_6));
		PokerHandResult straightWild   = eval.evaluate(card(HEARTS, RANK_2), card(CLUBS, RANK_3), WILDCARD, card(HEARTS, RANK_5), card(DIAMONDS, RANK_6));
		assertEquals(straight, straightWild);
		PokerHandResult straightLow 	  = eval.evaluate(card(HEARTS, RANK_ACE_HIGH), card(CLUBS, RANK_2), card(HEARTS, RANK_3), card(HEARTS, RANK_4), card(DIAMONDS, RANK_5));
		
		PokerHandResult straightFlush  = eval.evaluate(card(HEARTS, RANK_8), card(HEARTS, RANK_9), card(HEARTS, RANK_10), card(HEARTS, RANK_JACK), card(HEARTS, RANK_QUEEN));
		PokerHandResult royalFlush     = eval.evaluate(card(HEARTS, RANK_10), card(HEARTS, RANK_JACK), card(HEARTS, RANK_QUEEN), card(HEARTS, RANK_KING), WILDCARD);
		
		PokerHandResult fullHouse      = eval.evaluate(card(HEARTS, RANK_10), card(CLUBS, RANK_10), WILDCARD, card(HEARTS, RANK_KING), card(HEARTS, RANK_KING));
		assertPoker(PokerHandType.FULL_HOUSE, fullHouse);
		assertEquals(RANK_KING, fullHouse.getPrimaryRank());
		assertEquals(RANK_10, fullHouse.getSecondaryRank());
		
		// Add hands to list
		List<PokerHandResult> results = new ArrayList<PokerHandResult>();
		assertAdd(results, PokerHandType.HIGH_CARD, highCard);
		assertAdd(results, PokerHandType.PAIR, pairLowKicker);
		assertAdd(results, PokerHandType.PAIR, pairHighKicker);
		assertAdd(results, PokerHandType.PAIR, pairHigher);
		assertAdd(results, PokerHandType.TWO_PAIR, twoPair);
		assertAdd(results, PokerHandType.THREE_OF_A_KIND, threeOfAKind);
		assertAdd(results, PokerHandType.FLUSH, flush);
		assertAdd(results, PokerHandType.FOUR_OF_A_KIND, fourOfAKind);
		assertAdd(results, PokerHandType.STRAIGHT, straightLow);
		assertAdd(results, PokerHandType.STRAIGHT, straight);
		assertAdd(results, PokerHandType.STRAIGHT, straightWild);
		assertAdd(results, PokerHandType.STRAIGHT_FLUSH, straightFlush);
		assertAdd(results, PokerHandType.ROYAL_FLUSH, royalFlush);

		// Shuffle just for the fun of it
		Collections.shuffle(results);
		
		// Sort the list according to the HandResult comparable interface
		Collections.sort(results);
		
		// Assert the list
		Iterator<PokerHandResult> it = results.iterator();
		assertEquals(highCard, it.next());
		assertEquals(pairLowKicker, it.next());
		assertEquals(pairHighKicker, it.next());
		assertEquals(pairHigher, it.next());
		
		assertEquals(twoPair, it.next());
		assertEquals(threeOfAKind, it.next());
		
		assertEquals(straightLow, it.next());
		
		assertEquals(straight, it.next());
		assertEquals(straightWild, it.next());
		
		assertEquals(flush, it.next());
		assertEquals(fourOfAKind, it.next());
		assertEquals(straightFlush, it.next());
		assertEquals(royalFlush, it.next());
		
		
		// Make sure that we have processed the entire list
		assertFalse("List is not completely processed", it.hasNext()); 
	}

	@Test
	public void cannotHaveFullHouseOnlyAces() {
        PokerHandEval eval = PokerHandEval.defaultEvaluator();

        assertEquals(PokerHandType.THREE_OF_A_KIND, eval.evaluate(
            card(Suite.CLUBS, ClassicCard.RANK_ACE_HIGH),
            card(Suite.HEARTS, ClassicCard.RANK_5),
            card(Suite.SPADES, ClassicCard.RANK_KING),
            card(Suite.DIAMONDS, ClassicCard.RANK_ACE_HIGH),
            card(Suite.SPADES, ClassicCard.RANK_ACE_HIGH)
        ).getType());
        assertEquals(PokerHandType.THREE_OF_A_KIND, eval.evaluate(
            card(Suite.CLUBS, ClassicCard.RANK_ACE_HIGH),
            card(Suite.SPADES, ClassicCard.RANK_ACE_HIGH),
            card(Suite.SPADES, ClassicCard.RANK_KING),
            card(Suite.HEARTS, ClassicCard.RANK_8),
            card(Suite.HEARTS, ClassicCard.RANK_5),
            card(Suite.SPADES, ClassicCard.RANK_2),
            card(Suite.EXTRA, ClassicCard.RANK_WILDCARD)
        ).getType());
        assertEquals(PokerHandType.THREE_OF_A_KIND, eval.evaluate(
            card(Suite.CLUBS, ClassicCard.RANK_ACE_HIGH),
            card(Suite.SPADES, ClassicCard.RANK_ACE_HIGH),
            card(Suite.EXTRA, ClassicCard.RANK_WILDCARD)
        ).getType());
        assertEquals(PokerHandType.PAIR, eval.evaluate(
            card(Suite.CLUBS, ClassicCard.RANK_ACE_HIGH),
            card(Suite.SPADES, ClassicCard.RANK_KING),
            card(Suite.HEARTS, ClassicCard.RANK_7),
            card(Suite.HEARTS, ClassicCard.RANK_5),
            card(Suite.SPADES, ClassicCard.RANK_ACE_HIGH)
        ).getType());

        assertEquals(PokerHandType.PAIR, eval.evaluate(
            card(Suite.CLUBS, ClassicCard.RANK_ACE_HIGH),
            card(Suite.DIAMONDS, ClassicCard.RANK_ACE_HIGH)
        ).getType());
    }

	private static void assertAdd(List<PokerHandResult> results, PokerHandType type, PokerHandResult result) {
		assertPoker(type, result);
		results.add(result);
	}

	private static void assertPoker(PokerHandType type, PokerHandResult result) {
		if (type == null) {
			assertNull(result);
			return;
		}
		assertNotNull("Expected " + type, result);
		assertEquals(result.toString(), type, result.getType());
	}


	private static ClassicCard card(Suite suite, int rank) {
		return new ClassicCard(suite, rank);
	}
	
}
