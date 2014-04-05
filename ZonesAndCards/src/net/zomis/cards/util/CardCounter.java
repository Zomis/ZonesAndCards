package net.zomis.cards.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.zomis.cards.events.card.ZoneChangeEvent;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.Player;
import net.zomis.events.Event;
import net.zomis.events.EventListener;
import net.zomis.utils.ZomisList;
import net.zomis.utils.ZomisList.FilterInterface;

public class CardCounter<C extends Card<?>> implements EventListener {

	private final CardGame<?, ?> game;
	private Player perspectivePlayer;
	private int informationCounter;
	private final List<C> availableCards;
	
	public CardCounter(CardGame<?, ?> game) {
		this.game = game;
		game.registerListener(this);
		this.availableCards = new ArrayList<C>();
	}

	public void addAvailableCards(CardZone<C> zone) {
		this.availableCards.addAll(zone.cardList());
	}
	
	public void setPerspective(Player player) {
		this.perspectivePlayer = player;
	}
	
	public int getInformationCounter() {
		return informationCounter;
	}
	
	@SuppressWarnings("unchecked")
	@Event
	public void onZoneMove(ZoneChangeEvent event) {
		C card = (C) event.getCard();
		CardZone<C> source = (CardZone<C>) event.getFromCardZone();
		CardZone<C> dest = (CardZone<C>) event.getToCardZone();
		if (event.getFromCardZone().isKnown(perspectivePlayer)) {
			this.informMove(card, source, dest);
		}
		if (source != null && dest.isKnown(perspectivePlayer)) {
			this.informMove(card, source, dest);
		}
	}
	
	private void informMove(C card, CardZone<C> fromCardZone, CardZone<C> toCardZone) {
		++informationCounter;
		
	}

	public void javaGarbage() {
		game.removeListener(this);
		perspectivePlayer = null;
	}

	public List<C> getAvailable() {
		return new ArrayList<C>(this.availableCards);
	}

	public CardCalculation calculate(List<C> available, FilterInterface<C> filter) {
		LinkedList<C> filtered = ZomisList.filter2(available, filter);
		return new CardCalculation(available.size(), filtered.size());
	}
	
}
