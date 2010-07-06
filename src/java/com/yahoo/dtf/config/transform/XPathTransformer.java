package com.yahoo.dtf.config.transform;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.xerces.dom.DeferredAttrImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.xml.JXPathHelper;
import com.yahoo.dtf.xml.XMLTransformerCache;
import com.yahoo.dtf.xml.XMLUtil;

public class XPathTransformer implements Transformer {
    
    public String apply(String data, String expression) throws ParseException {
        Document document = XMLUtil.parseXML(data);
        JXPathContext ctx = JXPathContext.newContext(document);
        javax.xml.transform.Transformer transformer = 
                                                 XMLTransformerCache.checkOut();

        if ( expression.contains(",[") ) { 
            String[] parts = expression.split(",\\[");
            expression = parts[0];
            JXPathHelper.registerNamespaces(ctx,
	                                        parts[1].replace("]", ""));
        }
       
        List<Node> nodes = ctx.selectNodes(expression);
        
	    try { 
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(baos);
           
            if ( nodes.size() == 1 ) {
                Object obj = nodes.get(0);
                if ( obj instanceof DeferredAttrImpl ) { 
	                return ((DeferredAttrImpl)obj).getValue();
                } else if ( !(obj instanceof Node) ) { 
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
            XMLTransformerCache.checkIn(transformer);
        }
    }
}