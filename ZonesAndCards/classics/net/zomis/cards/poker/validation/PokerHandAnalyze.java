package net.zomis.cards.poker.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.zomis.cards.classics.AceValue;
import net.zomis.cards.classics.ClassicCard;
import net.zomis.cards.classics.Suite;

/**
 * A helper class to analyze ranks and suits for an array of {@link ClassicCard}s. Create new using the static method {@link #analyze(ClassicCard...)}
 */
public class PokerHandAnalyze {
	
	private final int[] ranks = new int[ClassicCard.RANK_ACE_HIGH];
	private final ClassicCard[] cards;
	private final int wildcards;

	/**
	 * Create a new instance and analyze the provided cards
	 * @param cards The cards to analyze
	 * @return Organized analyze of the provided cards
	 */
	public PokerHandAnalyze(ClassicCard... cards2) {
		this.cards = Arrays.copyOf(cards2, cards2.length);
		int wildcards = 0;
		for (ClassicCard card : cards) {
			if (card.isWildcard()) {
				wildcards++;
			}
			else if (card.isAce()) {
				ranks[AceValue.HIGH.getAceValue() - 1]++;
				ranks[AceValue.LOW.getAceValue() - 1]++;
			}
			else ranks[card.getRank() - 1]++;
		}
		this.wildcards = wildcards;
	}
	
	public int[] getRanks() {
		return Arrays.copyOf(ranks, ranks.length);
	}
	public int getWildcards() {
		return wildcards;
	}
	public ClassicCard[] getCards() {
		return Arrays.copyOf(cards, cards.length);
	}
	public int size() {
		return cards.length;
	}
	/**
	 * Create a sub-analyze which only includes wildcards and the specified suite. Useful to check for the FLUSH {@link PokerHandType}
	 * @param suite The suite to filter by
	 * @return A new analyze object
	 */
	public PokerHandAnalyze filterBySuite(Suite suite) {
		List<ClassicCard> cards = new ArrayList<ClassicCard>();
		for (ClassicCard card : this.cards) {
			if (card.isWildcard() || card.getSuite().equals(suite)) {
				cards.add(card);
			}
		}
		return new PokerHandAnalyze(cards.toArray(new ClassicCard[cards.size()]));
	}
}