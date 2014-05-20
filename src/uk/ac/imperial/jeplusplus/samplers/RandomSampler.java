package uk.ac.imperial.jeplusplus.samplers;

/**
 * Describes a random sampling method for jEPlus.
 * 
 * This method uses naive random sampling. If you want to use Latin hypercube
 * sampling, then please use {@link LHSSampler} instead.
 * 
 * @author James Keirstead
 * 
 */
public class RandomSampler extends JEPlusSampler {

	private int nSamples;
	private Integer seed = null;

	/**
	 * Creates a new RandomSampler object specifying the number of samples to
	 * run
	 * 
	 * @param n
	 *            a positive integer giving the number of samples to run. If
	 *            <code>n<=0</code>, the number of samples is set to 1.
	 */
	public RandomSampler(int n) {
		this.nSamples = (n > 0) ? n : 1;
	}

	/**
	 * Creates a new RandomSampler object specifying the number of samples to
	 * run and the random number seed
	 * 
	 * @param n
	 *            a positive integer giving the number of samples to run. If
	 *            <code>n<=0</code>, the number of samples is set to 1.
	 * @param seed
	 */
	public RandomSampler(int n, int seed) {
		this(n);
		this.seed = new Integer(seed);
	}

	@Override
	public String toString() {
		String s = null;
		if (seed == null) {
			s = String.format("%s %d", getFlag(), nSamples);
		} else {
			s = String.format("%s %d -seed %s", getFlag(), nSamples,
					seed.toString());
		}
		return s;
	}

	/**
	 * Gets the keyword flag for this RandomSampler object.
	 * 
	 * @return -sample
	 */
	protected String getFlag() {
		return "-sample";
	}
}
