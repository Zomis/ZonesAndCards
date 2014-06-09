package net.zomis.cards.analyze2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CardGroup<C> {

	private final List<C>	cards;

	public CardGroup(Collection<C> cards) {
		this.cards = new ArrayList<>(cards);
		
	}
	
	public Collection<C> getCards() {
		return cards;
	}
	
	@Override
	public String toString() {
		return "CG:" + this.cards; // + "@" + Integer.toString(this.hashCode(), 16);
	}
	
	ListSplit<C> splitCheck(CardGroup<C> group) {
		return ListSplit.split(this.cards, group.cards);
	}

	public boolean isEmpty() {
		return cards.isEmpty();
	}
	
	@Override
	public int hashCode() {
		return cards.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CardGroup))
			return false;
		CardGroup<?> other = (CardGroup<?>) obj;
		
		return other.cards.equals(this.cards);
	}
	
}
