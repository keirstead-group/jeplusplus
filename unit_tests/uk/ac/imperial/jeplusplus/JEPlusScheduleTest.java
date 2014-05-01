package uk.ac.imperial.jeplusplus;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JEPlusScheduleTest {

	JEPlusSchedule sched;
	
	@Before
	public void setUp() throws Exception {
		sched = new JEPlusSchedule(new File("schedule.txt"));
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
