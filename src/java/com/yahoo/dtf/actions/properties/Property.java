package com.yahoo.dtf.actions.properties;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import com.yahoo.dtf.actions.util.DTFProperty;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag property
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc defines a new property within the current script. The value 
 *               of the property can be defined with the value attribute or can
 *               be loaded from the full contents of a file specified by the uri
 *               attribute.
 *
 * @dtf.tag.example 
 * <property name="testvar1" value="values"/> 
 *
 * @dtf.tag.example 
 * <property name="testvar1" uri="storage://INPUT/myobject"/> 
 * 
 * @dtf.tag.example 
 * <property name="testvar1" uri="storage://INPUT/myobject" encoding="UTF-8"/> 
 */
public class Property extends DTFProperty {

    /**
     * @dtf.attr overwrite
     * @dtf.attr.desc defaults to false, and this defines if the property being
     *                defined should  overwrite existing values of any property 
     *                or not. 
     */
    private String overwrite = "false";
   
    /**
     * @dtf.attr uri
     * @dtf.attr.desc the uri attribute can be used to load the contents of a 
     *                specified file into the value of a property.
     */
    private String uri = null;
    

    /**
     * @dtf.attr encoding
     * @dtf.attr.desc See {@dtf.link Loadproperties} for more information on the
     *                encoding attribute.
     */
    private String encoding = null;
    
    public Property() { }

    public Property(String name, String value, boolean overwrite) throws ParseException {
        setName(name);
        setValue(value);
        setOverwrite("" + overwrite);
    }

    public void execute() throws DTFException {
        URI uri = getUri();
        String value = getValue();
        String cdata = getCDATA();
        
        if ( (value != null && uri != null) || 
             (value != null && cdata != null) || 
             (uri != null && cdata != null) ) {
            throw new ParseException("You can only set the value, uri or have " 
                                     + "a single text child node per property " 
                                     + "tag.");
        }
        
        if (uri != null) { 
            InputStream is = getStorageFactory().getInputStream(uri);
          
            /*
             * Copy back the contents into the value string and later make this
             * the value of the specific property name.
             */
            try {
                value = "";
                int read = 0;
                byte[] buffer = new byte[32*1024];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while (( read = is.read(buffer)) != -1) {
                    baos.write(buffer,0,read);
                }
                value = baos.toString(getEncoding());
            } catch (IOException e) {
                throw new DTFException("Error reading file contents [" + uri + 
                                       "]",e);
            }
        } 
        
        if ( cdata != null ) 
            value = cdata;
        
        if ( value == null ) 
            value = "";
        
        getConfig().setProperty(getName(), value, getOverwrite());
    }

    public boolean getOverwrite() throws ParseException { return toBoolean("overwrite",overwrite); }
    public void setOverwrite(String overwrite) throws ParseException { this.overwrite = overwrite; }

    public void setUri(String uri) { this.uri = uri; }
    public URI getUri() throws ParseException { return parseURI(uri); }

    public void setEncoding(String encoding) { this.encoding = encoding; }
    public String getEncoding() throws ParseException { return replaceProperties(encoding); }
}
