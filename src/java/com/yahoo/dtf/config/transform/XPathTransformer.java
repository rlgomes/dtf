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

/**
 * @dtf.feature XPath Transformer 
 * @dtf.feature.group Transformers
 * @dtf.feature.desc 
 * 
 * <p>
 * We've already seen that properties can be identified by using the following 
 * syntax ${propertyname}. Now this is simple and straightforward but sometimes 
 * the contents the property may contain more than just plain text and in those 
 * cases we may want to only reference certain elements within the structured 
 * data. For this we currently have the ability to apply transformations on the 
 * property's data using various  data query languages. Currently we support 
 * XPath on the any XML data within a property and here's a simple example of 
 * how this works:
 * </p>
 * 
 * <p> 
 * So lets say we have the property myxml that contains the following xml 
 * snipplet:
 * </p>
 * 
 * <pre>
 *  <list>
 *      <item>1</item>
 *      <item>2</item>
 *      <item>3</item>
 *      <item>4</item>
 *      <item>5</item>
 *      <item>6</item>
 *  </list>
 * </pre>
 * 
 * <p>
 * Now lets say we wanted to reference the myxml property but we only want the 
 * forth value of the child item in the XML data. This is how we would achieve 
 * that:
 * </p>
 * 
 * <pre>
 * <log>${myxml:xpath://list/item[3]}</log>
 * </pre>
 * 
 * <p> 
 * The Xpath language is a standard and all of the information on this standard 
 * can be easily found by searching for XPath examples online. 
 * </p>
 */
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