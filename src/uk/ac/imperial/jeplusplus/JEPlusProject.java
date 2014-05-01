package uk.ac.imperial.jeplusplus;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;

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

/**
 * Describes a jEPlus project file
 * 
 * @author James Keirstead
 * 
 */
public class JEPlusProject {

	private Document jep;

	/**
	 * Creates a new JEPlusProject
	 */
	protected JEPlusProject() {
	}

	/**
	 * Creates a new JEPlusProject from a specified directory. It assumes that
	 * there is one *.jep, *.imf, *.mvi, and *.epw file present.
	 * 
	 * @param dir
	 *            a File object giving the directory
	 */
	public JEPlusProject(File dir) {
		this();

		File[] jepFiles = getFileFilter(dir.toPath(), "*.jep");
		File[] idfFiles = getFileFilter(dir.toPath(), "*.imf");
		File[] mviFiles = getFileFilter(dir.toPath(), "*.mvi");
		File[] epwFiles = getFileFilter(dir.toPath(), "*.epw");
		File jep = getSingleFile(jepFiles);
		File idf = getSingleFile(idfFiles);
		File mvi = getSingleFile(mviFiles);
		File epw = getSingleFile(epwFiles);

		try {
			loadTemplate(jep);
			setIDFName(idf.getName());
			setMVIName(mvi.getName());
			setWeatherName(epw.getName());
			setNotes("jEPlus E+ v80 example");
		} catch (Exception e) {
			System.err.println("Unable to set attributes.  Quitting");
			System.exit(1);
		}
	}

	/**
	 * Writes this jePlusProject to file
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
	 * Loads the template project file
	 * 
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

	public Document getProjectXML() {
		return jep;
	}

	/**
	 * Gets a node of length 1 from this JEPlusProject DOM
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
			XPathExpression expr;
			expr = xpath.compile(query);
			NodeList nl = (NodeList) expr.evaluate(jep, XPathConstants.NODESET);

			if (nl.getLength() == 1) {
				return nl.item(0);
			} else {
				System.out.println(String.format(
						"%d nodes found matching '%s'.  Returning null",
						nl.getLength(), query));
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

	protected Node getWeatherNode() {
		return getSingleNode("//void[@property=\"weatherFile\"]//string");
	}

	/*
	 * TODO To set a parameter you need a well-defined template
	 * ("searchString"). Then just find the "valuesString" that follows and set
	 * the fixed value as necessary. That will leave just some of the values to
	 * actually be changed.
	 */

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
	 * Gets a single file from an Array of File objects. If the array contains
	 * more than one file, a warning is generated and the first file returned.
	 * 
	 * @param files
	 * @return
	 */
	private File getSingleFile(File[] files) {
		if (files.length != 1) {
			System.out.println(String.format(
					"Expected 1 file; found %d.  Returning first match.",
					files.length));
		}
		return files[0];
	}

	/**
	 * Sets the value of a fixed parameter within this JEPlusProject
	 * 
	 * @param name
	 *            a String giving the parameter name, e.g. "@@weekday@@"
	 * @param value
	 *            the value to be fixed. Corresponds to the index within the
	 *            specified parameter list.
	 */
	public void setFixedParameterValue(String name, int value) {
		Node n = getFixedParameterNode(name);
		n.getFirstChild().setNodeValue(String.valueOf(value));
	}

	/**
	 * Gets the index of the fixed parameter value. The user needs to know what
	 * order the parameters are specified in and then use this accordingly.
	 * 
	 * @param name
	 * @return
	 */
	protected int getFixedParameterValue(String name) {
		Node n = getFixedParameterNode(name);
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
			XPathExpression expr;
			expr = xpath.compile(query);
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
	 * Unfortunately there is no easy way to do this query. At the moment, you
	 * have to pass it the full name of the node id which can be found by
	 * manually inspecting the *.jep file. With a simple tree: <code>
	 * root > param1 > param2 > param3
	 * </code> you would find the node param2 by searching with
	 * <code>name=ParameterItem2</code>.
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
			XPathExpression expr;
			expr = xpath.compile(query);
			NodeList nl = (NodeList) expr.evaluate(jep, XPathConstants.NODESET);

			if (nl.getLength() == 1) {
				return nl.item(0);
			} else {
				System.out.println(String.format(
						"%d nodes found matching '%s'.  Returning null",
						nl.getLength(), query));
				return null;
			}
		} catch (XPathExpressionException e) {
			return null;
		}
	}
}
