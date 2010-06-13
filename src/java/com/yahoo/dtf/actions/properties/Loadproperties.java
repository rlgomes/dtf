package com.yahoo.dtf.actions.properties;

import java.net.URI;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.ActionException;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;


/**
 * @dtf.tag loadproperties
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc Loads all of the properties from a regular  
 *               <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/Properties.html#load(java.io.InputStream)}">Java Properties file</a>
 *
 * @dtf.tag.example 
 * <local>
 *     <createstorage id="INPUT" path="${dtf.xml.path}/input"/>
 *     <loadproperties uri="storage://INPUT/ut.properties"/>
 * </local>
 * 
 * @dtf.tag.example 
 * <local>
 *     <createstorage id="INPUT" path="${dtf.xml.path}/input"/>
 *     <loadproperties uri="storage://INPUT/ut.properties" encoding="UTF-16"/>
 * </local>
 */
public class Loadproperties extends Action {

    /**
     * @dtf.attr uri
     * @dtf.attr.desc Specifies the location where the properties file can be 
     *                found.  
     */
    private String uri = null;
    
    /**
     * @dtf.attr overwrite
     * @dtf.attr.desc Defaults to false, and this defines if the properties 
     *                being loaded from this external file are to overwrite 
     *                existing values of any property or not. 
     */
    private boolean overwrite = false;

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

    public Loadproperties() { }
    
    public void execute() throws DTFException {
        getConfig().loadProperties(getStorageFactory().getInputStream(getUri()),
                                   getOverwrite(),
                                   getEncoding());
    }
    
    public void setUri(String uri) { this.uri = uri; }
    public URI getUri() throws ActionException, ParseException { return parseURI(uri); }
  
    public void setOverwrite(boolean overwrite) { this.overwrite = overwrite; } 
    public boolean getOverwrite() { return overwrite; } 
    
    public void setEncoding(String encoding) { this.encoding = encoding; }
    public String getEncoding() throws ParseException { return replaceProperties(encoding); }
}
