package net.zomis.cards.model;

public class CardModel implements Comparable<CardModel> {

	private String name;
	
	public CardModel(String name) {
		this.setName(name);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	protected Card createCardInternal(CardZone initialZone) {
		Card card = new Card(this);
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
