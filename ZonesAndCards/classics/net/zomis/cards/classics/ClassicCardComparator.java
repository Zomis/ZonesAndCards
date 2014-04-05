package net.zomis.cards.classics;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import net.zomis.cards.model.Card;

public class ClassicCardComparator implements Comparator<Card<ClassicCard>> {

	private List<Suite> suiteOrder;
	private boolean	suiteFirst;

	public ClassicCardComparator(Suite[] suiteOrder, boolean suiteFirst) {
		this.suiteOrder = Arrays.asList(suiteOrder);
		this.suiteFirst = suiteFirst;
	}
	
	@Override
	public int compare(Card<ClassicCard> o1, Card<ClassicCard> o2) {
		return getValue(o1.getModel()) - getValue(o2.getModel());
	}
	
	protected int getValue(ClassicCard card) {
		int suitOrder = this.suiteOrder.indexOf(card.getSuite());
		int suitValue = (suiteFirst ? suitOrder * 14 : card.getSuite().ordinal());
		int rankValue = (suiteFirst ? card.getRank() : card.getRank() * suiteOrder.size());
		return suitValue + rankValue;
	}

}
