package com.yahoo.dtf.components;

import com.yahoo.dtf.exception.DTFException;

/**
 * ComponentUnlockHook is called on the runner side when a component is being 
 * unlocked. This is very useful if you have resources allocated with your own
 * ComponentLockHook and want to free that up at this point so that your long 
 * running tests don't accumulate unused resources.
 * 
 * @author rlgomes
 */
public interface ComponentUnlockHook {

    /**
     * 
     * @param id
     * @return
     */
    public void unlockComponent(String id) throws DTFException;
}
