package com.yahoo.dtf.actions.reference;

import java.util.ArrayList;

import com.yahoo.dtf.actions.reference.Referencable;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;


public class RefWrapper extends Action {

    public RefWrapper() { 
        
    }
    
    public RefWrapper(Referencable ref) { 
        addAction(ref);
    }
    
    public void execute() throws DTFException {
        Referencable referencable = (Referencable)findFirstAction(Referencable.class);
        if (referencable.isReference()) { 
            referencable.lookupReference().execute();
        } else if (referencable.isReferencable()) {
            throw new DTFException("Referencable elements are not to be executed.");
        } else
            referencable.execute();
    }
   
    /*
     * TODO: Adding children to refid can be caught here!
     */
    public void addAction(Action action) {
        Referencable referencable = (Referencable)findFirstAction(Referencable.class);
        if ( referencable == null ) { 
            super.addAction(action);
        } else { 
            referencable.addAction(action);
        }
    }
    
    public void addActions(ArrayList actions) {
        Referencable referencable = (Referencable)findFirstAction(Referencable.class);
        if ( referencable == null ) { 
            super.addActions(actions);
        } else { 
            referencable.addActions(actions);
        }
    }
    
    public Referencable lookupReference() throws ParseException { 
        Referencable referencable = (Referencable)findFirstAction(Referencable.class);
        if (referencable.isReference()) { 
            Referencable ref = (Referencable) referencable.lookupReference();
            if (ref == null)
                throw new ParseException("Unable to find reference for " + referencable.getRefid());
            return ref;
        } else 
            return referencable;
    }

    public boolean anInstanceOf(Class type) {
        Referencable referencable = (Referencable)findFirstAction(Referencable.class);
        return referencable.anInstanceOf(type);
    }
}