package net.zomis.cards.util;

import net.zomis.ZomisUtils;

public class CardCalculation {

	private final int available;
	private final int matchingCount;

	public CardCalculation(int available, int matchingCount) {
		this.available = available;
		this.matchingCount = matchingCount;
	}
	
	// TODO: calcRange(min, max) <-- use for example to find probability of between 3 and 6 hearts on hand when playing Hearts.
	public CardCalculation setMin(int min) {
		return new CardCalculation(available, matchingCount);
	}
	
	public double[] calcProbabilities(int pick) {
		double[] array = new double[pick];
		for (int i = 0; i < array.length; i++) {
			array[i] = NNKK(this.available, matchingCount, pick, i);
		}
		array = calculateProbabilities(array);
		return array;
	}
	
	private double[] calculateProbabilities(double[] array) {
		double[] result = new double[array.length];
		double sum = sum(array);
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i] / sum;
		}
		return result;
	}

	private double sum(double[] array) {
		double total = 0;
		for (double d : array)
			total += d;
		return total;
	}
	
	public double nnkk() {
		return 0;
	}
	
	public static double NNKK(int N, int n, int K, int k) {
//		Zomis.echo(String.format("NNKK %d %d %d %d", N, n, K, k)); // ????: (doubt it) Would buffering the values improve speed?
		return ZomisUtils.nCr(K, k) * ZomisUtils.nCr(N - K, n - k); //	/ RootAnalyze.nCr(N, n)
	}

}
