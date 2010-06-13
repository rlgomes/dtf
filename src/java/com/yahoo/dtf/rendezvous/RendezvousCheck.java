package com.yahoo.dtf.rendezvous;

import com.yahoo.dtf.actions.rendezvous.Rendezvous_check;
import com.yahoo.dtf.comm.rpc.ActionResult;
import com.yahoo.dtf.comm.rpc.Node;
import com.yahoo.dtf.exception.DTFException;

public class RendezvousCheck extends Rendezvous_check {

    @Override
    public void execute() throws DTFException {
        RemoteResult rr = new RemoteResult();
        rr.setBool(evaluate());
        
        ActionResult ar = (ActionResult) 
                                   getGlobalContext(Node.ACTION_RESULT_CONTEXT);
        
        ar.addAction(rr);
    }

}
