package net.zomis.cards.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

public class CardZone implements Comparable<CardZone> {

	@JsonBackReference
	CardGame game;
	
	private final Map<Player, Boolean> known = new HashMap<>();

	private boolean	knownGlobal;

	@JsonManagedReference
	private final LinkedList<Card> cards = new LinkedList<>();

	private final String name;

	@Deprecated
	public void remove() {
		this.game.removeZone(this);
	}
	
	public CardZone(String zoneName) {
		this.name = zoneName;
	}
	
	public void setGloballyKnown(boolean knowledge) {
		this.knownGlobal = knowledge;
	}
	public void setKnown(Player player, boolean knowledge) {
		this.known.put(player, knowledge);
	}
	
	public boolean isKnown(Player player) {
		Boolean b = known.get(player);
		return (b == null ? knownGlobal : b);
	}

	public void add(Card card) {
		this.cards.add(card);
		card.currentZone = this;
	}
	
	public LinkedList<Card> cardList() {
		return cards;
	}
	
	public void remove(Card card) {
		this.cards.remove(card);
	}
	
	@Override
	public String toString() {
		return String.format("%s: %b, %s", this.getName(), this.knownGlobal, this.cards);
	}
	
	public String getName() {
		return name;
	}

	@Override
	public int compareTo(CardZone arg0) {
		return name.compareTo(arg0.name);
	}
	
	public CardGame getGame() {
		return game;
	}
	public void shuffle() {
		Collections.shuffle(this.cards, game.getRandom());
	}
	public void sort(Comparator<Card> comparator) {
		Collections.sort(this.cards, comparator);
	}
	public void moveAll(CardZone destination, Player player) {
		for (Card card : new ArrayList<>(cardList())) {
			card.zoneMove(destination, player);
		}
	}
	
}
