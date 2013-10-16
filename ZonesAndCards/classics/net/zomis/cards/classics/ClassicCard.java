package net.zomis.cards.classics;

import net.zomis.cards.model.CardModel;

public class ClassicCard extends CardModel {

	private final Suite suite;
	private final int rank;
	
	public static final int RANK_ACE_LOW = 1;
	public static final int RANK_JACK = 11;
	public static final int RANK_QUEEN = 12;
	public static final int RANK_KING = 13;
	public static final int RANK_ACE_HIGH = 14;
	public static final int	RANK_WILDCARD = 0;
	
	public ClassicCard(Suite suite, int rank) {
		super(rank + "-" + suite.toString());
		
		this.suite = suite;
		this.rank = rank;
	}

	public int getRank() {
		return rank;
	}
	public Suite getSuite() {
		return suite;
	}
	
	public boolean isRed() {
		return suite.isRed();
	}
	public boolean isBlack() {
		return suite.isBlack();
	}
	public boolean isWildcard() {
		return suite.isWildcard();
	}
	
}
