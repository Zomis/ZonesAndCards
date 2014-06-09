package net.zomis.cards.classics;

import net.zomis.cards.model.CardModel;

public class ClassicCard extends CardModel {

	private final Suite suite;
	private final int rank;
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + rank;
		result = prime * result + ((suite == null) ? 0 : suite.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassicCard other = (ClassicCard) obj;
		if (rank != other.rank)
			return false;
		if (suite != other.suite)
			return false;
		return true;
	}
	public static final int RANK_ACE_LOW = 1;
	public static final int RANK_2 = 2;
	public static final int RANK_3 = 3;
	public static final int RANK_4 = 4;
	public static final int RANK_5 = 5;
	public static final int RANK_6 = 6;
	public static final int RANK_7 = 7;
	public static final int RANK_8 = 8;
	public static final int RANK_9 = 9;
	public static final int RANK_10 = 10;
	public static final int RANK_JACK = 11;
	public static final int RANK_QUEEN = 12;
	public static final int RANK_KING = 13;
	public static final int RANK_ACE_HIGH = 14;
	public static final int	RANK_WILDCARD = 20;
	
	public ClassicCard(Suite suite, int rank) {
		super(rank + "-" + suite);
		if (suite == null)
			throw new NullPointerException("Suite cannot be null");
		if (!suite.isWildcard() && rank == RANK_WILDCARD)
			throw new IllegalArgumentException("Rank cannot be RANK_WILDCARD when suite is " + suite);
		this.suite = suite;
		this.rank = rank;
	}

	public int getRank() {
		return rank;
	}
	public int getRankWithAceValue(AceValue aceValue) {
		if (isAce())
			return aceValue.getAceValue();
		return rank;
	}
	public boolean isAce() {
		return this.rank == RANK_ACE_LOW || this.rank == RANK_ACE_HIGH;
	}
	public boolean isWildcard() {
		return suite.isWildcard();
	}
	public Suite getSuite() {
		return suite;
	}
}
