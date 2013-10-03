package net.zomis.cards.model;

public abstract class CardType {
	private final int id;
	
	public CardType(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public abstract String getName();
}
