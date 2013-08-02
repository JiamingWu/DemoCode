package xml;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class ValidXsd {

	static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource"; 
	static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema"; 
	
	public static void main(String[] args) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(true);
		factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
		
		factory.setAttribute(JAXP_SCHEMA_SOURCE, ValidXsd.class.getResourceAsStream("xsd/cds_lcd_application.xsd"));
		
		DocumentBuilder parser = factory.newDocumentBuilder();  
		Document doc = parser.parse(ValidXsd.class.getResourceAsStream("lca.xml"));  
		
		System.out.println("valid xml!");
	}

}
