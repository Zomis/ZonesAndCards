package net.zomis.cards.count;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import net.zomis.cards.events.card.ZoneChangeEvent;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.Player;
import net.zomis.events.EventListener;
import net.zomis.events.IEventHandler;
import net.zomis.utils.ZomisList;
import net.zomis.utils.ZomisList.FilterInterface;

public class CardCounter implements EventListener {

	private final CardGame<?, ?> game;
	private Player perspectivePlayer;
	private int informationCounter;
	private final List<Card<?>> availableCards;
	private final IEventHandler eventHandler;
	
	public CardCounter(CardGame<?, ?> game) {
		this.game = game;
		this.eventHandler = game.registerHandler(ZoneChangeEvent.class, this::onZoneMove);
		this.availableCards = new ArrayList<>();
		
		for (CardZone<?> zone : game.getPublicZones()) {
			if (game.getActionZone() == zone)
				continue;
			availableCards.addAll(zone.cardList());
		}
		ZomisList.shuffle(availableCards, new Random());
	}

	public void addAvailableCards(CardZone<?> zone) {
		this.availableCards.addAll(zone.cardList());
	}
	
	public void setPerspective(Player player) {
		this.perspectivePlayer = player;
	}
	
	public int getInformationCounter() {
		return informationCounter;
	}
	
	public void onZoneMove(ZoneChangeEvent event) {
		Card<?> card = event.getCard();
		CardZone<?> source = event.getFromCardZone();
		CardZone<?> dest = event.getToCardZone();
		if (event.getFromCardZone().isKnown(perspectivePlayer)) {
			this.informMove(card, source, dest);
		}
		if (source != null && dest.isKnown(perspectivePlayer)) {
			this.informMove(card, source, dest);
		}
	}
	
	private void informMove(Card<?> card, CardZone<?> fromCardZone, CardZone<?> toCardZone) {
		++informationCounter;
	}

	public void javaGarbage() {
		game.removeHandler(eventHandler);
		perspectivePlayer = null;
	}

	public List<Card<?>> getAvailable() {
		return new ArrayList<Card<?>>(this.availableCards);
	}

	public <T extends Card<?>> CardCalculation calculate(FilterInterface<T> filter) {
		List<Card<?>> available = this.getAvailable();
		int filtered = 0;
		for (Card<?> card : available) {
			@SuppressWarnings("unchecked")
			T cc = (T) card;
			if (filter.shouldKeep(cc)) {
				filtered++;
			}
			
		}
		return new CardCalculation(available.size(), filtered);
	}

	public <T extends Card<?>> void addRule(CardZone<T> zone, int count, Predicate<T> object) {
		// 
		
	}

	public <T extends Card<?>> void getProbabilityDistributionOf(CardZone<T> zone, Predicate<T> object) {
		// TODO Auto-generated method stub
		
	}
	
}
