package uk.ac.imperial.jeplusplus.parameters;

/**
 * Describes a jEPlus parameter range. At the moment, the code doesn't support
 * the full creation and manipulation of Parameter objects within the tree. But
 * this class provides an easy way to specify common parameter types and ensure
 * that fixed parameter values are acceptable.
 * 
 * @param V
 *            the class of object in the parameter range
 * @author James Keirstead
 * 
 */
public abstract class ParameterRange<V> {

	/**
	 * Gets the index value corresponding to a specified object within this
	 * ParameterRange.
	 * 
	 * @param o
	 *            the object in the ParameterRange
	 * 
	 * @return an int giving the corresponding index
	 * 
	 * @throws IllegalArgumentException
	 *             if o is an invalid object for this ParameterRange
	 */
	public int getFixedParameterValue(V o) throws IllegalArgumentException {
		if (isValid(o)) {
			return getObjectIndex(o);
		} else {
			String msg = String.format(
					"%s out of range for this ParameterRange", o.toString());
			throw new IllegalArgumentException(msg);
		}
	}

	/**
	 * Gets the index of a particular object within this ParameterRange.
	 * 
	 * @param o
	 *            the object to check
	 * @return the index of <code>o</code> in this ParameterRange if present;
	 *         else 0. Valid indices are 1-indexed.
	 */
	private int getObjectIndex(V o) {
		V[] vals = getValues();

		for (int i = 0; i < vals.length; i++) {
			if (o.equals(vals[i])) {
				return i + 1;
			}
		}

		return 0;
	}

	/**
	 * Checks if a specified object is a valid member of this ParameterRange.
	 * 
	 * @param o
	 *            the object to check
	 * @return true if <code>o</code> is a member of this ParameterRange; else
	 *         false
	 */
	public boolean isValid(V o) {
		V[] vals = getValues();

		for (int i = 0; i < vals.length; i++) {
			if (o.equals(vals[i]))
				return true;
		}

		return false;
	}

	/**
	 * Gets all of the values in this ParameterRange
	 * 
	 * @return an Array of objects
	 */
	public abstract V[] getValues();

}
