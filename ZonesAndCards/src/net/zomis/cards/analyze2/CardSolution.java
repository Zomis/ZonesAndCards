package net.zomis.cards.analyze2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
	
}
