package net.zomis.cards.analyze2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.zomis.cards.model.CardZone;

public class ZoneRule<Z extends CardZone<?>, C> {

	private final Z zone;
	private CountStyle compare;
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
	
	private ZoneRule(ZoneRule<Z, C> previous) {
		this.zone = previous.zone;
		this.compare = previous.compare;
		this.count = previous.count;
		this.assignments = new CardAssignment<Z, C>(zone, new HashMap<>(previous.assignments.assigns));
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
	
	public int getCount() {
		return count;
	}

	public boolean isEqualKnown() {
		return this.compare == CountStyle.EQUAL && this.assignments.getGroups().size() == 1;
	}

	public void completedCheck() {
		int i = getAssignmentSum();
		if (i == this.zone.size()) {
			Iterator<Entry<CardGroup<C>, Integer>> it = this.assignments.assigns.entrySet().iterator();
			while (it.hasNext()) {
				if (it.next().getValue() == null)
					it.remove();
			}
			this.compare = CountStyle.DONE;
		}
		
	}

	private int getAssignmentSum() {
		int i = 0;
		for (Entry<CardGroup<C>, Integer> ee : this.assignments.assigns.entrySet()) {
			Integer value = ee.getValue();
			if (value != null)
				i += value;
		}
		return i;
	}

	public void clear() {
		this.compare = CountStyle.DONE;
		this.assignments.assigns.clear();
	}

	public ZoneRule<Z, C> copy() {
		return new ZoneRule<>(this);
	}

	private CardGroup<C> getOnlyUnassignedGroup() {
		CardGroup<C> unassigned = null;
		for (Entry<CardGroup<C>, Integer> ee : this.assignments.assigns.entrySet()) {
			if (ee.getValue() == null) {
				if (unassigned != null)
					return null;
				unassigned = ee.getKey();
			}
		}
		return unassigned;
	}

	public int getUnassignedGroupsSum(Map<CardGroup<C>, Integer> unassigned) {
		int count = 0;
		for (Entry<CardGroup<C>, Integer> ee : this.assignments.assigns.entrySet()) {
			if (ee.getValue() == null) {
				count += unassigned.get(ee.getKey());
			}
		}
		return count;
	}

	public boolean synchronizeWith(Map<CardGroup<C>, Integer> unplacedCards) {
		for (Entry<CardGroup<C>, Integer> ee : this.assignments.assigns.entrySet()) {
			if (ee.getValue() == null) {
				if (unplacedCards.get(ee.getKey()) == 0) {
					this.assignments.assign(ee.getKey(), 0);
					return true;
				}
			}
		}
		
		if (this.checkForOnlyOneUnassignedGroup(unplacedCards))
			return true;
		if (this.checkForUnassignedGroupsSumMatchingRemainingSpace(unplacedCards))
			return true;
		
		return false;
	}
	
	public boolean checkForOnlyOneUnassignedGroup(Map<CardGroup<C>, Integer> unassignedCards) {
		// Rule:Zone{Y} = null(0) Assign:Zone{Y}={CG:[Card:a, Card:a]=0, CG:[Card:b, Card:c, Card:b, Card:c]=null, CG:[Card:d, Card:d]=0}
		// {CG:[Card:a, Card:a]=2, CG:[Card:b, Card:c, Card:b, Card:c]=2, CG:[Card:d, Card:d]=2}
		
		CardGroup<C> unassigned = this.getOnlyUnassignedGroup();
		if (unassigned != null) {
			// Find the unassigned group and assign it to whatever space is available.
			
			int count = Math.min(unassignedCards.get(unassigned), this.getRemainingSpace());
			unassignedCards.put(unassigned, 0);
			this.assignments.assign(unassigned, count);
			this.checkFinished();
		}
		return unassigned != null;
	}
	
	private boolean checkFinished() {
		for (Integer ee : this.assignments.assigns.values()) {
			if (ee == null)
				return false;
		}
		this.compare = CountStyle.DONE;
		return true;
	}

	public CountStyle getCompare() {
		return compare;
	}
	
	private int getRemainingSpace() {
		int count = this.zone.size();
		for (Integer ee : this.assignments.assigns.values()) {
			if (ee != null)
				count -= ee;
		}
		return count;
	}

	public boolean checkForUnassignedGroupsSumMatchingRemainingSpace(Map<CardGroup<C>, Integer> unassignedCards) {
		// Rule:Zone{Y} = null(0) Assign:Zone{Y}={CG:[Card:a, Card:a]=0, CG:[Card:b, Card:b]=null, CG:[Card:c, Card:c]=null, CG:[Card:d, Card:d]=0}
		// Unassigned: {CG:[Card:a, Card:a]=2, CG:[Card:b, Card:b]=1, CG:[Card:c, Card:c]=1, CG:[Card:d, Card:d]=2}
		
		if (this.getUnassignedGroupsSum(unassignedCards) == this.getRemainingSpace()) {
			// Find the unassigned groups and assign them to the 
			return false;
		}
		return false;
	}
	
}
