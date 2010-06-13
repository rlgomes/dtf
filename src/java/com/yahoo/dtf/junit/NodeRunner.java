package com.yahoo.dtf.junit;

import org.junit.Ignore;

import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.DTFProperties;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.util.ThreadUtil;

@Ignore
public class NodeRunner extends Thread {
    public DTFNode _node;

    public NodeRunner(DTFNode _node) {
        this._node = _node;
    }
    
    public static NodeRunner startupNode(String type,
                                         String name) throws DTFException { 
        System.setProperty(DTFProperties.DTF_NODE_TYPE, type);
       
        System.setProperty("java.security.policy","src/config/policy.txt");

        NodeRunner runner = new NodeRunner(new DTFNode());
        runner.start();
        
        ThreadUtil.pause(3000); 
        return runner;
    }
}