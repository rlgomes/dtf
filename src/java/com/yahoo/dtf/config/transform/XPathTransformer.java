package com.yahoo.dtf.config.transform;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.xerces.dom.DeferredAttrImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.xml.XMLUtil;

public class XPathTransformer implements Transformer {
    
    private static TransformerFactory tf = TransformerFactory.newInstance();

    private static ArrayList<javax.xml.transform.Transformer> _transformers = 
        new ArrayList<javax.xml.transform.Transformer>();

	private void checkIn(javax.xml.transform.Transformer transformer) { 
		synchronized(_transformers) { 
		_transformers.add(transformer);
		}
	}
    private javax.xml.transform.Transformer checkOut() { 
        synchronized(_transformers) { 
            if ( _transformers.size() != 0 ) { 
                return _transformers.remove(0);
            } else {
                try {
                    javax.xml.transform.Transformer transformer =
                                                            tf.newTransformer();
                    transformer.
                      setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                    return transformer;
                } catch (TransformerConfigurationException e) {
                    throw new RuntimeException("Error intializing transformer.",e);
                } catch (TransformerFactoryConfigurationError e) {
                    throw new RuntimeException("Error intializing transformer.",e);
                }
            }
        }
    }
    
    public String apply(String data, String expression) throws ParseException {
        Document document = XMLUtil.parseXML(data);
        JXPathContext ctx = JXPathContext.newContext(document);
        List<Node> nodes = ctx.selectNodes(expression);
        javax.xml.transform.Transformer transformer = checkOut();
	    try { 
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(baos);
           
            if ( nodes.size() == 1 ) {
                Object obj = nodes.get(0);
                if ( obj instanceof DeferredAttrImpl ) { 
	                return ((DeferredAttrImpl)obj).getValue();
                } else if ( !(obj instanceof Node) ) { 
                    /*
                     * Basically Integer, Doubles will be handled here
                     */
                    return obj.toString();
                }
            } 

            /*
             * Default its an XML node that needs to be returned as XML
             */
	        for (int i = 0; i < nodes.size(); i++) { 
	            Node aux = nodes.get(i);
	            DOMSource source = new DOMSource(aux);
	            transformer.transform(source, result);
	        }
	        
            return baos.toString();
        } catch (TransformerException e) {
            throw new ParseException("Unable to transform the xml.",e);
        } finally { 
            checkIn(transformer);
        }
    }
}