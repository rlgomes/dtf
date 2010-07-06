package com.yahoo.dtf.xml;

import java.util.ArrayList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.TransformerFactory;

/**
 * Simple Transformer cache used by various XML features within DTF to re-use 
 * Transformer objects across multiple executions.
 * 
 * @author rlgomes
 */
public class XMLTransformerCache {

    private static TransformerFactory tf = TransformerFactory.newInstance();
   
    private static ArrayList<Transformer> _transformers = 
                                                   new ArrayList<Transformer>();

    /**
     * Return a previously checked out Transformer to be re-used in a subsequent
     * required situation.
     * 
     * @param transformer
     */
    public static void checkIn(Transformer transformer) { 
        synchronized(_transformers) { 
            _transformers.add(transformer);
        }
    }
   
    /**
     * Get a Transformer object to be used and returned to this cache with the
     * checkIn method.
     * 
     * @return
     */
    public static Transformer checkOut() { 
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
}
