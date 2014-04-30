package uk.ac.imperial.jeplusplus.samplers;

/**
 * 
 * Describes a sampling configuration for a JEPlus run
 * 
 * @author admin
 * 
 */
public abstract class JEPlusSampler {

	/**
	 * Writes this sampling configuration to a String that can be used as a command line argument
	 */
	public abstract String toString();
	
}
