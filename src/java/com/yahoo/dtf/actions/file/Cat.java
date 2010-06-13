package com.yahoo.dtf.actions.file;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import com.yahoo.dtf.actions.util.CDATA;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag cat
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc Cat tag is used to output information to files that would 
 *               usually be logged to the test case output.
 *               
 *              
 * @dtf.tag.example
 * <cat uri="storage://OUTPUT/testoutput">
 *      This output will be visible at storage://OUTPUT/testoutput
 *      Os: ${os.name}
 *      Arch: ${os.arch}
 *      dtf.test.property1 = ${dtf.test.property1}
 *      dtf.test.property2 = ${dtf.test.property2}
 * </cat>
 *
 * @dtf.tag.example
 * <cat uri="storage://OUTPUT/testoutput" append="false">BLAH BLAH BLAH</cat>
 */
public class Cat extends CDATA {
    
    /**
     * @dtf.attr uri
     * @dtf.attr.desc The URI defining where to output the enclosed message.
     */
    private String uri = null;
   
    /**
     * @dtf.attr append
     * @dtf.attr.desc Attribute will specify if we want to append to the 
     *                destination or just replace it with the new contents.
     */
    private String append = null;
   
    public void execute() throws DTFException {
        OutputStream os = getStorageFactory().getOutputStream(getUri(),getAppend());
           
        try { 
            String data = getCDATA();
           
            if ( data == null ) 
                data = "";
            
            os.write(data.getBytes());
        } catch (IOException e) { 
            throw new DTFException("Unable to write to file [" + getUri() + "]",e);
        } finally { 
            try {
                os.close();
            } catch (IOException ignore) { }
        }
    }

    public void setUri(String uri) { this.uri = uri; }
    public URI getUri() throws ParseException { return parseURI(uri); }
    
    public void setAppend(String append) { this.append = append; } 
    public boolean getAppend() throws ParseException { return toBoolean("append", append); }
}
