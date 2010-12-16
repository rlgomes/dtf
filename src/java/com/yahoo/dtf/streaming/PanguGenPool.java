package com.yahoo.dtf.streaming;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.pangu.PanguException;
import org.pangu.PanguGen;
import org.pangu.XSDCompiler;

import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.NodeShutdownHook;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.exception.StorageException;
import com.yahoo.dtf.storage.StorageFactory;

public class PanguGenPool {

    private static HashMap<Integer, PanguGenerateThread> gens = 
                                    new HashMap<Integer, PanguGenerateThread>();
    
    static { 
        DTFNode.registerShutdownHook(new NodeShutdownHook() {
            @Override
            public void shutdown() {
                for (PanguGenerateThread pgt : gens.values()) { 
                    pgt.cancel();
                }
            }
        });
    }
    
    public static PanguGenerateThread checkout(String xsd)
           throws ParseException { 
        int code = xsd.hashCode();
        PanguGenerateThread pgt = null;
   
        try { 
	        synchronized (gens) { 
	            pgt = gens.remove(code);
	            
	            if ( pgt == null ) { 
	                InputStream is = null;
	                
	                if ( xsd.startsWith("storage://") ) {
	                    StorageFactory sf = Action.getStorageFactory();
	                    is = sf.getInputStream(new URI(xsd));
	                } else { 
	                    is = new ByteArrayInputStream(xsd.getBytes());
	                }
	            
	                PanguGen gen = XSDCompiler.compile(is);
	                pgt = new PanguGenerateThread(gen);
	                pgt.start();
	                
	                gens.put(code,pgt);
	            }
	        }     
	    } catch (StorageException e) {
	        throw new ParseException("Error parsing XSD.",e);
	    } catch (URISyntaxException e) {
	        throw new ParseException("Error parsing XSD.",e);
	    } catch (PanguException e) {
	        throw new ParseException("Error parsing XSD.",e);
	    }
        
        return pgt;
    }
    
    public static void checkin(String xsd, PanguGenerateThread pgt) {
        gens.put(xsd.hashCode(), pgt);
    }
}
