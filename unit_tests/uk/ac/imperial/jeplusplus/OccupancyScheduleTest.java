package uk.ac.imperial.jeplusplus;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OccupancyScheduleTest {

	OccupancySchedule sched;
	
	@Before
	public void setUp() throws Exception {
		sched = new OccupancySchedule(new File("schedule.txt"));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		int max = 60*24;
		double[] values = new double[max];
		
		for (int i = 0; i<max; i++) {
			values[i] = (double) i/max;
		}
		sched.setDailyOccupancy(values);
		sched.writeToFile();
	}

}
