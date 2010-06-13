package com.yahoo.dtf.config.transform;

import java.util.HashMap;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

public final class TransformerFactory {

    private static HashMap<String, Transformer> _transformers = 
                                             new HashMap<String, Transformer>();
    
    public static Transformer getTransformer(String expression) throws ParseException { 
        String label = expression.split(":")[0];
       
        Transformer transformer = _transformers.get(label);
        if (!_transformers.containsKey(label)) {
            throw new ParseException("No transform language found for id [" + 
                                     label + "].");
        }
        
        return transformer;
    }
    
    /**
     * This method is used for registering Transformers for application on 
     * properties that contain special data that needs to be handled slightly 
     * differently when required.
     *  
     * @param signature the signature identifies uniquely the transformer that
     *        is being registered. If there is a colliding transformer than an
     *        exception is thrown. 
     *        <br/> 
     *        <br/> 
     *        example: ${myproperty:xpath://root/node_of_interest} 
     *        <br/> 
     *        <br/> 
     *        The signature in the above transformer is the xpath label the rest
     *        of the expression is intended for the transformer to parse.
     *        
     * @param transformer
     * @throws DTFException 
     */
    public static void registerTransformer(String signature,
                                           Transformer transformer)
                  throws DTFException { 
        if (_transformers.containsKey(signature)) { 
            throw new DTFException("Transformer with signature [" + 
                                   signature + "] already exists.");
        }
        _transformers.put(signature,transformer);
    }
}
