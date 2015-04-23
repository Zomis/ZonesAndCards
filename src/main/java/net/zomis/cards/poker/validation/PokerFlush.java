package net.zomis.cards.poker.validation;

import java.util.ArrayList;
import java.util.List;

import net.zomis.cards.classics.AceValue;
import net.zomis.cards.classics.Suite;

/**
 * Checks for FLUSH, ROYAL_FLUSH and STRAIGHT_FLUSH. Depends on {@link PokerStraight} for the straight analyze.
 */
public class PokerFlush implements PokerHandResultProducer {

	private final PokerHandResultProducer straight = new PokerStraight();
	
	@Override
	public PokerHandResult resultFor(PokerHandAnalyze analyze) {
		List<PokerHandResult> results = new ArrayList<PokerHandResult>();
		for (Suite suite : Suite.values()) {
			if (suite.isWildcard())
				continue;

			PokerHandAnalyze suiteHand = analyze.filterBySuite(suite);
			if (suiteHand.size() < HAND_SIZE)
				continue; // Not enough cards to make a complete hand

			// We have a complete hand, now let's create a HandResult for it.
			PokerHandResult straightResult = straight.resultFor(suiteHand);
			if (straightResult != null) {
				PokerHandType type = straightResult.getPrimaryRank() == AceValue.HIGH.getAceValue() ? PokerHandType.ROYAL_FLUSH : PokerHandType.STRAIGHT_FLUSH;
				results.add(new PokerHandResult(type, straightResult.getPrimaryRank(), 0, null)); // We have a straight so we don't need to provide any kickers.
			}
			else results.add(new PokerHandResult(PokerHandType.FLUSH, 0, 0, suiteHand.getCards()));
		}
		if (results.isEmpty())
			return null;
		
		return PokerHandResult.returnBest(results);
	}

}
