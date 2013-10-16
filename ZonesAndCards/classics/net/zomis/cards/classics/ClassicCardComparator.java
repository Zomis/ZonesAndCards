package net.zomis.cards.classics;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import net.zomis.cards.model.Card;

public class ClassicCardComparator implements Comparator<Card> {

	private List<Suite> suiteOrder;
	private boolean	suiteFirst;

	public ClassicCardComparator(Suite[] suiteOrder, boolean suiteFirst) {
		this.suiteOrder = Arrays.asList(suiteOrder);
		this.suiteFirst = suiteFirst;
	}
	
	@Override
	public int compare(Card o1, Card o2) {
		ClassicCard o1m = (ClassicCard) o1.getModel();
		ClassicCard o2m = (ClassicCard) o2.getModel();
		return getValue(o1m) - getValue(o2m);
	}
	
	protected int getValue(ClassicCard card) {
		int suitOrder = this.suiteOrder.indexOf(card.getSuite());
		int suitValue = (suiteFirst ? suitOrder * 14 : card.getSuite().ordinal());
		int rankValue = (suiteFirst ? card.getRank() : card.getRank() * suiteOrder.size());
		return suitValue + rankValue;
	}

}
