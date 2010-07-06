package com.yahoo.dtf.range;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.xerces.dom.AttrImpl;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.yahoo.dtf.range.Range;
import com.yahoo.dtf.exception.RangeException;

public class XMLDataRange extends Range {
    
    private static TransformerFactory tf = TransformerFactory.newInstance();
    
    private String expression = null;

    private String xpath = null;
    private long nextcalled = 0;
    private String xml = null;
    
    private List<Node> _nodes = null;
    private ArrayList<Node> _copy = null;
    
    private static DocumentBuilderFactory dbf = null;
    
    static { 
        try {
            // need to set the factory to the sun internal one otherwise it will
            // pick up the xerces which doesn't work correctly at the moment.
            // using the property because of jdk 1.5 compatibility
            System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
                           DocumentBuilderFactoryImpl.class.getCanonicalName());
            dbf = DocumentBuilderFactory.newInstance();
        } catch (TransformerFactoryConfigurationError e) {
            throw new RuntimeException("Error intializing transformer.",e);
        }
    }

    public static boolean matches(String expression) throws RangeException {
        if  ( expression.startsWith("xpath(") ) { 
            if ( expression.endsWith(")") ) { 
                return true;
            }
            throw new RangeException("XPath range is invalid [" + expression + "]");
        }
        return false;
    }
   
    /*
     * Used when transferring ranges between runner and agents.
     */
    public XMLDataRange() { 
        
    }

    private static HashMap<String, XPathExpression> _compiledExpression = 
                                         new HashMap<String, XPathExpression>();
    
    private synchronized XPathExpression compile(String expression) 
            throws XPathExpressionException { 
        XPathExpression xpath = _compiledExpression.get(expression);
        
        if ( xpath == null ) { 
            XPathFactory factory = XPathFactory.newInstance();            
            XPath x = factory.newXPath();
            xpath = x.compile(expression);
            _compiledExpression.put(expression, xpath);
        }
        
        return xpath;
    } 
    
    public XMLDataRange(String expression) throws RangeException {
        this.expression = expression;
        init();
    }
    
    private void init() throws RangeException { 
        String arguments = expression.substring("xpath".length());
        String[] args = arguments.split(",/");
           
        xml = args[0].substring(1);
            
        if ( !args[1].endsWith(")") ) {
            throw new RangeException("XPath expression is invalid [" + 
                                     expression + "]");
        }
        
        ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(bais);
            JXPathContext ctx = JXPathContext.newContext(doc);    
            String aux = args[1].substring(0,args[1].length()-1);
           
            if ( aux.contains(",[") ) { 
                String[] parts = aux.split(",\\[");
                xpath = "/" + parts[0];
                String[] maps = parts[1].replace("]", "").split(",");
                
                for ( String map : maps ) { 
                    String[] nsMap = map.split("=>");
                    ctx.registerNamespace(nsMap[0], nsMap[1]);
                }
            } else { 
                xpath = "/" + aux;
            }
           
            _nodes = ctx.selectNodes(xpath);
        } catch (SAXException e) {
            throw new RangeException("Unable to parse xml.",e);
        } catch (IOException e) {
            throw new RangeException("Unable to parse xml.",e);
        } catch (ParserConfigurationException e) {
            throw new RangeException("Unable to get a new DocumentBuilder.",e);
        }
            
        _copy = new ArrayList<Node>();
        for (int i = 0; i < _nodes.size(); i++) { 
            _copy.add(_nodes.get(i));
        }
    }
    
    public boolean hasMoreElements() {
        return (_copy.size() != 0);
    }
    
    public String nextElement() throws RangeException {
        nextcalled++;
        Node item = _copy.remove(0);

        if (item instanceof AttrImpl) {
            return item.getNodeValue();
        } else { 
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(baos);
            DOMSource source = new DOMSource(item);
            try {
                javax.xml.transform.Transformer transformer = null;
                transformer = tf.newTransformer();
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
                                              "yes");
                transformer.transform(source, result);
            } catch (TransformerException e) {
                throw new RangeException("Unable to handle XML.",e);
            }
            return new String(baos.toByteArray());
        }
    }
    
    public void reset() throws RangeException {
        init();
        nextcalled = 0;
    }
    
    public int size() {
        return _copy.size();
    }
    
    @Override
    public void restoreState(DataInputStream dis) throws RangeException {
        try { 
            expression = dis.readUTF();
            init();
            long calls = dis.readLong();
        
            // move this range into the same position.
            for(long i = 0; i < calls ; i++) 
                nextElement();
        } catch (IOException e) { 
            throw new RangeException("Error suspending state.",e);
        }
    }
    
    @Override
    public void suspendState(DataOutputStream dos) throws RangeException {
        try { 
            dos.writeUTF(expression);
            dos.writeLong(nextcalled);
        } catch (IOException e) { 
            throw new RangeException("Error suspending state.",e);
        }
    }

}
