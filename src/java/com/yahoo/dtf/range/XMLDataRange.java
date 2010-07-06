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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.xerces.dom.AttrImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.yahoo.dtf.exception.RangeException;
import com.yahoo.dtf.xml.JXPathHelper;
import com.yahoo.dtf.xml.XMLTransformerCache;
import com.yahoo.dtf.xml.XMLUtil;

public class XMLDataRange extends Range {
    
    private String expression = null;

    private String xpath = null;
    private long nextcalled = 0;
    private String xml = null;
    
    private List<Node> _nodes = null;
    private ArrayList<Node> _copy = null;


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
            DocumentBuilder db = XMLUtil.checkOut();
            try { 
	            Document doc = db.parse(bais);
	            JXPathContext ctx = JXPathContext.newContext(doc);    
	            String aux = args[1].substring(0,args[1].length()-1);
	           
	            if ( aux.contains(",[") ) { 
	                String[] parts = aux.split(",\\[");
	                xpath = "/" + parts[0];
	                JXPathHelper.registerNamespaces(ctx,
	                                                parts[1].replace("]", ""));
	            } else { 
	                xpath = "/" + aux;
	            }
	            
	            _nodes = ctx.selectNodes(xpath);
            } finally { 
                XMLUtil.checkIn(db);
            }
        } catch (SAXException e) {
            throw new RangeException("Unable to parse xml.",e);
        } catch (IOException e) {
            throw new RangeException("Unable to parse xml.",e);
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
            Transformer transformer = XMLTransformerCache.checkOut();
            try {
                transformer.transform(source, result);
            } catch (TransformerException e) {
                throw new RangeException("Unable to handle XML.",e);
            } finally { 
                XMLTransformerCache.checkIn(transformer);
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
