package uk.ac.imperial.jeplusplus.parameters;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WeekdayParameterRangeTest {

	WeekdayParameterRange wdpr;

	@Before
	public void setUp() throws Exception {
		wdpr = new WeekdayParameterRange();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetObjectIndex() {
		assertEquals(1, wdpr.getFixedParameterValue("Sunday"));
		assertEquals(7, wdpr.getFixedParameterValue("Saturday"));
	}

	@Test
	public void testIsValid() {
		assertTrue(wdpr.isValid("Sunday"));
		assertFalse(wdpr.isValid("blah"));
	}

}
