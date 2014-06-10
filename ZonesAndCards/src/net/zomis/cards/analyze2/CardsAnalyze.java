package net.zomis.cards.analyze2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Predicate;

import net.zomis.cards.model.CardZone;




public class CardsAnalyze<Z extends CardZone<?>, C> implements CardSolutionCallback<Z, C> {

	private final List<ZoneRule<Z, C>> rules = new ArrayList<>();
	private final List<C> cards = new ArrayList<>();
	private final List<Z> zones = new ArrayList<>();
	
	private final Map<CardGroup<C>, Integer> unplacedCards = new HashMap<>();
	private final List<ZoneRule<Z, C>> assignmentProgress = new ArrayList<>();
	private final CardSolutionCallback<Z, C> solutionCallback;
	private final List<CardSolution<Z, C>> solutions = new ArrayList<>();
	
	// GroupValues<Field> knownValues, List<FieldRule<Field>> unsolvedRules
	
	public CardsAnalyze() {
		this.solutionCallback = this;
	}
	
	public CardsAnalyze(CardSolutionCallback<Z, C> callback) {
		this.solutionCallback = callback;
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

	public CardSolutions<Z, C> solve() {
		this.solveOnce();
		this.solveInternal();
		return new CardSolutions<Z, C>(this.solutions);
	}
	
	private void solveOnce() {
		this.verifySums();
		this.addRulesAboutMissingCards();
		
		// Create/Split CardGroups
		this.splitGroups();
		this.calculateUnplacedCards();
	}
	
	private void solveInternal() {
		System.out.println("Before simplification:");
		this.outputRules();
		
		// Simplify rules
		this.simplify();
		
		System.out.println("After simplification:");
		this.outputRules();
		
		// Iterate and solve
		this.solveByIteration();
	}

	private void solveByIteration() {
		ZoneRule<Z, C> focusZone = determineFocusZone();
		if (focusZone == null) {
			this.solutionCallback.onSolved(assignmentProgress);
			return;
		}
		
		CardGroup<C> focusGroup = getFocusGroupFor(focusZone);
		
		int unplaced = this.unplacedCards.get(focusGroup);
		
		for (int i = 0; i <= unplaced; i++) {
			System.out.println("Solving by iteration: " + focusGroup + " = " + i + " in " + focusZone);
			
			CardsAnalyze<Z, C> copy = this.createCopy();
			copy.assign(focusZone.getZone(), focusGroup, i);
			copy.solveInternal(); 
			System.out.println("-------------");
		}
		
	}

	private ZoneRule<Z, C> determineFocusZone() {
//		ZoneRule<Z, C> zoneRule = this.assignmentProgress.get(2);
//		if (zoneRule.getCompare() != CountStyle.DONE)
//			return zoneRule;
		
		for (ZoneRule<Z, C> rule : this.assignmentProgress) {
			if (rule.getCompare() != CountStyle.DONE) {
				return rule;
			}
		}
		
		return null;
	}

	private CardGroup<C> getFocusGroupFor(ZoneRule<Z, C> focusZone) {
		for (Entry<CardGroup<C>, Integer> grp : focusZone.getAssignments().getAssigns().entrySet()) {
			if (grp.getValue() != null)
				continue;
			return grp.getKey();
		}
		throw new IllegalArgumentException("Rule does not have any unset groups: " + focusZone);
	}

	private CardsAnalyze<Z, C> createCopy() {
		CardsAnalyze<Z, C> result = new CardsAnalyze<>(this.solutionCallback);
		
		for (ZoneRule<Z, C> assignmentProg : this.assignmentProgress) {
			result.assignmentProgress.add(assignmentProg.copy());
		}
		
		for (ZoneRule<Z, C> rule : this.rules) {
			result.rules.add(rule.copy());
		}
		
//		result.assignmentProgress
//		result.rules
		result.unplacedCards.putAll(this.unplacedCards);
		
		return result;
	}

	private void calculateUnplacedCards() {
		ZoneRule<Z, C> first = this.assignmentProgress.get(0);
		for (CardGroup<C> group : first.getAssignments().getGroups()) {
			this.unplacedCards.put(group, group.size());
		}
	}

	/**
	 * Loop through the rules and check for `EQUAL(x) = Assign:{only one group}`
	 */
	private void simplify() {
		boolean simplificationDone;
		do {
			simplificationDone = false;
			for (ZoneRule<Z, C> rule : this.rules) {
				if (rule.isEqualKnown()) {

					Entry<CardGroup<C>, Integer> assignment = rule.getAssignments().getAssigns().entrySet().iterator().next(); // TODO: Law of Demeter. rule.getOnlyAssignment(); ?
					assignment.setValue(rule.getCount());
					CardGroup<C> group = assignment.getKey();

					this.assign(rule.getZone(), group, rule.getCount()); // Note that the assignment progress rule for `rule.getZone` does not match `rule` here!
					rule.clear();
					simplificationDone = true;
				}
			}
			// TODO: If assignments is complete, then try to scan for groups that can only be within one zone
			
			for (ZoneRule<Z, C> progress : this.assignmentProgress) {
				if (progress.synchronizeWith(unplacedCards))
					simplificationDone = true;
			}
		}
		while (simplificationDone);
	}

	private void assign(Z zone, CardGroup<C> group, int count) {
		ZoneRule<Z, C> progress = this.getAssignmentProgressFor(zone);
		
		progress.assign(group, count, unplacedCards);
	}

	private ZoneRule<Z, C> getAssignmentProgressFor(Z zone) {
		// TODO: Probably make assignment progress a Map<Z, ZoneRule<Z, C>> instead, this is just nuts right now.
		for (ZoneRule<Z, C> rule : this.assignmentProgress) {
			if (rule.getZone() == zone) {
				return rule;
			}
		}
		throw new IllegalStateException("No assignment progress found for zone " + zone);
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
			this.assignmentProgress.add(ZoneRule.unknown(zone, cards));
		}
	}

	private void splitGroups() {
		List<ZoneRule<Z, C>> rules = new ArrayList<>(this.rules);
		rules.addAll(this.assignmentProgress);
		
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
		Consumer<Object> log = System.out::println;
		Runnable hr = System.out::println;
		
		hr.run();
		log.accept("Rules:");
		this.rules.forEach(log);
		hr.run();
		log.accept("Assignment Progress:");
		this.assignmentProgress.forEach(log);
		hr.run();
		log.accept("Unplaced cards:");
		log.accept(this.unplacedCards);
		hr.run();
	}

	@Override
	public void onSolved(List<ZoneRule<Z, C>> results) {
		CardSolution<Z, C> sol = new CardSolution<Z, C>(results);
		if (sol.validCheck()) {
			this.solutions.add(sol);
		}
		else {
			System.out.println(this + " INVALID Solution has been found!!!".toUpperCase() + " -- " + sol);
			results.forEach(System.out::println);
			System.out.println();
			System.out.println();
			System.out.println();
		}
	}
	
}
