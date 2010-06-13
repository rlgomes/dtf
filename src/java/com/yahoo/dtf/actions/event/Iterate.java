package com.yahoo.dtf.actions.event;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.NoMoreResultsException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag iterate
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc this tag can be used to do just what its name says, "iterate" 
 *               over the values that a specified cursor may have. It will 
 *               continue to iterate until there are no more values and then 
 *               will no longer execute the underlying action.
 *               
 * @dtf.tag.example 
 * <sequence>
 *     <query uri="storage://OUTPUT/myevents.txt" cursor="cursor2"/>
 *     
 *     <iterate cursor="cursor2">
 *         <if> 
 *             <eq op1="${cursor2.field2}" op2="valueX"/>
 *             <then>
 *                 <log>${cursor2.field1}</log>
 *             </then>
 *         </if>    
 *     </iterate>
 * </sequence>
 */
public class Iterate extends Action {

    /**
     * @dtf.attr cursor
     * @dtf.attr.desc Identifies the cursor name that will be used to fetch the
     *                next result.
     */
    private String cursor = null;
    
    public Iterate() { }

    public void execute() throws DTFException {
        Nextresult nextresult = new Nextresult();
        nextresult.setCursor(getCursor());
        nextresult.setRecycle("false");
       
        try { 
            while (true) {
                nextresult.execute();
                // execute children
                executeChildren();
            }
        } catch (NoMoreResultsException e) { 
            if ( getLogger().isDebugEnabled() ) 
                getLogger().debug("Done with elements of [" + getCursor() + "]");
        }
    }

    public String getCursor() throws ParseException { return replaceProperties(cursor); }
    public void setCursor(String cursor) { this.cursor = cursor; }
}
