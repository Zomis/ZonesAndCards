package net.zomis.cards.analyze2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListSplit<T> {

	private final List<T>	onlyA;
	private final List<T>	both;
	private final List<T>	onlyB;

	public ListSplit(List<T> onlyA, List<T> both, List<T> onlyB) {
		this.onlyA = onlyA;
		this.onlyB = onlyB;
		this.both = both;
	}

	public List<T> getBoth() {
		return both;
	}
	
	public List<T> getOnlyA() {
		return onlyA;
	}
	
	public List<T> getOnlyB() {
		return onlyB;
	}
	
	@Override
	public String toString() {
		return "ListSplit:" + onlyA + " -- " + both + " -- " + onlyB;
	}
	

	public static <T> ListSplit<T> split(List<T> a, List<T> b) {
		if (a == b)
			return null;
		
		if (Collections.disjoint(a, b)) 
			return null; // Return if the groups have no fields in common
		
		List<T> both = new ArrayList<>(a);
		List<T> onlyA = new ArrayList<>(a);
		List<T> onlyB = new ArrayList<>(b);
		both.retainAll(b);
		onlyA.removeAll(both);
		onlyB.removeAll(both);

		if (onlyA.isEmpty() && onlyB.isEmpty()) {
			// inf-loop occoured because we're creating a NEW object all the time to hold them both. We should reuse one of the existing ones and go back to using == above.
//			a.add(a);
			both = a;
		}
		else; // a.add(both);
		
		
		return new ListSplit<T>(onlyA, both, onlyB);
	}

	public boolean splitPerformed() {
		return !onlyA.isEmpty() || !onlyB.isEmpty();
	}
	
}
