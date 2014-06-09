package net.zomis.cards.analyze2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.zomis.cards.model.CardZone;

public class CardSolution<Z extends CardZone<?>, C> {

	private final Map<Z, CardAssignment<Z, C>> assignments = new HashMap<>();
	
	public CardSolution(List<ZoneRule<Z, C>> results) {
		for (ZoneRule<Z, C> rule : results) {
			assignments.put(rule.getZone(), rule.getAssignments());
		}
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
