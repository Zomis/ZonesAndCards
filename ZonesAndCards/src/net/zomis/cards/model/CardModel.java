package net.zomis.cards.model;

public class CardModel implements Comparable<CardModel> {

	private ZoneChangeListener onZoneChange;

	private String name;
	
	public CardModel(String name) {
		this.name = name;
	}
	
	public CardModel setOnZoneChange(ZoneChangeListener onZoneChange) {
		this.onZoneChange = onZoneChange;
		return this;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public ZoneChangeListener getOnZoneChange() {
		return onZoneChange;
	}
	
	public Card createCard() {
		return new Card(this);
	}

	@Override
	public int compareTo(CardModel o) {
		return name.compareTo(o.getName());
	}
	@Override
	public String toString() {
		return this.getName();
	}
}
