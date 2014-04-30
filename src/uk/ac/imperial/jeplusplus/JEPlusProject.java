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
	private String notes;
	private Document jep;

	public JEPlusProject(File idf, File mvi) {
		// this.idfFile = idf;
		// this.mviFile = mvi;
		notes = "jEPlus E+ v80 example";
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
	 * Gets the Node of this JEPlusProject containing the IDF file name
	 * 
	 * @return an XML Node
	 * @throws XPathExpressionException
	 */
	protected Node getIDFNode() throws XPathExpressionException {
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression expr = xpath
				.compile("//void[@property=\"IDFTemplate\"]//string");
		NodeList nl = (NodeList) expr.evaluate(jep, XPathConstants.NODESET);
		return nl.item(0);
	}

	/**
	 * Gets the Node of this JEPlusProject containing the MVI file name
	 * 
	 * @return an XML Node
	 * @throws XPathExpressionException
	 */
	protected Node getMVINode() throws XPathExpressionException {
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression expr = xpath
				.compile("//void[@property=\"RVIFile\"]//string");
		NodeList nl = (NodeList) expr.evaluate(jep, XPathConstants.NODESET);
		return nl.item(0);
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
