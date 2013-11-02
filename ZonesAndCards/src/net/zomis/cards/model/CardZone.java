package net.zomis.cards.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.zomis.cards.events.card.CardCreatedEvent;
import net.zomis.cards.events.zone.ZoneReverseEvent;
import net.zomis.cards.events.zone.ZoneShuffleEvent;
import net.zomis.cards.events.zone.ZoneSortEvent;
import net.zomis.events.IEvent;

public class CardZone implements Comparable<CardZone> {

	public static interface GetZoneInterface<E> {
		CardZone getZone(E object);
	}
	
	CardGame game;
	
	private final Map<Player, Boolean> known = new HashMap<Player, Boolean>();
	private final LinkedList<Card> cards = new LinkedList<Card>();
	private final String name;

	private boolean	knownGlobal;

	public CardZone(String zoneName) {
		this.name = zoneName;
	}
	public void setGloballyKnown(boolean knowledge) {
		this.knownGlobal = knowledge;
	}
	public CardZone setKnown(Player player, boolean knowledge) {
		this.known.put(player, knowledge);
		return this;
	}
	
	public boolean isKnown(Player player) {
		Boolean b = known.get(player);
		return (b == null ? knownGlobal : b);
	}

	public LinkedList<Card> cardList() {
		return cards;
	}
	
	@Override
	public final String toString() {
		return String.format("Zone{%s}", this.getName());
//		return String.format("Zone{%s: %b, %s}", this.getName(), this.knownGlobal, this.cards);
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
		this.shuffle(game.getRandom());
	}
	public void shuffle(Random random) {
		Collections.shuffle(this.cards, random);
		this.executeEvent(new ZoneShuffleEvent(this));
	}
	public void sort(Comparator<Card> comparator) {
		Collections.sort(this.cards, comparator);
		this.executeEvent(new ZoneSortEvent(this));
	}
	public void createCardOnTop(CardModel cardModel) {
		Card card = cardModel.createCardInternal(this);
		this.cards.addFirst(card);
		this.executeEvent(new CardCreatedEvent(card));
	}
	private void executeEvent(IEvent event) {
		if (this.game != null)
			this.game.executeEvent(event);
	}
	public void createCardOnBottom(CardModel cardModel) {
		Card card = cardModel.createCardInternal(this);
		this.cards.addLast(card);
		this.executeEvent(new CardCreatedEvent(card));
	}
	public Card getTopCard() {
		return this.cards.peekFirst();
	}
	public Card getBottomCard() {
		return this.cards.peekLast();
	}
	public CardZone extractTopCards(int number) {
		CardZone copy = this.createEmptyCopy();
		for (int i = 0; i < number; i++) {
			this.getTopCard().zoneMoveOnBottom(copy);
		}
		return copy;
	}
	public CardZone extractBottomCards(int number) {
		CardZone copy = this.createEmptyCopy();
		for (int i = 0; i < number; i++) {
			this.getBottomCard().zoneMoveOnTop(copy);
		}
		return copy;
	}
	/**
	 * Reverse the ordering of the cards in this zone
	 * @return Returns self
	 */
	public CardZone reverse() {
		Collections.reverse(cards);
		this.executeEvent(new ZoneReverseEvent(this));
		return this;
	}
	public void moveToTopOf(CardZone destination) {
		ArrayList<Card> list = new ArrayList<Card>(cardList());
		Collections.reverse(list);
		for (Card card : list) {
			card.zoneMoveOnTop(destination);
		}
	}
	public void moveToBottomOf(CardZone destination) {
		for (Card card : new ArrayList<Card>(cardList())) {
			card.zoneMoveOnBottom(destination);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (name.hashCode());
		return result;
	}
	private int timesCopied;
	private CardZone createEmptyCopy() {
		CardZone zone = new CardZone(this.getName() + "-Copy" + ++timesCopied);
		zone.setGloballyKnown(this.knownGlobal);
		for (Entry<Player, Boolean> ee : this.known.entrySet()) {
			zone.setKnown(ee.getKey(), ee.getValue());
		}
		return zone;
	}
	public boolean isEmpty() {
		return this.cards.isEmpty();
	}
	public int size() {
		return this.cards.size();
	}
	public <E> void deal(int count, E[] objects, GetZoneInterface<E> zoneGetter) {
		this.deal(count, Arrays.asList(objects), zoneGetter);
	}
	public <E> void deal(int count, List<E> objects, GetZoneInterface<E> zoneGetter) {
		for (E e : objects) {
			if (count <= 0)
				return;
			CardZone zone = zoneGetter.getZone(e);
			this.getTopCard().zoneMoveOnBottom(zone);
			count--;
		}
	}
	public <E> void dealUntilLeft(int cardsLeft, List<? extends E> players, GetZoneInterface<E> getHand) {
		while (true) {
			for (E player : players) {
				if (this.size() <= cardsLeft)
					return;
				Card card = this.getTopCard();
				card.zoneMoveOnBottom(getHand.getZone(player));
			}
		}
	}
}
