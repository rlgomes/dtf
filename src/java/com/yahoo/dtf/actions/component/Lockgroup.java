package com.yahoo.dtf.actions.component;

import java.util.ArrayList;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.protocol.Lock;
import com.yahoo.dtf.actions.protocol.LockGrp;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.util.TimeUtil;

/**
 * @dtf.tag lockgroup
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               This tag offers the ability to specific a group for locking so
 *               that DTF can be smart and allow for the whole group to be 
 *               locked or release the partial group so that the resources
 *               can be used by others that would be locking components at the
 *               same time. This avoids deadlocks because two tests being
 *               executed in parallel would like to lock the same 3 resources
 *               and end up failing because they locked 1-2 components each and
 *               locked the other test out.
 *               </p>
 *               <h2>When to use a lockgroup?</h2>
 *               <p>
 *               A lockgroup should be used whenever you are locking more than 1
 *               component and are going to be running this test concurrently 
 *               with other tests. The lockgroup gives DTF the ability to 
 *               correctly free up locks when they're not completely fulfilled
 *               for a testscript to execute and give those resources to another
 *               test that can execute while this testscript awaits its turn 
 *               to use the available resources.  
 *               </p>
 *               
 * 
 * @dtf.tag.example 
 * <local>
 *     <lockgroup>
 *          <lockcomponent id="DTFA1"/>
 *          <lockcomponent id="DTFA2"/>
 *          <lockcomponent id="DTFA3"/>
 *     </lockgroup>
 * </local>
 */
public class Lockgroup extends Action {

    /**
     * @dtf.attr timeout
     * @dtf.attr.desc The timeout in seconds to wait for the component to 
     *                become available to the DTFC. This timeout is useful when
     *                automating testing and wanting to start up the framework 
     *                along with the test driver (DTFX) and not knowing when 
     *                the necessary components are available. Using the timeout
     *                you can estimate the component will be come available with
     *                in X amount of time. 
     */
    private String timeout = null;
   
    public Lockgroup() { }
  
    public void execute() throws DTFException { 
        // Whenever we haven't been named lets make sure to register first.
        getComm().checkAndConnectToDTFC();
        
        ArrayList<Lockcomponent> lockcomponents = findActions(Lockcomponent.class);
        ArrayList<Lock> locks = new ArrayList<Lock>();
        
        for (Lockcomponent lockcomponent : lockcomponents)
            locks.add(lockcomponent.assembleLock());

        LockGrp lockgroup = new LockGrp();
        lockgroup.setXMLLocation(this);
        lockgroup.addActions(locks);
       
        Action result = getComm().sendActionToCaller("dtfc", lockgroup);
        
        if (result != null) {
	        ArrayList<Lock> returnedLocks = result.findAllActions(Lock.class);
	        
	        if ( returnedLocks.size() == 0 )
	            result.execute();
	      
	        // handle the returned locks correctly
	        for (int i = 0 ; i < lockcomponents.size(); i++) { 
	            lockcomponents.get(i).handleLockResponse(returnedLocks.get(i));
	        }
        } else { 
            throw new DTFException("Unable to register component :(.");
        }
    }
   
    public long getTimeout() throws ParseException { 
        return TimeUtil.parseTime("timeout",replaceProperties(timeout)); 
    }
    public void setTimeout(String timeout) { this.timeout = timeout; }
}
