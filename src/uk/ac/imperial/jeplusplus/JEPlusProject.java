package uk.ac.imperial.jeplusplus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Describes a jEPlus project file
 * 
 * @author James Keirstead
 *
 */
public class JEPlusProject {

	private static String JEP_TEMPLATE = "/resources/template.jep";
	private File idfFile;
	private File mviFile;
	private String notes;	
	private Document jep;
	
	public JEPlusProject(File idf, File mvi) {
		this.idfFile = idf;
		this.mviFile = mvi;
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
	protected void loadTemplate() throws ParserConfigurationException, SAXException, IOException {
		
		// Load the template
		InputStream template = this.getClass().getResourceAsStream(JEP_TEMPLATE);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		jep = dBuilder.parse(template);
		
		//optional, but recommended
		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		jep.getDocumentElement().normalize();
					
	}

	public Document getProjectXML() {
		return jep;
	}
}
