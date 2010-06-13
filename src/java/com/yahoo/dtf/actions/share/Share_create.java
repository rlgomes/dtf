package com.yahoo.dtf.actions.share;

import java.util.HashMap;

import com.yahoo.dtf.DTFConstants;
import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.exception.ShareException;
import com.yahoo.dtf.share.Share;
import com.yahoo.dtf.share.ShareComponentHook;
import com.yahoo.dtf.share.ShareCreateAll;
import com.yahoo.dtf.share.ShareFactory;

/**
 * @dtf.tag share_create
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc A share point is a special way of communicating between threads
 *               in DTF. This resource works across components and can be used
 *               to share information between any thread running on any component
 *               in DTF. When creating a share remember that it is local to the
 *               component where you created it but its accessed over the 
 *               network for all the other components. 
 *               <br/>
 *               <br/>
 *               Also remember that you can develop your own share point types
 *               if necessary and consult the developers guide for more 
 *               information.
 *               
 * @dtf.tag.example 
 * <share_create id="SHARE1" type="single"/>
 *
 * @dtf.tag.example 
 * <share_create id="SHARE1" type="queue"/>
 */
public class Share_create extends ShareOperation {

    /**
     * @dtf.attr type
     * @dtf.attr.desc The share point type is important becuase it defines how
     *                all your share_sets and share_gets will behave. The default
     *                is 'single', look at the following table for more 
     *                information on the various share types:
     *                
     *           <b>Share Types</b>
     *           <table border="1">
     *               <tr>
     *                   <th>Type</th> 
     *                   <th>Description</th> 
     *               </tr>
     *               <tr>
     *                   <td>single</td> 
     *                   <td>
     *                   The single share point type basically will hold a 
     *                   single instance of the last set done to this share.
     *                   Any previous share_set is lost and can never be 
     *                   retrieved. This type is usually used when you just want
     *                   to have share that contains the latest and greatest 
     *                   version of some information.
     *                   </td> 
     *               </tr>
     *               <tr>
     *                   <td>queue</td> 
     *                   <td>
     *                   The queue share point type basically will queue up all 
     *                   of the share_sets done and will return them in the same 
     *                   order back to the each of the share_gets that are done. 
     *                   If N threads are doing share_get on the same queue 
     *                   share point they will each get a different set from the 
     *                   queue and never will there be 2 threads have the same 
     *                   thing returned.
     *                   </td>
     *               </tr>
     *               <tr>
     *                   <td>cumulative</td> 
     *                   <td>
     *                   The cumulative share point is special because just like 
     *                   the queue it will keep all the sets that were done in a 
     *                   queue, but when a share_get comes through this share 
     *                   point will return all of its current information back 
     *                   to that one share_get.  So it acts like a buffer where 
     *                   you can write a bunch of different information and once 
     *                   one single share_get comes through it will be able to 
     *                   pick up all the information in a single request. You 
     *                   must be careful to not overload this type of share 
     *                   point with thousands of sets because you will break 
     *                   the agents with an out of memory exception.
     *                   </td>
     *               </tr>
     *          </table>
     */
    private String type = null;
    
    @Override
    public void execute() throws DTFException {
        HashMap<String, Share> shares = getShares();
        Share sp = shares.get(getId());
        
        if ( sp != null ) {
            throw new ShareException("Share with name [" + getId() + 
                                     "] already exists.");
        }
        
        sp = ShareFactory.getShare(getType(), getId());

        synchronized(shares) { 
            shares.put(getId(),sp);
            shares.notifyAll();
        }

        if ( !DTFNode.getType().equals(DTFConstants.DTFX_ID) ) {
            /*
             * Make sure notification/creation of this hare occurs on all
             * other components.
             */
            if ( getLogger().isDebugEnabled() )
                getLogger().debug("Sending share [" + getId() + "] to runner.");
            
            String owner = DTFNode.getOwner().getOwner();
            ShareCreateAll sca = new ShareCreateAll();
            sca.setId(getId());
            sca.setType(getType());
            sca.setCid(Action.getLocalID());
            getComm().sendActionToCaller(owner, sca).execute(); 
        } else { 
            ShareComponentHook.createOnAll(getId(), getType(), Action.getLocalID());
        }
    }
    
    public void setType(String type) { this.type = type; }
    public String getType() throws ParseException { return replaceProperties(type); }
}
