package net.zomis.fizzbuzz;

public class DividableFizz implements IFizz {

	private int	mod;
	private String	str;

	public DividableFizz(int i, String string) {
		this.mod = i;
		this.str = string;
	}

	@Override
	public boolean is(int value) {
		return value % mod == 0;
	}

	@Override
	public String print(int value) {
		return this.str;
	}

}
