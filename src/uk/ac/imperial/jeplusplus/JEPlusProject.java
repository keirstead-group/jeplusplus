package uk.ac.imperial.jeplusplus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

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

	private static String JEP_TEMPLATE = "/resources/template.jep";
	private Document jep;

	protected JEPlusProject() {
		try {
			loadTemplate();
		} catch (Exception e) {
			System.err.println("Unable to find project template. Quitting.");
			System.exit(1);
		}
	}

	public JEPlusProject(File idf, File mvi) {
		this();
		if (idf == null || mvi == null) {
			throw new IllegalArgumentException("idf and mvi must be non-null");
		}
		try {
			setIDFName(idf.getName());
			setMVIName(mvi.getName());
			// setNotes("jEPlus E+ v80 example");
		} catch (XPathExpressionException e) {
			System.err.println("Unable to set attributes.  Quitting");
			System.exit(1);
		}

	}

	/**
	 * Writes this jePlusProject to file
	 */
	public void writeToFile(File jep) {

	}

	/**
	 * Loads the template project file
	 * 
	 * @return the project Document
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	protected void loadTemplate() throws ParserConfigurationException,
			SAXException, IOException {

		// Load the template
		InputStream template = this.getClass()
				.getResourceAsStream(JEP_TEMPLATE);
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
	 * @param idf
	 *            a String giving the filename. This should be a relative path
	 *            and assumes that the file is located in the same directory as
	 *            the project file.
	 * @throws XPathExpressionException
	 */
	public void setMVIName(String idf) throws XPathExpressionException {
		Node n = getMVINode().getFirstChild();
		n.setNodeValue(idf);
	}

}
