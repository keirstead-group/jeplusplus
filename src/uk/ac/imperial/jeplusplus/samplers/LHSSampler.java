package uk.ac.imperial.jeplusplus.samplers;

/**
 * Describes a Latin hypercube sampler for jEPlus.
 * 
 * @author James Keirstead
 * 
 */
public class LHSSampler extends RandomSampler {

	/**
	 * Creates a new LHSSampler object specifying the number of samples to run
	 * 
	 * @param n
	 *            a positive integer giving the number of samples to run. If
	 *            <code>n<=0</code>, the number of samples is set to 1.
	 */
	public LHSSampler(int n) {
		super(n);
	}

	/**
	 * Creates a new LHSSampler object specifying the number of samples to run
	 * and the random number seed
	 * 
	 * @param n
	 *            a positive integer giving the number of samples to run. If
	 *            <code>n<=0</code>, the number of samples is set to 1.
	 * @param seed
	 */
	public LHSSampler(int n, int seed) {
		super(n, seed);
	}

	/**
	 * Gets the keyword flag for this LHSSampler object.
	 * 
	 * @return -lhs
	 */
	@Override
	protected String getFlag() {
		return "-lhs";
	}

}
