package net.zomis.cards.analyze2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import net.zomis.cards.model.CardZone;




public class CardsAnalyze<Z extends CardZone<?>, C> {

	private final List<ZoneRule<Z, C>> rules = new ArrayList<>();
	private final List<C> cards = new ArrayList<>();
	private final List<Z> zones = new ArrayList<>();
	
	public CardsAnalyze() {
	}

	public CardsAnalyze<Z, C> addCards(Iterable<C> cards) {
		if (!this.rules.isEmpty()) {
			throw new IllegalStateException("Cannot add more cards when rules has been added");
		}
		for (C c : cards) {
			this.cards.add(c);
		}
		return this;
	}

	public CardsAnalyze<Z, C> addZone(Z zone) {
		this.zones.add(zone);
		return this;
	}

	public void addRule(Z zone, int count, Predicate<C> predicate) {
		this.addRule(zone, CountStyle.EQUAL, count, predicate);
	}
	
	public void addRule(Z zone, CountStyle compare, int count, Predicate<C> predicate) {
		this.rules.add(new ZoneRule<Z, C>(zone, compare, count, findCards(predicate)));
	}

	private final List<CardSolution<Z, C>> solutions = new ArrayList<>();
	
	public void solve() {
		this.verifySums();
		this.addRulesAboutMissingCards();
		
		
		// Create/Split CardGroups
		this.splitGroups();
		
		this.outputRules();
		
		// Simplify rules
		
		
		
		// Iterate and solve
		
		
	}
	
	
	private void verifySums() {
		int cards = 0;
		for (Z zone : this.zones) {
			cards += zone.size();
		}
		
		if (cards != this.cards.size()) {
			throw new IllegalStateException("Mismatch cards: " + this.cards.size() + " cards and room for " + cards);
		}
	}

	private void addRulesAboutMissingCards() {
		/* 2x + 2y + 2z = 2a + b + c + 2d
		 * 2y = 0a
		 * 2x = 0d
		 * --->
		 * 2x = ?a + ?bc
		 * 2y = ?bc + ?d
		 * 2z = ?a + ?bc + ?d
		 **/
		for (Z zone : this.zones) {
			
//			List<ZoneRule<Z, C>> zoneRules = getRulesFor(zone);
			this.rules.add(ZoneRule.unknown(zone, cards));
			
			
		}
		
		
	}

	private List<ZoneRule<Z, C>> getRulesFor(Z zone) {
		List<ZoneRule<Z, C>> list = new ArrayList<>();
		for (ZoneRule<Z, C> rule : this.rules) {
			if (rule.getZone() == zone) {
				list.add(rule);
			}
		}
		
		return list;
	}

	private void splitGroups() {
//		List<CardGroup<C>> groups = new ArrayList<>();
//		for (ZoneRule<Z, C> rule : this.rules) {
//			for (CardGroup<C> grp : rule.getAssignments().getGroups()) {
//				groups.add(grp);
//			}
//		}
		
		boolean splitPerformed = true;
		while (splitPerformed) {
			splitPerformed = false;
			for (ZoneRule<Z, C> a : rules) {
				for (ZoneRule<Z, C> b : rules) {
					boolean result = a.checkIntersection(b);
					if (result) {
						splitPerformed = true;
					}
				}
			}
		}
		
		
		
		
	}

	private Collection<C> findCards(Predicate<C> predicate) {
		List<C> matchingCards = new ArrayList<>();
		for (C card : cards) {
			if (predicate.test(card))
				matchingCards.add(card);
		}
		
		return matchingCards;
	}

	public void outputRules() {
		this.rules.forEach(System.out::println);
	}
	
}
