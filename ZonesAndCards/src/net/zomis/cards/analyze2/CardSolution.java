package net.zomis.cards.analyze2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.zomis.cards.model.CardZone;

public class CardSolution<Z extends CardZone<?>, C> {

	private final Map<Z, CardAssignment<Z, C>> assignments = new HashMap<>();
	private final double combinations;
	
	public CardSolution(List<ZoneRule<Z, C>> results) {
		Map<CardGroup<C>, Integer> positioned = new HashMap<>();
		
		for (ZoneRule<Z, C> rule : results) {
			assignments.put(rule.getZone(), rule.getAssignments());
			
			for (Entry<CardGroup<C>, Integer> ee : rule.getAssignments().getAssigns().entrySet()) {
				int value = ee.getValue();
				if (value == 0)
					continue; // Booooooring!
				if (!positioned.containsKey(ee.getKey())) {
					/* 8 cards (4+4), 2 zones (4+4) ---> ZomisUtils.NNKKwithDiv(8, 4, 4, x)
					 * 8 cards (5+3), 2 zones (4+4) ---> ZomisUtils.NNKKwithDiv(8, 5/3, 4, x)
					 * 9 cards (3+3+3), 3 zones (3+3+3) ---> ZomisUtils.NNKKwithDiv(9, 3, 3, x)
					 * 
					 * */
				}
				
				
//				ZomisUtils.nCr(n, r);
			}
			
		}
		this.combinations = 0;
	}
	
	public Map<Z, CardAssignment<Z, C>> getAssignments() {
		return assignments;
	}

	public boolean validCheck() {
		for (Entry<Z, CardAssignment<Z, C>> ee : assignments.entrySet()) {
			int zoneSize = ee.getKey().size();
			int assigns = ee.getValue().getTotalAssignments();
			if (zoneSize != assigns)
				return false;
		}
		
		return true;
	}
	
}
