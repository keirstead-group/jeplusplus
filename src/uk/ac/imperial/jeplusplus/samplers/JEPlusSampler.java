package uk.ac.imperial.jeplusplus.samplers;

/**
 * 
 * Describes a sampling configuration for a JEPlus run.
 * 
 * Since these objects represent command-line options, the only common method is
 * {@link #toString()}.
 * 
 * @author James Keirstead
 * 
 */
public abstract class JEPlusSampler {

	/**
	 * Writes this sampling configuration to a String that can be used as a
	 * command line argument
	 */
	public abstract String toString();

}
