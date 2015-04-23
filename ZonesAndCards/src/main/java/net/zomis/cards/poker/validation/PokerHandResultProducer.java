package net.zomis.cards.poker.validation;

/**
 * Interface for scanning for Poker hands.
 */
public interface PokerHandResultProducer {
	/**
	 * Constant for how big our hands should be.
	 */
	final int HAND_SIZE = 5;
	/**
	 * Method which does the job of finding a matching Poker hand for some analyze data.
	 * @param analyze {@link PokerHandAnalyze} object containing data for which we should try to find a matching Poker hand.
	 * @return {@link PokerHandResult} for the best poker hand we could find.
	 */
	PokerHandResult resultFor(PokerHandAnalyze analyze);
}
