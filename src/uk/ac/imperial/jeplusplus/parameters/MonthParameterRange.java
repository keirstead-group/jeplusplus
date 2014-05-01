package uk.ac.imperial.jeplusplus.parameters;

public class MonthParameterRange extends ParameterRange<Integer> {

	/**
	 * {@inheritDoc}
	 * 
	 * Valid month values are the integers 1 to 12 inclusive.
	 */
	@Override
	protected int getObjectIndex(Integer o) {
		return o;
	}

	@Override
	public boolean isValid(Integer o) {

		Integer[] vals = getValues();

		for (int i = 0; i < vals.length; i++) {
			if (o == vals[i])
				return true;
		}

		return false;

	}

	@Override
	public Integer[] getValues() {
		Integer[] i = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
		return i;
	}

}
