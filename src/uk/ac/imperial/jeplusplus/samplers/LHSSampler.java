package uk.ac.imperial.jeplusplus.samplers;

public class LHSSampler extends RandomSampler {
		
	public LHSSampler(int n) {
		super(n);
	}

	public LHSSampler(int n, int seed) {		
		super(n, seed);
	}
	
	/**
	 * Gets the keyword flag for this RandomSamples object.
	 *  
	 * @return -sample
	 */
	protected String getFlag() {
		return "-lhs";
	}

}
