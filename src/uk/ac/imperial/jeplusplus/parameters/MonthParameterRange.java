package uk.ac.imperial.jeplusplus.parameters;

public class MonthParameterRange extends ParameterRange {

	/**
	 * {@inheritDoc}
	 * 
	 * Valid month values are the integers 1 to 12 inclusive.
	 */
	@Override
	protected int getObjectIndex(Object o) {
		int i = (Integer) o;
		return i;
	}

	@Override
	public boolean isValid(Object o) {
		if (o instanceof Integer) {
			int i = (Integer) o;
			return (i>=1 && i<=12);
		} else {
			return false;
		}		
	}

}
