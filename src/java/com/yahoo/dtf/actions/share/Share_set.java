package com.yahoo.dtf.actions.share;

import java.util.HashMap;

import com.yahoo.dtf.actions.flowcontrol.Sequence;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ShareException;
import com.yahoo.dtf.share.Share;

/**
 * @dtf.tag share_set
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc This tag will set some information on the share point identified
 *               by the attribute id. This tag is not blocking and will return
 *               as soon as the actions have been set. 
 *               
 * @dtf.tag.example 
 * <share_set id="SHARE1">
 *     <property name="X" value="Y" overwrite="true"/>
 *     <createrange name="R" value="1..10" recycle="true"/>
 * </share_set>
 *
 * @dtf.tag.example 
 * <share_set id="SHARE1">
 *      <share_set id="ANOTHERSHARE">
 *          <property name="someone.got.our.data" value="true" overwrite="true"/>
 *      </share_set> 
 *      <property name="somedata" value="X"/>
 * </share_set>
 */
public class Share_set extends ShareOperation {

    @Override
    public void execute() throws DTFException {
        HashMap<String, Share> shares = getShares();
        Share sp = shares.get(getId());
        
        if ( sp == null ) {
            throw new ShareException("Share with name [" + getId() + 
                                     "] does not exist.");
        }
        
        Sequence sequence = new Sequence();
        sequence.addActions(children());
        sp.set(sequence);
    }
}
