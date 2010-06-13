package com.yahoo.dtf.actions.protocol;

import com.yahoo.dtf.exception.DTFException;

/**
 * Clean up hook interface used by other plugins, or parts of DTF to clean up 
 * any resources left behind at the moment of unlocking of a component.
 * 
 * @author rlgomes
 */
public interface CleanUpHook {
    public void cleanup() throws DTFException;
}
