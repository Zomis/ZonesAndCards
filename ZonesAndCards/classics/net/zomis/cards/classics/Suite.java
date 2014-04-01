package net.zomis.cards.classics;


public enum Suite {

	SPADES, HEARTS, DIAMONDS, CLUBS, EXTRA;
	
	public boolean isBlack() {
		return this == SPADES || this == CLUBS;
	}
	public boolean isWildcard() {
		return this == EXTRA;
	}
	public boolean isRed() {
		return !isBlack() && !isWildcard();
	}
	public static int suiteCount(boolean includingWildcards) {
		return Suite.values().length - (includingWildcards ? 0 : 1);
	}
}
