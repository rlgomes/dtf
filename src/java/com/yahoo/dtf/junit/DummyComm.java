package com.yahoo.dtf.junit;

import org.junit.Ignore;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.flowcontrol.Sequence;
import com.yahoo.dtf.comm.Comm;
import com.yahoo.dtf.config.Config;
import com.yahoo.dtf.exception.DTFException;

@Ignore
public class DummyComm extends Comm {
    public DummyComm(Config config) throws DTFException {
        super(config);
    } 
    
    @Override
    public Action sendAction(String id, Action action) throws DTFException {
        action.execute();
        return new Sequence();
    }
    
    @Override
    public Action sendActionToCaller(String id, Action action)
            throws DTFException {
        action.execute();
        return new Sequence();
    }
    
}