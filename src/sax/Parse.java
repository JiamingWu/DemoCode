package sax;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


public class Parse {
	
	public static void main(String[] args) {
		Parse t = new Parse();
		List<CatalogVO> vs = t.getCatalogs();
		System.out.println("==");
		for (CatalogVO v : vs) {
			System.out.println(v.getIcon() + "," + v.getTitle() + "," + v.getDesc() + "," + v.getPlaylist() + "," + v.getSubs().size());
		}
	}

	private List<CatalogVO> getCatalogs() {
    	try {
	    	SAXParserFactory spf = SAXParserFactory.newInstance();
		    SAXParser sp = spf.newSAXParser();
			XMLReader reader = sp.getXMLReader();
			List<CatalogVO> records = new ArrayList<CatalogVO>();
			ContentHandler contentHandler = new RecordContentHandler(records);
			reader.setContentHandler(contentHandler);
			reader.parse(new InputSource(Parse.class.getResourceAsStream("catalog.xml")));
			return records;
    	} catch (Exception e) {
    		e.printStackTrace();
    		return new ArrayList<CatalogVO>();
    	}
    }
    
    class RecordContentHandler implements ContentHandler {

		private StringBuffer buf = new StringBuffer();
		private String tag = null;
		private List<CatalogVO> catalogs = null;
		private Map<String, String> atts = new HashMap<String, String>();
		private CatalogVO parent = null;
		private CatalogVO curr = null;
		
		RecordContentHandler(List<CatalogVO> records) {
			this.catalogs = records;
		}
		
		public void characters(char[] chars, int start, int length)
				throws SAXException {
			buf.append(chars,start,length);
		}
		
		public void startElement(String namespaceURI, String localName, String fullName,
				Attributes attributes) throws SAXException {
			buf.setLength(0);
			tag = fullName;
			for (int i=0; i<attributes.getLength(); i++) {
				atts.put(attributes.getLocalName(i), attributes.getValue(i));
			}
			if (tag.equals("item")) {
				curr = new CatalogVO();
				curr.setIcon(atts.get("icon"));	
				catalogs.add(curr);
				parent = curr;
			}
			if (tag.equals("subitem")) {
				CatalogVO sub = new CatalogVO();
				sub.setIcon(atts.get("icon"));	
				parent.addSub(sub);
				curr = sub;
			}
		}

		public void endElement(String namespaceURI, String localName, String fullName)
				throws SAXException {
			String text = buf.toString();
			if (tag.equals("title")) {
				curr.setTitle(text);
			}
			if (tag.equals("desc")) {
				curr.setDesc(text);
			}
			if (tag.equals("playlist")) {
				curr.setPlaylist(text);
			}
			tag = "";
			atts.clear();
		}

		public void endDocument() throws SAXException {
			
		}

		public void endPrefixMapping(String arg0) throws SAXException {
			
		}

		public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
				throws SAXException {
			
		}

		public void processingInstruction(String arg0, String arg1)
				throws SAXException {
			
		}

		public void setDocumentLocator(Locator arg0) {
			
		}

		public void skippedEntity(String arg0) throws SAXException {
			
		}

		public void startDocument() throws SAXException {
			
		}

		public void startPrefixMapping(String prefix, String uri)
				throws SAXException {
			
		}
		
	}
}
