package net.zomis.cards.analyze2;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CardAssignment<Z, C> {

	private final Z zone;
	final Map<CardGroup<C>, Integer>	assigns;

	CardAssignment() {
		this(null, new HashMap<>());
	}
	
	public CardAssignment(Z zone, Map<CardGroup<C>, Integer> assigns) {
		// 2x = ?a + ?bc
		// 2x = 1a + c
		this.zone = zone;
		this.assigns = assigns;
	}
	
	public Z getZone() {
		return zone;
	}
	
	public Map<CardGroup<C>, Integer> getAssigns() {
		return assigns;
	}
	
	public void assign(CardGroup<C> group, int value) {
		if (!assigns.containsKey(group))
			throw new IllegalStateException("Assign does not contain key: " + group);
		if (assigns.get(group) != null)
			throw new IllegalStateException("Group has already been assigned: " + group + " = " + assigns.get(group));
		assigns.put(group, value);
	}
	
	public Set<CardGroup<C>> getGroups() {
		return this.assigns.keySet();
	}
	
	@Override
	public String toString() {
		return "Assign:" + zone + "=" + assigns;
	}

	public int getTotalAssignments() {
		int i = 0;
		for (Integer value : this.assigns.values()) {
			if (value != null)
				i += value;
		}
		
		return i;
	}
	
	
}
