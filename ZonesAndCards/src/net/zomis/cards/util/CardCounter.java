package net.zomis.cards.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.zomis.ZomisList;
import net.zomis.ZomisList.FilterInterface;
import net.zomis.cards.events.ZoneChangeEvent;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.Player;
import net.zomis.events.Event;
import net.zomis.events.EventListener;

public class CardCounter implements EventListener {

	private final CardGame game;
	private Player perspectivePlayer;
	private int informationCounter;
	private final List<Card> availableCards;
	
	public CardCounter(CardGame game) {
		this.game = game;
		game.registerListener(this);
		this.availableCards = new ArrayList<Card>();
	}

	public void addAvailableCards(CardZone zone) {
		this.availableCards.addAll(zone.cardList());
	}
	
	public void setPerspective(Player player) {
		this.perspectivePlayer = player;
	}
	
	public int getInformationCounter() {
		return informationCounter;
	}
	
	@Event
	public void onZoneMove(ZoneChangeEvent event) {
		if (event.getFromCardZone().isKnown(perspectivePlayer)) {
			this.informMove(event.getCard(), event.getFromCardZone(), event.getToCardZone());
		}
		if (event.getToCardZone() != null && event.getToCardZone().isKnown(perspectivePlayer)) {
			this.informMove(event.getCard(), event.getFromCardZone(), event.getToCardZone());
		}
	}
	
	private void informMove(Card card, CardZone fromCardZone, CardZone toCardZone) {
		++informationCounter;
		
	}

	public void javaGarbage() {
		game.removeListener(this);
		perspectivePlayer = null;
	}

	public List<Card> getAvailable() {
		return new ArrayList<Card>(this.availableCards);
	}

	public CardCalculation calculate(List<Card> available, FilterInterface<Card> filter) {
		LinkedList<Card> filtered = ZomisList.filter2(available, filter);
		return new CardCalculation(available.size(), filtered.size());
	}
	
}
