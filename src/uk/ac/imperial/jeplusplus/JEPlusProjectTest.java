package uk.ac.imperial.jeplusplus;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class JEPlusProjectTest {

	JEPlusProject project;
	
	@Before
	public void setUp() throws Exception {
		project = new JEPlusProject(null, null);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLoadTemplate() {
		try {
			project.loadTemplate();
			Document doc = project.getProjectXML(); 			
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			String output = writer.getBuffer().toString(); 
			System.out.println(output);			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Couldn't load template");
		} 
		
	}

}
