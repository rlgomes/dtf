package com.yahoo.dtf.share;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.share.ShareOperation;
import com.yahoo.dtf.comm.rpc.ActionResult;
import com.yahoo.dtf.comm.rpc.Node;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

public class SharePointGet extends Action {

    private String id = null;
    
    private String blocking = null;
    
    @Override
    public void execute() throws DTFException {
        Share sp = ShareOperation.getShares().get(getId());
        
        if ( sp == null ) {
            throw new DTFException("SyncPoint not found with the name [" + 
                                   getId() + "]");
        }
       
        Action result = sp.get(getBlocking());
        ActionResult ar = (ActionResult) getContext(Node.ACTION_RESULT_CONTEXT);
        ar.addAction(result);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public boolean getBlocking() throws ParseException { return toBoolean("blocking",blocking); }
    public void setBlocking(String blocking) { this.blocking = blocking; }
}
