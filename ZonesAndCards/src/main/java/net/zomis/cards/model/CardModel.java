package net.zomis.cards.model;

public class CardModel implements Comparable<CardModel> {

	private final String name;
	
	public CardModel(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	protected <E extends CardModel> Card<E> createCardInternal(CardZone<?> initialZone) {
		@SuppressWarnings("unchecked")
		Card<E> card = new Card<E>((E) this); // TODO: Possibly create the cards themselves elsewhere...
		card.currentZone = initialZone;
		return card;
	}

	@Override
	public int compareTo(CardModel o) {
		if (name == null || o.name == null)
			throw new NullPointerException("Name cannot be null");
		return name.compareTo(o.getName());
	}
	@Override
	public String toString() {
		return this.getName();
	}
	
	@Override
	public int hashCode() {
		return this.getName().hashCode();
	}
}
