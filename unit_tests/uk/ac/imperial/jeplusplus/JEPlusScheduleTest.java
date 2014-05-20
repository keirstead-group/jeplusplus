package uk.ac.imperial.jeplusplus;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JEPlusScheduleTest {

	EnergyPlusSchedule sched;
	
	@Before
	public void setUp() throws Exception {
		sched = new EnergyPlusSchedule(new File("schedule.txt"));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		sched.setConstantValue(117.239997864);
		sched.writeToFile();
	}

}
