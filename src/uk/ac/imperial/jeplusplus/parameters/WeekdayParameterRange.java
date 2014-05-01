package uk.ac.imperial.jeplusplus.parameters;

public class WeekdayParameterRange extends ParameterRange<String> {

	@Override
	public String[] getValues() {
		String[] weekdays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
		return weekdays;
	}

}
