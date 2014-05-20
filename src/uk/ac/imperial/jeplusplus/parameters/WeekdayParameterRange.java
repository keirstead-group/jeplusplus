package uk.ac.imperial.jeplusplus.parameters;

/**
 * Describes a range of valid weekday values. Sunday is the first entry in the
 * range.
 * 
 * @author James Keirstead
 * 
 */
public class WeekdayParameterRange extends ParameterRange<String> {

	@Override
	public String[] getValues() {
		String[] weekdays = { "Sunday", "Monday", "Tuesday", "Wednesday",
				"Thursday", "Friday", "Saturday" };
		return weekdays;
	}

}
