package com.yahoo.dtf.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import com.yahoo.dtf.DTFProperties;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.logger.DTFLogger;
import com.yahoo.dtf.xml.ActionParser;

public class ActionFactory {

    protected final static DTFLogger _logger = DTFLogger.getLogger(ActionFactory.class);

    public static Action parseAction(InputStream is,
                                     String filename) throws ParseException {
        return parseAction(is,filename,true,true);
    }
    
    public static Action parseAction(InputStream is,
                                     String filename,
                                     boolean processFunctions,
                                     boolean processReferences) 
                  throws ParseException {
        
        ActionParser parser = new ActionParser(processReferences,
                                               processFunctions);
        parser.setFilename(filename);
        
        XMLReader xr = newXMLReader();
        InputSource source = new InputSource(is);
        xr.setContentHandler(parser);
      
        MyErrorHandler errorHandler = new MyErrorHandler(parser);
        xr.setErrorHandler(errorHandler);
        
        xr.setEntityResolver(new EntityResolver() { 
            public InputSource resolveEntity(String publicId,
                                             String systemId)
                   throws SAXException, IOException {
                String home;
                try {
                    home = Action.getConfig().getProperty(DTFProperties.DTF_HOME);
                } catch (ParseException e) {
                    throw new SAXException("Internal DTF issue resolving property.",e);
                }
                File dtfxsd = new File(home + File.separatorChar + "dtfx.sd");
                FileInputStream fis = new FileInputStream(dtfxsd);
                System.out.println(dtfxsd + " " + dtfxsd.exists());
                InputSource is = new InputSource(fis);
                return is;
            }
        });
        
        try {
            xr.parse(source);
            return (Action) parser.getResult();
        } catch (IOException e) {
            throw new ParseException("Error parsing XML.",e);
        } catch (SAXException e) {
            throw new ParseException("Error parsing XML.",e);
        }
    }
    
    public static class MyErrorHandler implements ErrorHandler {
        
        private ActionParser _parser = null;

        public MyErrorHandler(ActionParser parser) { 
            _parser = parser;
        }
        
        public void error(SAXParseException exception) throws SAXException {
            Locator locator = _parser.getLocator();
            StringBuffer message = new StringBuffer();
            message.append("Error at line: " + locator.getLineNumber());
            message.append(" column: " + locator.getColumnNumber());
            message.append(" with " + exception.getMessage());
            throw new SAXException(message.toString());
        }

        public void fatalError(SAXParseException exception) throws SAXException {
            Locator locator = _parser.getLocator();
            StringBuffer message = new StringBuffer();
            message.append("Error at line: " + locator.getLineNumber());
            message.append(" column: " + locator.getColumnNumber());
            message.append(" with " + exception.getMessage());
            throw new SAXException(message.toString());
        }

        public void warning(SAXParseException exception) throws SAXException {
            _logger.warn("Warning while processing XML.",exception);
        } 
    }

    private static String APACHE_PREFIX = "http://apache.org/xml/features";
    private static String validationFeature = "http://xml.org/sax/features/validation";
    private static String schemaFeature = APACHE_PREFIX + "/validation/schema";

    protected static XMLReader newXMLReader() throws ParseException {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            
            SAXParser saxParser = spf.newSAXParser();
            XMLReader r = saxParser.getXMLReader();
           
            r.setFeature(validationFeature,true);
            r.setFeature(schemaFeature,true);
           
            // Tell SAX where to find the dtf.xsd file
            r.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                          XMLConstants.W3C_XML_SCHEMA_NS_URI);
            String home = Action.getConfig().getProperty(DTFProperties.DTF_HOME);
            r.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource",
                          new File(home + File.separatorChar + "dtf.xsd"));
            return r;
        } catch (SAXException e) {
            throw new ParseException("Unable to get SAXParser.",e);
        } catch (ParserConfigurationException e) {
            throw new ParseException("Unable to get SAXParser.",e);
        }
    }
}
