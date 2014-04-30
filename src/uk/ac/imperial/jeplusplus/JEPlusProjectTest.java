package uk.ac.imperial.jeplusplus;

import static org.junit.Assert.*;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class JEPlusProjectTest {

	JEPlusProject project;

	@Before
	public void setUp() throws Exception {
		project = new JEPlusProject();
		project.loadTemplate();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLoadTemplate() {
		try {
			Document doc = project.getProjectXML();
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
					"yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			String output = writer.getBuffer().toString();
			System.out.println(output);
		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testGetIDFNode() {
		try {
			Node n = project.getIDFNode();			
			String fileName = "HVACTemplate-5ZoneFanCoil.imf";
			assertEquals(fileName, n.getFirstChild().getNodeValue());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSetIDFName() {		
		try {
			String idfName = "test.idf";
			project.setIDFName(idfName);
			Node n = project.getIDFNode();			
			assertEquals(idfName, n.getFirstChild().getNodeValue());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetMVINode() {
		try {
			Node n = project.getMVINode();			
			String fileName = "HVACTemplate-5ZoneFanCoil.mvi";
			assertEquals(fileName, n.getFirstChild().getNodeValue());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSetMVIName() {		
		try {
			String mviName = "test.mvi";
			project.setMVIName(mviName);
			Node n = project.getMVINode();			
			assertEquals(mviName, n.getFirstChild().getNodeValue());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testSetNotes() {
		String notes = "My project notes";
		project.setNotes(notes);
		Node n = project.getNotesNode();
		assertEquals(notes, n.getFirstChild().getNodeValue());
	}
}
