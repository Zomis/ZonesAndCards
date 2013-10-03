package net.zomis.cards.classics;

import java.util.Comparator;

import net.zomis.cards.classics.ClassicGame.AceValue;
import net.zomis.cards.model.CardModel;

public class ClassicCard extends CardModel {
	
	public static class Compare implements Comparator<ClassicCard> {
		private boolean	suiteFirst;
		private AceValue aceValue;
		public Compare(boolean suiteFirst, AceValue aceValue) {
			this.suiteFirst = suiteFirst;
			this.aceValue = aceValue;
		}
		@Override
		public int compare(ClassicCard o1, ClassicCard o2) {
			return o1.getValue(suiteFirst, aceValue) - o1.getValue(suiteFirst, aceValue);
		}
	}

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

	public int getValue(boolean suiteFirst, AceValue aceValue) {
		int suitValue = (suiteFirst ? this.getSuite().ordinal() * 14 : getSuite().ordinal());
		int rankValue = (suiteFirst ? this.getRank() : this.getRank() * 4);
		return suitValue + rankValue;
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
