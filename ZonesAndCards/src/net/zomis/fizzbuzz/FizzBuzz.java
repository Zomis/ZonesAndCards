package net.zomis.fizzbuzz;

import java.util.LinkedList;
import java.util.List;

public class FizzBuzz {
	
	protected List<IFizz> fizzes;
	
	public FizzBuzz() {
		this.fizzes = new LinkedList<IFizz>();
	}

	public void addFizz(IFizz fizz) {
		this.fizzes.add(fizz);
	}
	
	public void perform(int min, int max) {
		for (int i = min; i <= max; i++) {
			handleNumber(i);
		}
	}

	protected void handleNumber(int i) {
		StringBuilder str = new StringBuilder();
		for (IFizz fizz : fizzes) {
			if (fizz.is(i)) {
				str.append(fizz.print(i));
			}
		}
		if (str.length() == 0)
			out(i);
		else out(str.toString());
	}

	protected void out(int i) {
		System.out.println(i);
	}
	protected void out(String str) {
		System.out.println(str);
	}
	
	
}
