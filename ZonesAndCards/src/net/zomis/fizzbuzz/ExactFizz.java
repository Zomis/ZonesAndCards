package net.zomis.fizzbuzz;

import java.util.Arrays;

public class ExactFizz implements IFizz {

	private int[] values;
	private String	str;
	
	public ExactFizz(String str, int... values) {
		this.str = str;
		this.values = values;
		Arrays.sort(this.values);
	}
	
	@Override
	public boolean is(int value) {
		int index = Arrays.binarySearch(values, value);
		return index >= 0;
	}

	@Override
	public String print(int value) {
		return this.str;
	}

}
