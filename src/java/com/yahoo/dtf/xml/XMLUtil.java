package com.yahoo.dtf.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.yahoo.dtf.exception.ParseException;

public class XMLUtil {
    
    private static DocumentBuilderFactory dbf = 
                                           DocumentBuilderFactory.newInstance();
    
    private static ArrayList<DocumentBuilder> _dbs = 
                                               new ArrayList<DocumentBuilder>();
    
    private static void checkIn(DocumentBuilder db) { 
       synchronized(_dbs) { 
           _dbs.add(db);
       }
       
       dbf.setNamespaceAware(true);
    }
    
    public static Document newDocument() throws ParseException { 
        try {
            return dbf.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new ParseException("Unable to create new Document.",e);
        }
    }
    
    private static DocumentBuilder checkOut() { 
        synchronized(_dbs) { 
	        if ( _dbs.size() != 0 ) { 
	            return _dbs.remove(0);
	        } else {
		        try {
		            return dbf.newDocumentBuilder();
		        } catch (TransformerFactoryConfigurationError e) {
		            throw new RuntimeException("Error intializing transformer.",e);
		        } catch (ParserConfigurationException e) {
		            throw new RuntimeException("Error intializing transformer.",e);
		        }
	        }
        }
    }
    
    public static Document parseXML(String xml) throws ParseException {
        DocumentBuilder db = checkOut();
        try { 
	        ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
	        try { 
	            return db.parse(bais);
	        } catch (SAXException e) { 
	            throw new ParseException("Unable to parse xml.",e);
	        } catch (IOException e) {
	            throw new ParseException("Unable to parse xml.",e);
	        }
        } finally { 
            checkIn(db);
        }
    }
}
