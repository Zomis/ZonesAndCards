package net.zomis.cards.iface;


public class TargetOptions {

	private final int min;
	private final int max;
	private final CardFilter2 filter;

	public TargetOptions(int min, int max, CardFilter2 filter) {
		this.min = min;
		this.max = max;
		this.filter = filter;
	}

	public static TargetOptions singleTarget(CardFilter2 filter) {
		return new TargetOptions(1, 1, filter);
	}
	
	public int getMin() {
		return min;
	}
	
	public CardFilter2 getFilter() {
		return filter;
	}
	
	public int getMax() {
		return max;
	}
	
}
