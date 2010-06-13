package com.yahoo.dtf.actions.file;

import java.net.URI;

import com.yahoo.dtf.actions.util.CDATA;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag input
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc This tag is used to just point to inputs for other tags such 
 *               as the {@dtf.link diff} tag which uses this tag to reference 
 *               the data that the test writer wants to calculate the 
 *               differences on. See {@dtf.link diff} for more examples.
 * 
 * @dtf.tag.example 
 * <diff>
 *     <input uri="storage://OUTPUT/file1"/>
 *     <input uri="storage://OUTPUT/file2"/>
 *     <input uri="storage://OUTPUT/file3"/>
 * </diff>
 */
public class Input extends CDATA {
   
    /**
     * @dtf.attr uri
     * @dtf.attr.desc The URI of the data that will be used as an input.
     */
    private String uri = null;

    public Input() { }
    
    public void execute() throws DTFException {
        
    }

    public URI getUri() throws ParseException { return parseURI(uri); }
    public void setUri(String uri) { this.uri = uri; }

}
