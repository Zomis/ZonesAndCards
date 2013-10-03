package net.zomis.fizzbuzz;

import java.util.ArrayList;
import java.util.LinkedList;

public class OnceFizz extends FizzBuzz {

	private LinkedList<IFizz>	original;

	public OnceFizz() {
	}
	
	@Override
	public void perform(int min, int max) {
		this.original = new LinkedList<IFizz>(this.fizzes);
		super.perform(min, max);
		this.fizzes = this.original;
	}
	
	@Override
	protected void handleNumber(int i) {
		super.handleNumber(i);
		for (IFizz fizz : new ArrayList<IFizz>(this.fizzes))
			if (fizz.is(i))
				this.fizzes.remove(fizz);
	}
	
}
