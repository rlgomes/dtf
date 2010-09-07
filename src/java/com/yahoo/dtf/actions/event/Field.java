package com.yahoo.dtf.actions.event;

import com.yahoo.dtf.actions.event.Attribute;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag field
 * @dtf.skip.index
 *  
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc The field tag is just like the attribute tag but is used in the
 *               context of queries to identify the fields that are suppose to 
 *               selected by the select clause in the query.
 * 
 */
public class Field extends Attribute {
    public Field() { }
    public void execute() throws DTFException { }
}
