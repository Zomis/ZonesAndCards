package net.zomis.cards.classics;

import java.util.Arrays;

public enum AceValue {
	LOW(ClassicCard.RANK_ACE_LOW), HIGH(ClassicCard.RANK_ACE_HIGH);
	
	private final int aceValue;
	private final int minRank;
	private final int maxRank;
	private final int[] ranks;

	private AceValue(int value) {
		this.aceValue = value;
		this.minRank = Math.min(2, getAceValue());
		this.maxRank = Math.max(ClassicCard.RANK_KING, getAceValue());
		this.ranks = new int[52 / 4];
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

	public int[] getRanks() {
		return Arrays.copyOf(this.ranks, this.ranks.length);
	}
}
