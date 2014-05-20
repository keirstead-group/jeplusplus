package uk.ac.imperial.jeplusplus;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JEPlusControllerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSetJarPath() {
		
		// Try with a bad path first
		try {
			JEPlusController.setJarPath("d:/blahblah.jar");
			fail("Shouldn't be here");
		} catch (Exception e) {
		}
				
		// Then try with a good path
		try {
			JEPlusController.setJarPath("d:/software/jEPlus_v1.5_pre_05/jEPlus.jar");
		} catch (Exception e) {
			fail("Shouldn't be here");
		}
		
		
	}

}
