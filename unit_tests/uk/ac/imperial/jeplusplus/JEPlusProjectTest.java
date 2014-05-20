package uk.ac.imperial.jeplusplus;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

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
import org.w3c.dom.NodeList;

public class JEPlusProjectTest {

	JEPlusProject project;
	Path in = Paths.get("demo");
	Path out = in.resolve("output");
	
	@Before
	public void setUp() throws Exception {
		JEPlusController.setJarPath("D:/software/jEPlus_v1.5_pre_05/jEPlus.jar");
		project = new JEPlusProject();
		File template = new File("demo/HVACTemplate-5ZoneFanCoil.jep");
		project.loadTemplate(template);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testWriteToFile() {
		File f = new File("demo/hello.jep");
		project.writeToFile(f);
		assertTrue(f.exists());
		f.delete();
		
	}
	

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
	
	@Test
	public void testSetWeatherName() {
		String weather = "weather_file.epw";
		project.setWeatherName(weather);
		Node n = project.getWeatherNode();
		assertEquals(weather, n.getFirstChild().getNodeValue());
	}
	
	@Test
	public void testGetParameterNode() {
		Node n = project.getParameterNode("ParameterItem2");
		NodeList nl = n.getChildNodes();
		for (int i =0; i<nl.getLength(); i++) {
			n = nl.item(i);
			System.out.print(i + " = " + n);			
			System.out.println(nl.item(i).getTextContent());
		}
	}
	
	@Test
	public void testGetFixedParameterValue() {		
		int value = project.getFixedParameterValue("ParameterItem2");
		assertEquals(1, value);
	}
	
	@Test
	public void testSetParameterNode() {
		
		project.setFixedParameterValue("ParameterItem2", 3);
		assertEquals(3, project.getFixedParameterValue("ParameterItem2"));

	}
	
	@Test
	public void testRun() {		
		
		project = new JEPlusProject(in, out);
		try {
			project.run();
		} catch (IOException e) {
			fail(e.getMessage());			
		}
	}
	
	@Test
	public void testScaleResults() {		
		project = new JEPlusProject(in, out);
		try {
			project.run();
			project.scaleResults(2);
		} catch (IOException e) {
			fail(e.getMessage());			
		}
	}
}
