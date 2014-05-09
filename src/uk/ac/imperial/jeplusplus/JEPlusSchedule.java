package uk.ac.imperial.jeplusplus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class JEPlusSchedule {

	private File output;
	private static int HOURS = 8760;
	protected static int RESOLUTION = 60;
	protected double[] values = new double[HOURS * RESOLUTION];

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
						HOURS, RESOLUTION);
	}

	public void writeToFile() {
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(
					output)));
			pw.println(getHeader());

			for (int i = 0; i < values.length; i++) {
				pw.println(String.format("%.4f,", values[i]));
			}
			pw.close();
		} catch (Exception e) {
			System.out.println("Unable to print on file");
		}

	}
}
