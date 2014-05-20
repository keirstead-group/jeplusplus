package uk.ac.imperial.jeplusplus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import uk.ac.imperial.jeplusplus.samplers.JEPlusSampler;

/**
 * A convenience class for running jEPlus.
 * 
 * @author James Keirstead
 * 
 */
public class JEPlusController {

	private static Logger log = Logger.getLogger(JEPlusController.class
			.getName());

	private static String JAR_PATH = null;
	private final static String CMD_TEMPLATE = "java -jar %s %s";
	private File outdir;

	/**
	 * Creates a new JEPlusController specifying the output directory
	 * 
	 * @param out
	 *            the output directory
	 */
	public JEPlusController(File out) {
		this.outdir = out;
	}

	/**
	 * Runs the jEPlus jar with a specified command. In addition to any results
	 * files, a <code>console.log</code> file will be created to store messages
	 * from the console.
	 * 
	 * @param cmd
	 *            a String giving the arguments to pass to the jEPlus jar
	 * @param working
	 *            the working directory for jEPlus
	 * @param outdir
	 *            the directory in which to store the results
	 * @throws IOException
	 *             if there are problems writing the results to File or the
	 *             jEPlus jar file isn't set.
	 * 
	 */
	private void doRun(String cmd, File working, File outdir)
			throws IOException {

		if (JAR_PATH == null)
			throw new IOException(
					"Path to jEPlus jar not set.  Call setJarPath first.");

		// Build the template
		cmd = String.format(CMD_TEMPLATE, JAR_PATH, cmd);
		log.info(String.format("Running jEPlus with options '%s'", cmd));

		if (!outdir.exists())
			outdir.mkdirs();

		// Create the log
		File logFile = new File(outdir, "console.log");
		PrintWriter bw = new PrintWriter(new FileWriter(logFile));

		Process p;
		try {
			p = Runtime.getRuntime().exec(cmd, null, working);
			p.waitFor();

			// Dump the output into the log
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				bw.println(line);
			}

			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		log.fine("jEPlus complete.");

	}

	/**
	 * Runs the help message for jEPlus and dumps it into console.log
	 * 
	 * @throws IOException
	 */
	public void help() throws IOException {
		doRun("-help", new File("."), outdir);
	}

	/**
	 * Runs all of the jobs within a specified jEPlus job. You must manually
	 * configure the jobs before calling this function.
	 * 
	 * @param job
	 *            a File object describing the *.jep file to run
	 * @param config
	 *            the jEPlus config file specifying the location of Energy+ and
	 *            other software. Must be in the same directory as job.
	 * @param samples
	 *            a JEPlusSample object describing how to sample the jobs
	 * @throws IOException
	 * 
	 */
	public void runJob(JEPlusProject project, File config, JEPlusSampler samples)
			throws IOException {

		// Add the config file to the project
		if (!config.exists())
			throw new FileNotFoundException(config.getCanonicalPath());
		project.addFiles(config);

		// Copy everything to a directory without spaces
		File tmpDir = FileUtils.getTempDirectory();
		tmpDir = new File(tmpDir, "jeplusC");
		Collection<File> files = project.getProjectFiles();
		for (File f : files)
			FileUtils.copyFileToDirectory(f, tmpDir);

		// Tweak the job and config files to reflect this new path
		File tmpConfig = new File(tmpDir, config.getName());
		File tmpJob = new File(tmpDir, "project.jep");
		File tmpOutput = new File(tmpDir, outdir.getName());
		project.writeToFile(tmpJob);

		String cmd = String.format("-job %s %s -cfg %s -output %s",
				tmpJob.toString(), samples.toString(), tmpConfig.toString(),
				tmpOutput.getCanonicalPath());
		doRun(cmd, tmpDir, tmpOutput);

		// Copy the results back
		FileUtils.deleteDirectory(outdir);
		FileUtils.copyDirectory(tmpOutput, outdir);

		// Delete the old directory
		FileUtils.deleteDirectory(tmpDir);
	}

	/**
	 * Sets the path to the jEPlus jar file. This method must be called before
	 * trying to run a jEPlus simulation.
	 * 
	 * @param path
	 *            the path
	 * @throws FileNotFoundException
	 *             if the jar file is not found
	 */
	public static void setJarPath(String path) throws FileNotFoundException {
		File f = new File(path);
		if (f.exists()) {
			JEPlusController.JAR_PATH = path;
		} else {
			throw new FileNotFoundException();
		}
	}
}
