package uk.ac.imperial.jeplusplus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import uk.ac.imperial.jeplusplus.samplers.JEPlusSampler;
import uk.ac.imperial.jeplusplus.samplers.RandomSampler;

/**
 * Describes a jEPlus project.
 * 
 * @author James Keirstead
 * 
 */
public class JEPlusProject {

	private static Logger log = Logger.getLogger(JEPlusProject.class.getName());

	private Document jep;
	private ArrayList<File> files;
	private Path indir;
	private Path outdir;

	/**
	 * Creates a new JEPlusProject
	 */
	protected JEPlusProject() {
		files = new ArrayList<File>();
	}

	/**
	 * Creates a new JEPlusProject from a specified directory.
	 * <p>
	 * Assumes that within <code>input</code>, there is one *.jep, *.imf, *.mvi,
	 * and *.epw file present.
	 * 
	 * @param indir
	 *            a Path object giving the input directory
	 * @param outdir
	 *            a Path object giving the output directory
	 */
	public JEPlusProject(Path indir, Path outdir) {
		this();

		this.indir = indir;
		this.outdir = outdir;

		File[] jepFiles = getFileFilter(indir, "*.jep");
		File[] idfFiles = getFileFilter(indir, "*.imf");
		File[] mviFiles = getFileFilter(indir, "*.mvi");
		File[] epwFiles = getFileFilter(indir, "*.epw");
		File jep = getSingleFile(jepFiles);
		File idf = getSingleFile(idfFiles);
		File mvi = getSingleFile(mviFiles);
		File epw = getSingleFile(epwFiles);

		addFiles(jep, idf, mvi, epw);

		try {
			loadTemplate(jep);
			setIDFName(idf.getName());
			setMVIName(mvi.getName());
			setWeatherName(epw.getName());
			setNotes("jEPlus E+ v80 example");
		} catch (Exception e) {
			log.severe("Unable to set attributes.  Quitting");
			System.exit(1);
		}
	}

	/**
	 * Adds an external file to this JEPlusProject.
	 * <p>
	 * Users can specify parameters within the IMF file (and possibly other
	 * files) from external files. This method allows you to specify which files
	 * should be considered part of the project and therefore copy them to the
	 * working directory for analysis.
	 * 
	 * @param files
	 *            one or more File objects that will be copied into the working
	 *            directory when running
	 */
	public void addFiles(File... files) {
		for (File f : files) {
			this.files.add(f);
		}
	}

	/**
	 * Writes this jePlusProject to a specified file
	 * 
	 * @param file
	 *            the file to write the project to
	 */
	public void writeToFile(File file) {

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(jep), new StreamResult(writer));
			String output = writer.getBuffer().toString();

			PrintWriter pw = new PrintWriter(file);
			pw.println(output);
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Loads a specified template project file
	 * 
	 * @param template
	 *            a File object specifying the template *.jep file
	 * @return the project Document
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	protected void loadTemplate(File template)
			throws ParserConfigurationException, SAXException, IOException {

		// Load the template
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		jep = dBuilder.parse(template);

		// optional, but recommended
		// read this -
		// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		jep.getDocumentElement().normalize();
	}

	/**
	 * Gets the XML document describing this JEPlusProject
	 * 
	 * @return an XML Document object
	 */
	public Document getProjectXML() {
		return jep;
	}

	/**
	 * Gets a node of length 1 from this JEPlusProject DOM matching a specified
	 * search query
	 * 
	 * @param query
	 *            an xpath search query
	 * 
	 * @return the Node specified, if <code>query</code> matches a NodeList of
	 *         length 1. Otherwise a warning is printed and null is returned.
	 */
	private Node getSingleNode(String query) {
		try {
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile(query);
			NodeList nl = (NodeList) expr.evaluate(jep, XPathConstants.NODESET);

			if (nl.getLength() == 1) {
				return nl.item(0);
			} else {
				String msg = String.format(
						"%d nodes found matching '%s'.  Returning null",
						nl.getLength(), query);
				log.warning(msg);
				return null;
			}
		} catch (XPathExpressionException e) {
			return null;
		}
	}

	/**
	 * Gets the Node of this JEPlusProject containing the IDF file name
	 * 
	 * @return an XML Node
	 */
	protected Node getIDFNode() {
		String query = "//void[@property=\"IDFTemplate\"]//string";
		return getSingleNode(query);
	}

	/**
	 * Gets the Node of this JEPlusProject containing the MVI file name
	 * 
	 * @return an XML Node
	 */
	protected Node getMVINode() {
		String query = "//void[@property=\"RVIFile\"]//string";
		return getSingleNode(query);
	}

	/**
	 * Sets the name of the IDF file for this JEPlusProject
	 * 
	 * @param idf
	 *            a String giving the filename. This should be a relative path
	 *            and assumes that the file is located in the same directory as
	 *            the project file.
	 * @throws XPathExpressionException
	 */
	public void setIDFName(String idf) throws XPathExpressionException {
		Node n = getIDFNode().getFirstChild();
		n.setNodeValue(idf);
	}

	/**
	 * Sets the name of the MVI file for this JEPlusProject
	 * 
	 * @param mvi
	 *            a String giving the filename. This should be a relative path
	 *            and assumes that the file is located in the same directory as
	 *            the project file.
	 */
	public void setMVIName(String mvi) {
		Node n = getMVINode().getFirstChild();
		n.setNodeValue(mvi);
	}

	/**
	 * Sets the project notes
	 * 
	 * @param notes
	 *            a String giving the project notes.
	 * 
	 */
	public void setNotes(String notes) {
		Node n = getNotesNode();
		n.getFirstChild().setNodeValue(notes);
	}

	/**
	 * Gets the XML node containing the notes field
	 * 
	 * @return the notes node if found, else null
	 */
	protected Node getNotesNode() {
		return getSingleNode("//void[@property=\"projectNotes\"]//string");
	}

	/**
	 * Sets the name of the weather file for this JEPlusProject
	 * 
	 * @param weather
	 *            a String giving the filename. This should be a relative path
	 *            and assumes that the file is located in the same directory as
	 *            the project file.
	 */
	public void setWeatherName(String weather) {
		Node n = getWeatherNode();
		n.getFirstChild().setNodeValue(weather);
	}

	/**
	 * Gets the XML node containing the weather field
	 * 
	 * @return the weather node if found, else null
	 */
	protected Node getWeatherNode() {
		return getSingleNode("//void[@property=\"weatherFile\"]//string");
	}

	/**
	 * Gets a list of files from a directory matching a pattern
	 * 
	 * @param path
	 *            a Path object specifying the directory to search
	 * @param string
	 *            a String giving the search pattern
	 * 
	 * @return an Array of matching File objects
	 */
	private File[] getFileFilter(Path path, String string) {
		File dir = path.toFile();
		FileFilter filter = new WildcardFileFilter(string);
		return dir.listFiles(filter);
	}

	/**
	 * Gets a single file from an Array of File objects.
	 * 
	 * @param files
	 *            an array of multiple files
	 * 
	 * @return the only file within <code>files</code> if the length is one,
	 *         else returns the first file in the array.
	 */
	private File getSingleFile(File[] files) {
		if (files.length != 1) {
			log.warning(String.format(
					"Expected 1 file; found %d.  Returning first match.",
					files.length));
		}
		return files[0];
	}

	/**
	 * Sets the value of a fixed parameter within this JEPlusProject. Currently
	 * you have to manually search the \code{jep} file in order to find the id
	 * reference of the object node containing the parameter of interest.
	 * 
	 * @param name
	 *            a String giving the parameter name, e.g. "@@month@@"
	 * @param value
	 *            the value to be fixed. Corresponds to the index within the
	 *            specified parameter list.
	 */
	public void setFixedParameterValue(String name, int value) {
		String container = getContainingObjectId(name);
		Node n = getFixedParameterNode(container);
		n.getFirstChild().setNodeValue(String.valueOf(value));
	}

	/**
	 * Gets the index of the fixed parameter value. The user needs to know what
	 * order the parameters are specified in and then use this accordingly.
	 * 
	 * @param name
	 *            the jEPlus parameter name, e.g. "@@month@@"
	 * @return the value of the parameter
	 */
	protected int getFixedParameterValue(String name) {
		String container = getContainingObjectId(name);
		Node n = getFixedParameterNode(container);
		int value = Integer.valueOf(n.getFirstChild().getNodeValue());
		return value;
	}

	/**
	 * Gets the value node for the fixed parameter value
	 * 
	 * @param name
	 *            the parameter name
	 * @return the node containing the index value
	 */
	protected Node getFixedParameterNode(String name) {
		Node n = getParameterNode(name);
		try {
			String query = "void[@property=\"selectedAltValue\"]";
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile(query);
			NodeList nl = (NodeList) expr.evaluate(n, XPathConstants.NODESET);
			if (nl.getLength() == 1) {
				return nl.item(0).getChildNodes().item(1);
			}
		} catch (XPathExpressionException e) {

		}
		return null;
	}

	/**
	 * Gets a named ParameterNode from this JEPlusProject.
	 * <p>
	 * This needs to be the name of the object idref, e.g. ParameterItem1. This
	 * can be found using {@link #getContainingObjectId(String)} and passing it
	 * the jEPlus parameter code.
	 * 
	 * @param name
	 *            the id field of the Node as described above.
	 * 
	 * @return the Node if found; else 0.
	 */
	protected Node getParameterNode(String name) {
		try {
			String query = String.format("//object[@id='%s']", name);
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile(query);
			NodeList nl = (NodeList) expr.evaluate(jep, XPathConstants.NODESET);

			if (nl.getLength() == 1) {
				return nl.item(0);
			} else {
				log.warning(String.format(
						"%d nodes found matching '%s'.  Returning null",
						nl.getLength(), query));
				return null;
			}
		} catch (XPathExpressionException e) {
			return null;
		}
	}

	/**
	 * Gets the id of the object node containing a specified jEPlus parameter
	 * name
	 * 
	 * @param parameter
	 *            the parameter name
	 * @return the idref attribute value of the containing object node
	 */
	protected String getContainingObjectId(String parameter) {
		try {
			String query = String.format("//string[.='%s']", parameter);
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile(query);
			NodeList nl = (NodeList) expr.evaluate(jep, XPathConstants.NODESET);

			if (nl.getLength() == 1) {
				nl = nl.item(0).getParentNode().getChildNodes();
				query = "./object";
				expr = xpath.compile(query);
				nl = (NodeList) expr.evaluate(nl, XPathConstants.NODESET);
				Node n = nl.item(0);
				String s = n.getAttributes().getNamedItem("idref")
						.getNodeValue();
				return s;
			} else {
				log.warning(String.format(
						"%d nodes found matching '%s'.  Returning null",
						nl.getLength(), query));
				return null;
			}
		} catch (XPathExpressionException e) {
			return null;
		}
	}

	/**
	 * Gets all of the files associated with this JEPlusProject
	 * 
	 * @return a Collection of File objects
	 */
	public Collection<File> getProjectFiles() {
		return files;
	}

	/**
	 * Runs this JEPlusProject.
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 * 
	 */
	public void run() throws FileNotFoundException, IOException {

		// Define the controllers and run the job
		JEPlusController controller = new JEPlusController(outdir.toFile());
		File config = indir.resolve("jeplus_v80.cfg").toFile();
		JEPlusSampler sampler = new RandomSampler(1);
		controller.runJob(this, config, sampler);
	}

	/**
	 * Scales the JEPlus results by a multiplicative factor. All columns in the
	 * JEPlus output file will be scaled.
	 * 
	 * @param factor
	 *            a scaling factor
	 * @throws FileNotFoundException
	 *             if unable to find the results file
	 * @throw IOException if unable to write the scaled results
	 */
	public void scaleResults(double factor) throws IOException {

		File rawFile = outdir.resolve("SimResults.csv").toFile();

		if (!rawFile.exists()) {
			throw new FileNotFoundException("Unable to find the results file.");
		}

		// Load the results
		InputStream is = new FileInputStream(rawFile);
		CSVReader reader = new CSVReader(new InputStreamReader(is));
		List<String[]> rawLines = reader.readAll();
		reader.close();

		// Write the results to the new file applying the scaling
		int nHeaderRows = 1;
		File scaledFile = outdir.resolve("SimResults-scaled.csv").toFile();
		Writer output = new BufferedWriter(new FileWriter(scaledFile));
		CSVWriter writer = new CSVWriter(output);
		int lineCount = 0;
		for (String[] s : rawLines) {
			lineCount++;
			if (lineCount <= nHeaderRows) {
				writer.writeNext(s);
			} else {
				for (int i = 3; i < s.length; i++) {
					double val = Double.valueOf(s[i]);
					s[i] = String.valueOf(val * factor);
				}
				writer.writeNext(s);
			}
		}
		writer.close();

	}

}
