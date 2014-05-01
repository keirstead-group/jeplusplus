package uk.ac.imperial.jeplusplus.parameters;

public class MonthParameterRange extends ParameterRange<Integer> {

	@Override
	public Integer[] getValues() {
		Integer[] i = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
		return i;
	}

}
