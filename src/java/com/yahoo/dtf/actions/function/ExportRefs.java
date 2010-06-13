package com.yahoo.dtf.actions.function;

import java.util.ArrayList;

import com.yahoo.dtf.actions.reference.Referencable;
import com.yahoo.dtf.actions.util.DTFProperty;
import com.yahoo.dtf.comm.rpc.Node;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.state.ActionState;
import com.yahoo.dtf.state.DTFState;

/**
 * This class is used to export references on the component side and should only
 * be used by internal classes such as the export class which is accessible in 
 * the DTF XML.
 */
public class ExportRefs extends DTFProperty {

    public ExportRefs() { }

    public void execute() throws DTFException {
        DTFState state = ActionState.getInstance().getState(Node.BASE_CONFIG);
        ArrayList<Referencable> references = findActions(Referencable.class);
        
        for (int i = 0; i < references.size(); i++) { 
            Referencable reference = references.get(i);
            state.getReferences().addReference(reference.getId(), reference);
            if ( getLogger().isDebugEnabled() ) {
                getLogger().debug("Accepting reference [" + reference.getId() + 
                                  "]");
            }
        }
    }
    
    public String getValue() throws ParseException {
        return value;
    }
}
