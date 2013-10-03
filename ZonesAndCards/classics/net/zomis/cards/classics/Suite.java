package net.zomis.cards.classics;

public enum Suite {

	SPADES, HEARTS, DIAMONDS, CLUBS, EXTRA;
	
	public boolean isBlack() {
		return this.ordinal() % 2 == 0 && !isWildcard();
	}
	public boolean isWildcard() {
		return this == EXTRA;
	}
	public boolean isRed() {
		return !isBlack() && !isWildcard();
	}
	public static int suiteCount(boolean includingWildcards) {
		int i = 0;
		for (Suite suite : Suite.values()) {
			if (!suite.isWildcard() || includingWildcards) {
				++i;
			}
		}
		return i;
	}
	
}
