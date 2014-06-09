package net.zomis.cards.analyze2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zomis.cards.model.CardZone;

public class ZoneRule<Z extends CardZone<?>, C> {

	private final Z zone;
	private final CountStyle compare;
	private final int count;
	private final CardAssignment<Z, C> assignments;
	
	private ZoneRule(Z zone, Collection<C> cards) {
		this.zone = zone;
		this.compare = null;
		this.count = 0;
		Map<CardGroup<C>, Integer> assigns = new HashMap<>();
		assigns.put(new CardGroup<>(cards), null);
		this.assignments = new CardAssignment<Z, C>(zone, assigns);
	}
	
	public ZoneRule(Z zone, CountStyle compare, int count, Collection<C> cards) {
		this.zone = zone;
		this.compare = compare;
		this.count = count;
		Map<CardGroup<C>, Integer> assigns = new HashMap<>();
		CardGroup<C> grp = new CardGroup<>(cards);
		if (compare == CountStyle.EQUAL) {
			assigns.put(grp, count);
		}
		else assigns.put(grp, null);
		
		this.assignments = new CardAssignment<Z, C>(zone, assigns);
		
	}
	
	public Z getZone() {
		return zone;
	}
	
	public CardAssignment<Z, C> getAssignments() {
		return assignments;
	}
	
	public boolean checkIntersection(ZoneRule<Z, C> other) {
		if (other == this)
			return false;
		
		List<CardGroup<C>> fieldsCopy = new ArrayList<CardGroup<C>>(assignments.getGroups());
		List<CardGroup<C>> ruleFieldsCopy = new ArrayList<CardGroup<C>>(other.assignments.getGroups());
		
		for (CardGroup<C> groupA : fieldsCopy) {
			for (CardGroup<C> groupB : ruleFieldsCopy) {
				if (groupA == groupB)
					continue;
				
				ListSplit<C> splitResult = groupA.splitCheck(groupB);
				if (splitResult == null)
					continue; // nothing to split
				if (!splitResult.splitPerformed())
					continue;
				
				System.out.println(splitResult);
				
				CardGroup<C> both = new CardGroup<>(splitResult.getBoth());
				CardGroup<C> onlyA = new CardGroup<>(splitResult.getOnlyA());
				CardGroup<C> onlyB = new CardGroup<>(splitResult.getOnlyB());
				
				this.assignments.assigns.remove(groupA);
				if (!onlyA.isEmpty())
					this.assignments.assigns.put(onlyA, null);
				if (!both.isEmpty())
					this.assignments.assigns.put(both, null);
				
				other.assignments.assigns.remove(groupB);
				if (!both.isEmpty())
					other.assignments.assigns.put(both, null);
				if (!onlyB.isEmpty()) 
					other.assignments.assigns.put(onlyB, null);
				return true;
			}
		}
		
		return false;
	}
	
	public static <Z extends CardZone<?>, C> ZoneRule<Z, C> unknown(Z zone, Collection<C> cards) {
		return new ZoneRule<Z, C>(zone, cards);
	}
	
	@Override
	public String toString() {
		return "Rule:" + this.zone + " = " + compare + "(" + count + ") " + assignments;
	}
	
}
