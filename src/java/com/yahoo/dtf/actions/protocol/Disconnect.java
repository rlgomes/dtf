package com.yahoo.dtf.actions.protocol;

import com.yahoo.dtf.actions.protocol.Connect;
import com.yahoo.dtf.NodeState;
import com.yahoo.dtf.exception.DTFException;

/**
 * This action is used internally for the protocol connect action for 
 * establishing a connection to the DTFC
 * 
 * @author Rodney Gomes
 */
public class Disconnect extends Connect {
    public Disconnect()  { }

    public void execute() throws DTFException {
        NodeState ni = NodeState.getInstance(); 
        ni.removeNode(this); 
    }
}
