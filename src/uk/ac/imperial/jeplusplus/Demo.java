package uk.ac.imperial.jeplusplus;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A simple demostration of jE++
 * 
 * @author James Keirstead
 * 
 */
public class Demo {

	// Set your path to jEPlus here
	private static String JAR_PATH = "d:/software/jEPlus_v1.5_pre_05/jEPlus.jar";

	public static void main(String[] args) throws IOException {

		// Define the input and output directories
		Path input = Paths.get("demo");
		Path output = input.resolve("output");

		// Tell jE++ where jEPlus lives
		JEPlusController.setJarPath(JAR_PATH);

		// Build the project
		JEPlusProject project = new JEPlusProject(input, output);

		/*
		 * The project file describes a parameter called @@month@@ which is
		 * contained within the object ParameterItem2. Fix this value to July
		 */
		project.setFixedParameterValue("ParameterItem2", 7);
		project.run();

	}

}
