package com.yahoo.dtf.actions.http;

import com.yahoo.dtf.actions.util.CDATA;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.streaming.DTFInputStream;

/**
 * @dtf.tag entity
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc this tag defines an entity to be passed in the body of an http
 *               request. The data will be placed in the body of the message 'as
 *               is' and there is currently no support for multi-part messages.
 *               (Can be added later)
 * 
 * @dtf.tag.example 
 * <http_get uri="${dtf.http.uri}" perfrun="true" onFailure="fail">
 *      <entity value="XXXXX"/>
 * </http_get>
 * 
 */
public class Entity extends CDATA {
 
    /**
     * @dtf.attr.name value
     * @dtf.attr.desc the value of the entity to attach to the current http 
     *                request.
     */
    private String value = null;
   
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
        // nothing to do.
    }
    
    public String getValue() throws ParseException { 
        return replaceProperties(value);
    }
    
    public void setValue(String value) { this.value = value; }

    public DTFInputStream getEntityStream() throws ParseException { 
        return getEntityStream(getEncoding());
    }
    
    public DTFInputStream getEntityStream(String encoding)
           throws ParseException { 
        if ( value == null ) { 
            return getCDATAStream();
        } else { 
            return replacePropertiesAsInputStream(value,encoding);
        }
    }

    public void setEncoding(String encoding) { this.encoding = encoding; }
    public String getEncoding() throws ParseException { return replaceProperties(encoding); }
}
