package net.zomis.cards.analyze2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

import net.zomis.cards.model.CardZone;
import net.zomis.utils.ZomisUtils;

public class CardSolution<Z extends CardZone<?>, C> {

	private final Map<Z, CardAssignment<Z, C>> assignments = new HashMap<>();
	private final double combinations;
	
	public CardSolution(List<ZoneRule<Z, C>> results) {
		Map<CardGroup<C>, Integer> positioned = new HashMap<>();
		double combinations = 1;
//		System.out.println("Creating solution");
		for (ZoneRule<Z, C> rule : results) {
			assignments.put(rule.getZone(), rule.getAssignments());
//			System.out.println("Rule " + rule);
			
			for (Entry<CardGroup<C>, Integer> ee : rule.getAssignments().getAssigns().entrySet()) {
				int value = ee.getValue();
				if (value == 0)
					continue; // Booooooring!
				Integer positionedTemp = positioned.get(ee.getKey());
				int positionedAlready = positionedTemp == null ? 0 : positionedTemp;
				int n = ee.getKey().size() - positionedAlready;
				int r = value; // - positionedAlready;
//				System.out.println(ee + " nCr: " + n + ", " + r);
				combinations *= ZomisUtils.nCr(n, r);
				positioned.put(ee.getKey(), positionedAlready + value);
				
				if (!positioned.containsKey(ee.getKey())) {
					/* 8 cards (4+4), 2 zones (4+4) ---> ZomisUtils.NNKKwithDiv(8, 4, 4, x)
					 * 8 cards (5+3), 2 zones (4+4) ---> ZomisUtils.NNKKwithDiv(8, 5/3, 4, x)
					 * 9 cards (3+3+3), 3 zones (3+3+3) ---> ZomisUtils.NNKKwithDiv(9, 3, 3, x)
					 * 
					 * */
				}
				else {
					
				}
				
				
//				ZomisUtils.nCr(n, r);
			}
			
		}
		this.combinations = combinations;
	}
	
	public Map<Z, CardAssignment<Z, C>> getAssignments() {
		return assignments;
	}

	public boolean validCheck() {
		for (Entry<Z, CardAssignment<Z, C>> ee : assignments.entrySet()) {
			int zoneSize = ee.getKey().size();
			if (!ee.getValue().isValidAssignment())
				return false;
			
			int assigns = ee.getValue().getTotalAssignments();
			if (zoneSize != assigns)
				return false;
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		return super.toString() + " combinations = " + combinations;
	}
	
	public double getCombinations() {
		return combinations;
	}
	
	public double[] getProbabilityDistributionOf(Z zone, Predicate<C> predicate) {
		CardAssignment<Z, C> zoneAssigns = assignments.get(zone);
		System.out.println("Get probability distribution of: " + zoneAssigns);
		List<CardGroup<C>> groups = new ArrayList<>(zoneAssigns.getGroups());
		int count = 0;
		int groupsCount = groups.size();
		
		int[] values = new int[groupsCount];
		int[] loopMaxs = new int[groupsCount];
		int[] predicateCounts = new int[groupsCount];
		for (int i = 0; i < groups.size(); i++) {
			CardGroup<C> group = groups.get(i);
			int value = zoneAssigns.getAssigns().get(group);
			int cardsMatchingPredicate = countMatching(group.getCards(), predicate);
			values[i] = value;
			loopMaxs[i] = Math.min(value, cardsMatchingPredicate);
			predicateCounts[i] = cardsMatchingPredicate;
			count += cardsMatchingPredicate;
		}
		
		double[] result = new double[Math.min(count, zone.size()) + 1];
		System.out.println("Creating array with length " + result.length);
		// Zone{Player1 Hand} --> Assign:Zone{Player1 Hand}={CG:[4 CLUBS + 9 others]=0, CG:[9 CLUBS + 30 others]=13}
		// Zone{Player1 Hand} --> Assign:Zone{Player1 Hand}={CG:[2 CLUBS + 7 others]=2, CG:[2 CLUBS + 7 others]=4, CG:[9 CLUBS + 30 others]=7}
		// Zone{Player0 __Y__} --> Assign:Zone{Player0 __Y__}={CG:[Card:a, Card:a]=0, CG:[Card:d, Card:d]=1, CG:[Card:b, Card:c]=0, CG:[Card:b, Card:c]=1}
		
//		List<Map<CardGroup<C>, Integer>> mapList = new ArrayList<>();
		int[] loop = new int[groups.size()];
		int lastIndex = loop.length - 1;
		
		while (loop[0] <= loopMaxs[0]) {
			System.out.print(Arrays.toString(loop) + " = ");
			int loopSum = 0;
			double combinations = 1;
			for (int i = 0; i < groupsCount; i++) {
				loopSum += loop[i];
				CardGroup<C> group = groups.get(i);
				combinations *= ZomisUtils.NNKKwithDiv(group.size(), predicateCounts[i], values[i], loop[i]);
				System.out.println(combinations);
			}
			result[loopSum] += combinations;
			
			
			// Increase the count
			loop[lastIndex]++;
			for (int overflow = lastIndex; overflow >= 1 && loop[overflow] > loopMaxs[overflow]; overflow--) {
				loop[overflow] = 0;
				loop[overflow - 1]++;
			}
		}
		// a + a + a + b + b = 2, a + a + c + c = 1
		
//		for (int i = 0; i < result.length; i++) {
//			result[i] = result[i] / this.combinations;
//		}
		
		System.out.println("Result = " + Arrays.toString(result));
		System.out.println("Sum = " + Arrays.stream(result).sum());
		return result;
	}
	
	private int countMatching(Collection<C> cards, Predicate<C> predicate) {
		int i = 0;
		for (C card : cards) {
			if (predicate.test(card))
				i++;
		}
		return i;
	}

	public int getAssignment(Z zone, CardGroup<C> group) {
		CardAssignment<Z, C> assigns = this.assignments.get(zone);
		if (assigns == null)
			throw new IllegalArgumentException("Zone " + zone + " does not have an assignment in this solution");
		Integer value = assigns.getAssigns().get(group);
		if (value == null)
			throw new IllegalArgumentException("Group " + group + " does not have an assignment for zone " + zone);
		return value;
	}
	
}
