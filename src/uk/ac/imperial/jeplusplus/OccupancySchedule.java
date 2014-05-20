package uk.ac.imperial.jeplusplus;

import java.io.File;

/**
 * Creates an occupancy schedule.
 * 
 * @author James Keirstead
 * 
 */
public class OccupancySchedule extends EnergyPlusSchedule {

	/**
	 * Creates a new OccupancySchedule object
	 * 
	 * @param output
	 *            the output file
	 */
	public OccupancySchedule(File output) {
		super(output);
	}

	/**
	 * Sets an occupancy schedule assuming that the values provided repeat for
	 * every day of the year.
	 * 
	 * @param values
	 *            an array of occupancy values. No assumption is made on the
	 *            range of these values; it is up to the user to ensure that
	 *            they are appropriate for the type of EnergyPlus Schedule
	 *            object being modelled.
	 */
	public void setDailyOccupancy(double[] values) {

		int expectedValues = EnergyPlusSchedule.RESOLUTION * 24;
		if (values.length != (expectedValues)) {
			String msg = String.format(
					"values must be an array of length %d; %d values provided",
					expectedValues, values.length);
			throw new IllegalArgumentException(msg);
		}

		// Repeat the same schedule for each day
		for (int i = 0; i < 365; i++) {

			// Place the values in the array
			for (int j = 0; j < values.length; j++) {

				int index = i * values.length + j;
				this.values[index] = values[j];
			}
		}
	}
}
