package net.zomis.cards.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardModel;
import net.zomis.utils.ZomisUtils;

public class DeckList {
	
	private final Map<String, Integer> cards;
	private final String name;

	public static DeckList newInstance() {
		return new DeckList();
	}
	public static DeckList newInstance(String name) {
		return new DeckList(name);
	}
	
	public DeckList() {
		this(null);
	}
	
	public DeckList(String name) {
		this.name = name;
		this.cards = new HashMap<String, Integer>();
	}
	public Map<String, Integer> getCards() {
		return new HashMap<String, Integer>(cards);
	}
	public DeckList add(DeckList deck) {
		for (Entry<String, Integer> ee : deck.getCards().entrySet()) {
			this.add(ee.getValue(), ee.getKey());
		}
		return this;
	}
	
	public <M extends CardModel> List<CardCount<M>> getCount(CardGame<?, M> game) {
		Map<String, Integer> cardCopy = new HashMap<String, Integer>(this.cards);
		List<CardCount<M>> count = new ArrayList<CardCount<M>>(cardCopy.size());
		Set<M> gameCards = new HashSet<M>(game.getCards().values());
		for (M ee : gameCards) {
			Integer cardCount = cardCopy.get(ee.getName());
			if (cardCount != null) {
				CardCount<M> cc = new CardCount<M>(ee, null);
				cc.setCount(cardCount);
				count.add(cc);
				cardCopy.remove(ee.getName());
			}
		}
		if (!cardCopy.isEmpty()) {
			throw new IllegalArgumentException("Some cards were not found in game: " + cardCopy.keySet() + ". Game contains " + gameCards.size() + " known cards.");
		}
		
		return new ArrayList<CardCount<M>>(count);
	}
	
	public DeckList add(int count, String cardName) {
		if (cards.get(cardName) != null) {
			cards.put(cardName, cards.get(cardName) + count);
		}
		else cards.put(cardName, count);
		return this;
	}
	public DeckList add(String countAndName) {
		// don't use str.split because cardName needs to support spaces
		String countStr = ZomisUtils.textBefore(countAndName, " ");
		String cardName = ZomisUtils.textAfter(countAndName, " ");
		
		int count;
		try {
			count = Integer.parseInt(countStr);
		}
		catch (NumberFormatException ex) {
			count = 1;
			cardName = countAndName;
		}
		
		return add(count, cardName);
	}

	@Override
	public String toString() {
		return name == null ? super.toString() : name;
	}
	
	public String getName() {
		return name;
	}
	public DeckList addMany(String string) {
		String[] strings = string.split(";");
		for (String str : strings) {
			add(str);
		}
		return this;
	}
	public DeckList add(int count, CardModel card) {
		return this.add(count, card.getName());
	}
	
}
