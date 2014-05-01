package uk.ac.imperial.jeplusplus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class JEPlusSchedule {

	private File output;
	private static int HOURS = 8760;
	private static int resolution = 60;
	private double[] values = new double[HOURS * resolution];

	public JEPlusSchedule(File output) {
		this.output = output;
	}

	public void setConstantValue(double value) {
		for (int i = 0; i < values.length; i++) {
			values[i] = value;
		}
	}

	public String getHeader() {
		return String
				.format("# Schedule data for jEPlus at one-minute resolution, must contain %d*%d rows",
						HOURS, resolution);
	}

	public void writeToFile() {
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(
					output)));
			pw.println(getHeader());

			for (int i = 0; i < values.length; i++) {
				pw.println(String.format("%s,", values[i]));
			}
			pw.close();
		} catch (Exception e) {
			System.out.println("Unable to print on file");
		}

	}
}
