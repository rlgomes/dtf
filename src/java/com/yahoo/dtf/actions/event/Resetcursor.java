package com.yahoo.dtf.actions.event;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.query.Cursor;

/**
 * @dtf.tag resetcursor
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc The reset cursor places the cursor back at the start where it 
 *               was immediately after the {@dtf.link query} tag was called. 
 *               This allows you to reuse the same cursor and not have to 
 *               re-issue the same query.
 *               
 * @dtf.tag.example 
 * <resetcursor cursor="cursor2"/>
 */
public class Resetcursor extends Action {

    /**
     * @dtf.attr cursor
     * @dtf.attr.desc Identifies the cursor that should be reset so that it 
     *                seems as if the cursor was reopened by a {@dtf.link Query}
     *                tag.
     */
    private String cursor = null;
    
    public Resetcursor() { }

    public void execute() throws DTFException {
        Cursor cursor = retCursor(getCursor());
        
        if ( cursor == null ) 
            throw new ParseException("Unable to find cursor [" + getCursor() + "]");
        
        getLogger().info("Resetting curosr [" + getCursor() + "]");
        cursor.reset();  
    }

    public String getCursor() throws ParseException { return replaceProperties(cursor); }
    public void setCursor(String cursor) { this.cursor = cursor; }
}
