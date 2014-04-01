package net.zomis.cards.hstone.factory;

import net.zomis.cards.hstone.HStoneClass;
import net.zomis.cards.util.DeckList;

public class HStoneChar {

	private String name;
	private HStoneClass	charClass;
	private DeckList deck;

	public HStoneChar(String name, HStoneClass charClass, DeckList deck) {
		this.name = name;
		this.charClass = charClass;
		this.deck = deck;
	}
	
	public String getName() {
		return name;
	}
	
	public DeckList getDeck() {
		return deck;
	}
	
	public HStoneClass getCharClass() {
		return charClass;
	}
	
}
