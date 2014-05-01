package uk.ac.imperial.jeplusplus.parameters;

/**
 * Describes a ParameterRange. At the moment, the code doesn't support the full
 * creation and manipulation of Parameter objects within the tree. But this
 * class provides an easy way to specify common parameter types and ensure that
 * fixed parameter values are acceptable.
 * 
 * @author admin
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
	 * @throws IllegalArgumentException if o is an invalid object for this ParameterRange
	 */
	public int getFixedParameterValue(V o) throws IllegalArgumentException {
		if (isValid(o)) {
			return getObjectIndex(o);
		} else {
			String msg = String.format("%s out of range for this ParameterRange", o.toString());
			throw new IllegalArgumentException(msg);
		}
	}
	
	/**
	 * Note that these are 1 indexed
	 * @param o
	 * @return
	 */
	private int getObjectIndex(V o) {
		V[] vals = getValues();
		
		for (int i=0; i<vals.length; i++) {
			if (o.equals(vals[i])) {
				return i+1;
			}
		}
		
		return 0;
	}

	public boolean isValid(V o) {
		V[] vals = getValues();

		for (int i = 0; i < vals.length; i++) {
			if (o.equals(vals[i]))
				return true;
		}

		return false;
	}
	
	public abstract V[] getValues();
	
}