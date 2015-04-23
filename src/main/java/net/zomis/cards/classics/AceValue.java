package net.zomis.cards.classics;


public enum AceValue {
	LOW(ClassicCard.RANK_ACE_LOW), HIGH(ClassicCard.RANK_ACE_HIGH);
	
	private final int aceValue;
	private final int minRank;
	private final int maxRank;
	private final int[] ranks;

	private AceValue(int value) {
		this.aceValue = value;
		this.minRank = Math.min(ClassicCard.RANK_2, getAceValue());
		this.maxRank = Math.max(ClassicCard.RANK_KING, getAceValue());
		this.ranks = new int[ClassicCard.RANK_ACE_HIGH - ClassicCard.RANK_ACE_LOW];
		for (int i = 0; i < ranks.length; i++)
			ranks[i] = this.minRank + i;
	}
	
	public int getMaxRank() {
		return maxRank;
	}
	public int getMinRank() {
		return minRank;
	}
	public int getAceValue() {
		return this.aceValue;
	}
	/**
	 * @return An array of all the available ranks in the game
	 */
	public int[] getRanks() {
		int[] copy = new int[ranks.length];
		for (int i = 0; i < copy.length; i++) {
			copy[i] = ranks[i];
		}
		return copy;
		
//		return Arrays.copyOf(this.ranks, this.ranks.length);
	}
}
