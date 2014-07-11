package net.zomis.cards.analyze2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListSplit<T> {

	private final List<T>	onlyA;
	private final List<T>	both;
	private final List<T>	onlyB;

	private ListSplit(List<T> onlyA, List<T> both, List<T> onlyB) {
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
	
	/**
	 * Performs a split if necessary, or returns null if no split was done
	 * 
	 * @param a One of the lists to split
	 * @param b The other list to split
	 * @return A {@link ListSplit} object, or null if the lists refer to the same object or if they have no groups in common
	 */
	public static <T> ListSplit<T> split(List<T> a, List<T> b) {
		if (a == b)
			return null;
		
		if (Collections.disjoint(a, b)) 
			return null; 
		
		List<T> both = new ArrayList<>(a);
		List<T> onlyA = new ArrayList<>(a);
		List<T> onlyB = new ArrayList<>(b);
		both.retainAll(b);
		onlyA.removeAll(both);
		onlyB.removeAll(both);

		// Check if ALL fields are in common
		if (onlyA.isEmpty() && onlyB.isEmpty()) {
			// If this is called in a loop an inf-loop can occur if we don't do this because we're creating a NEW object all the time to hold them both.
			// We should reuse one of the existing ones and go back to using == above.
			both = a;
		}
		
		return new ListSplit<T>(onlyA, both, onlyB);
	}

	public boolean splitPerformed() {
		return !onlyA.isEmpty() || !onlyB.isEmpty();
	}
	
}
