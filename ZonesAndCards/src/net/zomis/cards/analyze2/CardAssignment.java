package net.zomis.cards.analyze2;

import java.util.Map;
import java.util.Set;

public class CardAssignment<Z, C> {

	private final Z zone;
	final Map<CardGroup<C>, Integer>	assigns;

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
	
	public Set<CardGroup<C>> getGroups() {
		return this.assigns.keySet();
	}
	
	@Override
	public String toString() {
		return "Assign:" + zone + "=" + assigns;
	}
	
	
}
