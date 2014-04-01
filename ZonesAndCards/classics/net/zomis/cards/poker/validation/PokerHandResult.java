package net.zomis.cards.poker.validation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.zomis.cards.classics.AceValue;
import net.zomis.cards.classics.ClassicCard;

/**
 * Data for a found poker hand. Provides data for the type of poker hand, the primary rank and secondary rank, and kickers. Including methods for sorting. Also implements hashCode and equals.
 */
public class PokerHandResult implements Comparable<PokerHandResult> {
	private final PokerHandType type;
	private final int primaryRank;
	private final int secondaryRank;
	private final int[]	kickers;
	
	public PokerHandResult(PokerHandType type, int primaryRank, int secondaryRank, ClassicCard[] cards) {
		this(type, primaryRank, secondaryRank, cards, PokerHandResultProducer.HAND_SIZE);
	}
	public PokerHandResult(PokerHandType type, int primaryRank, int secondaryRank, ClassicCard[] cards, int numKickers) {
		this.type = type;
		this.primaryRank = primaryRank;
		this.secondaryRank = secondaryRank;
		this.kickers = kickers(cards, new int[]{ primaryRank, secondaryRank }, numKickers);
		Arrays.sort(this.kickers);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + primaryRank;
		result = prime * result + secondaryRank;
		result = prime * result + Arrays.hashCode(kickers);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof PokerHandResult))
			return false;
		PokerHandResult other = (PokerHandResult) obj;
		if (type != other.type)
			return false;
		if (primaryRank != other.primaryRank)
			return false;
		if (secondaryRank != other.secondaryRank)
			return false;
		if (!Arrays.equals(kickers, other.kickers))
			return false;
		return true;
	}

	private static int compareKickers(int[] sorted1, int[] sorted2) {
		int index1 = sorted1.length - 1;
		int index2 = sorted2.length - 1;
		int compare = 0;
		
		while (compare == 0 && index1 >= 0 && index2 >= 0) {
			// If one of them is bigger than another we will stop comparing, so decreasing both indexes is perfectly OK.
			compare = Integer.compare(sorted1[index1--], sorted2[index2--]);
		}
		return compare;
	}
	
	@Override
	public int compareTo(PokerHandResult other) {
		// compare order: HandType, primary rank (int), secondary (used for two pair and full house), kickers
		int compare = this.type.compareTo(other.type);
		if (compare == 0)
			compare = Integer.compare(this.primaryRank, other.primaryRank);
		if (compare == 0)
			compare = Integer.compare(this.secondaryRank, other.secondaryRank);
		if (compare == 0)
			compare = compareKickers(this.kickers, other.kickers);
		return compare;
	}
	public PokerHandType getType() {
		return type;
	}
	/**
	 * Return the best {@link PokerHandResult} of a list of results. The method first orders the list and then returns the last result.
	 * @param results A list of PokerHandResults
	 * @return The best result from the list
	 */
	public static PokerHandResult returnBest(List<PokerHandResult> results) {
		if (results.isEmpty())
			return null;
		Collections.sort(results);
		return results.get(results.size() - 1);
	}

	/**
	 * Create an integer array of "kickers", to separate FOUR_OF_A_KIND with Ace-kicker vs. King-kicker
	 * @param cards The cards in your hand. If null, an empty array will be returned
	 * @param skip Ranks that will be skipped (for example, if you have a pair of 4s then you can skip those 4s)
	 * @param count How many kickers that should be included. This should ideally be 5 - number of cards required for the {@link PokerHandType} the kickers are provided for
	 * @return An array of the ranks that will be used as kickers. Wildcards and the ranks in the skip array are excluded
	 */
	private static int[] kickers(ClassicCard[] cards, int[] skip, int count) {
		if (cards == null)
			return new int[]{};
		int[] result = new int[cards.length];
		Arrays.sort(skip);
		for (int i = 0; i < cards.length; i++) {
			int rank = cards[i].getRankWithAceValue(AceValue.HIGH);
			// Check if we should skip this rank in the kicker-data.
			if (cards[i].isWildcard() || Arrays.binarySearch(skip, rank) >= 0)
				continue;
			result[i] = rank;
		}
		Arrays.sort(result);
		return Arrays.copyOfRange(result, Math.max(result.length - count, 0), result.length);
	}
	
	public int getPrimaryRank() {
		return primaryRank;
	}
	
	public int getSecondaryRank() {
		return secondaryRank;
	}
	
	@Override
	public String toString() {
		return String.format("PokerHand: %s. %d, %d. Kickers: %s", type, primaryRank, secondaryRank, Arrays.toString(kickers));
	}
}
