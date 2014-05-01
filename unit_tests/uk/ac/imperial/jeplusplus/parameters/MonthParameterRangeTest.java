package uk.ac.imperial.jeplusplus.parameters;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MonthParameterRangeTest {

	ParameterRange mpr;

	@Before
	public void setUp() throws Exception {
		mpr = new MonthParameterRange();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetObjectIndex() {
		for (int i = 1; i <= 12; i++)
			assertEquals(i, mpr.getObjectIndex(i));
	}

	@Test
	public void testIsValid() {
		for (int i = 1; i <= 12; i++)
			assertTrue(mpr.isValid(i));
	}

}
