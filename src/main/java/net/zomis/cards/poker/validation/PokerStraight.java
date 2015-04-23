package net.zomis.cards.poker.validation;

/**
 * Checks for a normal STRAIGHT. Returns null if no straight was found
 */
public class PokerStraight implements PokerHandResultProducer {

	/**
	 * Scans an array of ints to search for sequence of 1s. Can fill in gaps by using wildcards
	 * @param ranks An array of the ranks provided by {@link PokerHandAnalyze}
	 * @param wildcards Number of usable wildcards to fill gaps for the straight
	 * @return The highest rank (index + 1) for which the straight started
	 */
	public static int findHighestIndexForStraight(int[] ranks, int wildcards) {
		return findHighestIndexForStraight(ranks, ranks.length - 1, wildcards);
	}
	
	private static int findHighestIndexForStraight(int[] ranks, int startIndex, int wildcards) {
		int wildsLeft = wildcards;
		for (int i = startIndex; i >= 0; i--) {
			int count = ranks[i];
			if (count > 0) {
				// do nothing
			}
			else if (wildsLeft > 0) {
				wildsLeft--;
			}
			else {
				return findHighestIndexForStraight(ranks, startIndex - 1, wildcards);
			}
			if (startIndex - i + 1 >= PokerHandResultProducer.HAND_SIZE)
				return startIndex + 1;
		}
		return -1;
	}
	
	@Override
	public PokerHandResult resultFor(PokerHandAnalyze analyze) {
		int straight = findHighestIndexForStraight(analyze.getRanks(), analyze.getWildcards());
		if (straight != -1)
			return new PokerHandResult(PokerHandType.STRAIGHT, straight, 0, null); // We don't need to provide any kickers since we have a straight of 5 cards.
		
		return null;
	}

}
