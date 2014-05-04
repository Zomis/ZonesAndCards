package net.zomis.aiscore.genetics;

import java.util.Set;

import net.zomis.aiscores.ScoreConfig;

public class Genetics<Params, Field> {

//	private final Set<ScoreConfig<Params, Field>> configs;
	
	public Genetics(Set<ScoreConfig<Params, Field>> configs) {
//		this.configs = new HashSet<ScoreConfig<Params,Field>>(configs);
		/* chromosome = array of values (genes)
		 * Input: list/array/set of chromosomes
		 * 
		 * - create copies of the genes
		 * 
		 * for each config, a:
		 *   pick another config, b:
		 * - fight(a, b)
		 * -- add best to mating pool.
		 * 
		 * - create exact copies of the ones in the mating pool
		 * - select pairs of those in mating pool
		 *  ? perform some crossover - swap some scorer options
		 *  - mutate some of the scorers, assign completely new values. Randomly increase or decrease value. Don't generate a completely new one?
		 * 
		 * 
		 * Output: new array of values (genes)
		 * */
	}
	
}
