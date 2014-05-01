package uk.ac.imperial.jeplusplus;

import java.io.File;

/**
 * Creates an occupancy schedule.
 * 
 * @author James Keirstead
 * 
 */
public class OccupancySchedule extends JEPlusSchedule {

	public OccupancySchedule(File output) {
		super(output);
	}

	public void setDailyOccupancy(double[] values) {
		
		int expectedValues = JEPlusSchedule.RESOLUTION * 24;
		if (values.length!=(expectedValues)) {
			String msg = String.format("values must be an array of length %d; %d values provided", expectedValues, values.length);
			throw new IllegalArgumentException(msg);
		}
		
		for (int i = 0; i < values.length; i++) {
			if (values[i]<0 || values[i]>1) { 
				String msg = String.format("Value %s out of range [0,1]", values[i]);
				throw new IllegalArgumentException(msg);
			}
		}
		// Repeat the same schedule for each day
		for (int i = 0; i<365; i++) {
			
			// Place the values in the array
			for (int j = 0; j < values.length; j++) {
				
				int index = i*values.length +j; 
				this.values[index] = values[j];
			}
		}
	}
}
