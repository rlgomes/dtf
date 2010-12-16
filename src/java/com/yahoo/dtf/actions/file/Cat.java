package com.yahoo.dtf.actions.file;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import com.yahoo.dtf.actions.util.CDATA;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.streaming.DTFInputStream;

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
   
    /**
     * @dtf.attr encoding
     * @dtf.attr.desc <p>
     *                Encoding to use when loading the specified property file. 
     *                DTF defaults to the Java default file encoding which comes
     *                from the property <b>file.encoding.</b> If you want to 
     *                change the default encoding set the file.encoding on the 
     *                command line when you start any of the components.
     *                </p> 
     *                <p>
     *                Every implementation of the Java platform is required to 
     *                support the following character encodings. Consult the 
     *                release documentation for your implementation to see if 
     *                any other encodings are supported.
     *                </p>
     *
     *                <ul>
     *                  <li>US-ASCII    Seven-bit ASCII, a.k.a.</li>
     *                  <li>ISO646-US, a.k.a. the Basic Latin block of the Unicode 
     *                               character set</li>
     *                  <li>ISO-8859-1  ISO Latin Alphabet No. 1, a.k.a. 
     *                               ISO-LATIN-1</li>
     *                  <li>Eight-bit Unicode Transformation Format</li>
     *                  <li>UTF-16BE Sixteen-bit Unicode Transformation Format,
     *                               big-endian byte order</li>
     *                  <li>UTF-16LE Sixteen-bit Unicode Transformation Format,
     *                               little-endian byte order</li>
     *                  <li>UTF-16  Sixteen-bit Unicode Transformation Format,
     *                               byte order specified by a mandatory initial 
     *                               byte-order mark (either order accepted on 
     *                               input, big-endian used on output)</li>
     *                </ul> 
     */
    private String encoding = null;
   
    public void execute() throws DTFException {
        OutputStream os = getStorageFactory().getOutputStream(getUri(),getAppend());
           
        try { 
            DTFInputStream dis = getCDATAStream(getEncoding());
            
            if ( dis != null ) {
                byte[] bytes = new byte[4*1024];
                int read = 0;
                while ( (read = dis.read(bytes)) != -1 ) { 
                    os.write(bytes,0,read);
                }
            }
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

    public void setEncoding(String encoding) { this.encoding = encoding; }
    public String getEncoding() throws ParseException { return replaceProperties(encoding); }
}
