package net.zomis.cards.count;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import net.zomis.cards.analyze2.CardSolutions;
import net.zomis.cards.analyze2.CardsAnalyze;
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
	private final CardsAnalyze<CardZone<?>, Card<?>> analyze;
	
	@SuppressWarnings("unchecked")
	public CardCounter(CardGame<?, ?> game) {
		analyze = new CardsAnalyze<>();

		this.game = game;
		this.eventHandler = game.registerHandler(ZoneChangeEvent.class, this::onZoneMove);
		this.availableCards = new ArrayList<>();
		
		for (CardZone<?> zone : game.getPublicZones()) {
			if (zone == game.getActionZone())
				continue;
			analyze.addZone(zone);
			analyze.addCards((CardZone<Card<?>>) zone);
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
		// TODO: When moving the same card again, to another zone, the old rule should be deleted. Instead of adding rules, use a `Map<Card, Zone>`
		if (fromCardZone.isKnown(perspectivePlayer) || toCardZone.isKnown(perspectivePlayer)) {
			System.out.println("CardCounter informed about move: " + card + " ---> " + toCardZone);
			analyze.addRule(toCardZone, 1, c -> c == card);
		}
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

	@SuppressWarnings("unchecked")
	public <T extends Card<?>> void addRule(CardZone<T> zone, int count, Predicate<T> object) {
		analyze.addRule(zone, count, (Predicate<Card<?>>) object); 
		
	}

	public CardSolutions<CardZone<?>, Card<?>> solve() {
		CardsAnalyze<CardZone<?>, Card<?>> copy = analyze.createCopy();
		for (CardZone<?> cz : game.getPublicZones()) {
			if (cz.isKnown(perspectivePlayer) && cz != game.getActionZone()) {
				if (!cz.isEmpty())
					copy.addRule(cz, cz.size(), card -> card.getCurrentZone() == cz);
				else copy.addRule(cz, 0, card -> true);
			}
		}
		
		CardSolutions<CardZone<?>, Card<?>> solutions = copy.solve();
//		System.out.println("Solutions:");
//		solutions.getSolutions().forEach(sol -> {
//			System.out.println("-- Solution: " + sol);
//			sol.getAssignments().entrySet().forEach(System.out::println);
//			System.out.println();
//		});
//		System.out.println("---- END SOLUTIONS");
		return solutions;
	}
	
	public <T extends Card<?>> void getProbabilityDistributionOf(CardZone<T> zone, Predicate<T> object) {
		// TODO: Actually return probabilities -- `double[]`
		
		solve();
		
	}
	
}
