package net.zomis.cards.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
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
import net.zomis.utils.ZomisList;

public class CardZone<E extends Card<?>> implements Comparable<CardZone<E>>, Iterable<E> {

	public static interface GetZoneInterface<E> {
		<C extends Card<?>> CardZone<C> getZone(E object);
	}
	
	CardGame<?, ?> game;
	
	private final Map<Player, Boolean> known = new HashMap<Player, Boolean>();
	private final LinkedList<E> cards = new LinkedList<E>();
	private final String name;
	private final Player owner;

	private boolean	knownGlobal;

	public CardZone(String zoneName, Player owner) {
		this.name = zoneName;
		this.owner = owner;
	}
	public CardZone(String zoneName) {
		this(zoneName, null);
	}
	public void setGloballyKnown(boolean knowledge) {
		this.knownGlobal = knowledge;
	}
	public CardZone<E> setKnown(Player player, boolean knowledge) {
		this.known.put(player, knowledge);
		return this;
	}
	
	public boolean isKnown(Player player) {
		Boolean b = known.get(player);
		return (b == null ? knownGlobal : b);
	}
	
	public boolean contains(E card) {
		return cards.contains(card);
	}

	public LinkedList<E> cardList() {
		return cards;
	}
	
	@Override
	public final String toString() {
		return "Zone{" + getName() + "}";
	}
	
	public String getFullName() {
		return owner == null ? getName() : "Player" + getOwner().getIndex() + " " + getName();
	}
	
	public String getName() {
		return name;
	}

	@Override
	public int compareTo(CardZone<E> arg0) {
		return name.compareTo(arg0.name);
	}
	
	public CardGame<?, ?> getGame() {
		return game;
	}
	public void shuffle() {
		if (game == null)
			throw new NullPointerException("CardZone doesn't have a game");
		this.shuffle(game.getRandom());
	}
	public void shuffle(Random random) {
		ZomisList.shuffle(cards, random);
		this.executeEvent(new ZoneShuffleEvent(this));
	}
	
	public void sort(Comparator<E> comparator) {
		Collections.sort(this.cards, comparator);
		this.executeEvent(new ZoneSortEvent(this));
	}
	
	public E createCardOnTop(CardModel cardModel) {
		if (cardModel == null)
			throw new NullPointerException("CardModel cannot be null");
		
		@SuppressWarnings("unchecked")
		E card = (E) cardModel.createCardInternal(this); // TODO: Possibly use a "CardFactory" somewhere...
		this.cards.addFirst((E) card);
		this.executeEvent(new CardCreatedEvent(card));
		return card;
	}
	private void executeEvent(IEvent event) {
		if (this.game != null)
			this.game.executeEvent(event);
	}
	public E createCardOnBottom(CardModel cardModel) {
		if (cardModel == null)
			throw new NullPointerException("CardModel cannot be null");
		@SuppressWarnings("unchecked")
		E card = (E) cardModel.createCardInternal(this);
		this.cards.addLast(card);
		this.executeEvent(new CardCreatedEvent(card));
		return card;
	}
	public E getTopCard() {
		if (cards.isEmpty())
			return null;
		return this.cards.getFirst();
	}
	
	public E getBottomCard() {
		if (cards.isEmpty())
			return null;
		return this.cards.getLast();
	}
	
	public CardZone<E> extractTopCards(int number) {
		CardZone<E> copy = this.createEmptyCopy();
		for (int i = 0; i < number; i++) {
			this.getTopCard().zoneMoveOnBottom(copy);
		}
		return copy;
	}
	public CardZone<E> extractBottomCards(int number) {
		CardZone<E> copy = this.createEmptyCopy();
		for (int i = 0; i < number; i++) {
			this.getBottomCard().zoneMoveOnTop(copy);
		}
		return copy;
	}
	/**
	 * Reverse the ordering of the cards in this zone
	 * @return Returns self
	 */
	public CardZone<E> reverse() {
		Collections.reverse(cards);
		this.executeEvent(new ZoneReverseEvent(this));
		return this;
	}
	public void moveToTopOf(CardZone<E> destination) {
		List<E> list = new ArrayList<E>(cards);
		Collections.reverse(list);
		for (Card<?> card : list) {
			card.zoneMoveOnTop(destination);
		}
	}
	public void moveToBottomOf(CardZone<E> destination) {
		for (E card : new ArrayList<E>(cards)) {
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
	private CardZone<E> createEmptyCopy() {
		CardZone<E> zone = new CardZone<E>(this.getName() + "-Copy" + ++timesCopied, owner);
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
	
	@Deprecated
	public <F> void deal(int count, F[] objects, GetZoneInterface<F> zoneGetter) {
		this.deal(count, Arrays.asList(objects), zoneGetter);
	}
	
	@Deprecated
	public <F> void deal(int count, List<F> objects, GetZoneInterface<F> zoneGetter) {
		for (F e : objects) {
			if (count <= 0)
				return;
			CardZone<?> zone = zoneGetter.getZone(e);
			this.getTopCard().zoneMoveOnBottom(zone);
			count--;
		}
	}
	
	@Deprecated
	public <F> void dealUntilLeft(int cardsLeft, List<? extends F> players, GetZoneInterface<F> getHand) {
		while (true) {
			for (F player : players) {
				if (this.size() <= cardsLeft)
					return;
				E card = this.getTopCard();
				card.zoneMoveOnBottom(getHand.getZone(player));
			}
		}
	}
	
	public Player getOwner() {
		return owner;
	}
	
	@Override
	public Iterator<E> iterator() {
		return cards.iterator();
	}
	
	public boolean containsModel(CardModel c) {
		for (E card : this) {
			if (card.getModel() == c) {
				return true;
			}
		}
		return false;
	}
}
