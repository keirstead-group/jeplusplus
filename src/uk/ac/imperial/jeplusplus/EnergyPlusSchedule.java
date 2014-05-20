package uk.ac.imperial.jeplusplus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Describes an EnergyPlus schedule object.
 * 
 * This is a convenience class for handling Schedule:File objects within an
 * EnergyPlus model.
 * 
 * @author James Keirstead
 * 
 */
public class EnergyPlusSchedule {

	private File output;
	private static int HOURS = 8760;
	protected static int RESOLUTION = 60;
	protected double[] values = new double[HOURS * RESOLUTION];

	/**
	 * Creates a new EnergyPlusSchedule specifying the output file
	 * 
	 * @param output
	 *            the output file
	 */
	public EnergyPlusSchedule(File output) {
		this.output = output;
	}

	/**
	 * Sets a constant value for this EnergyPlusSchedule
	 * 
	 * @param value
	 *            a value to be used for all time slots within this
	 *            EnergyPlusSchedule
	 */
	public void setConstantValue(double value) {
		for (int i = 0; i < values.length; i++) {
			values[i] = value;
		}
	}

	/**
	 * Gets a header string for this EnergyPlusSchedule
	 * 
	 * @return a String that can be placed at the head of the output file
	 */
	public String getHeader() {
		return String
				.format("# Schedule data for jEPlus at one-minute resolution, must contain %d*%d rows",
						HOURS, RESOLUTION);
	}

	/**
	 * Writes this EnergyPlusSchedule object to file.
	 */
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
