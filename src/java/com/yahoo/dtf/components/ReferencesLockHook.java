package com.yahoo.dtf.components;

import java.util.ArrayList;
import java.util.Enumeration;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.function.ExportRefs;
import com.yahoo.dtf.actions.reference.Referencable;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.logger.DTFLogger;

public class ReferencesLockHook implements LockHook {
    
    public ArrayList<Action> init(String id) throws DTFException {
        DTFLogger log = Action.getLogger();
        ArrayList<Action> result = new ArrayList<Action>();
        /*
         * Export references!
         */
        Enumeration<Referencable> references = 
                            Action.getState().getReferences().getAll();
        if ( !references.hasMoreElements() ) { 
            if ( log.isDebugEnabled() )
                log.debug("No exported functions.");
        } else {
            ExportRefs export = new ExportRefs();
           
            while ( references.hasMoreElements() ) { 
                Referencable reference = references.nextElement();
                export.addAction(reference);
                
                if ( log.isDebugEnabled() )
                    log.debug("Sending reference [" + 
                              reference.getId() + "]");
            } 
           
            result.add(export);
        }
        return result;
    }
}
