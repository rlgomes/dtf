package com.yahoo.dtf.actions.component;

import com.yahoo.dtf.actions.component.Attrib;
import com.yahoo.dtf.actions.util.DTFProperty;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.util.StringUtil;

/**
 * @dtf.tag attrib
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc This tag is used for defining attributes that a lockcomponent 
 *               should search for when trying to match the right type of 
 *               component. The attrib name and value are matched with 
 *               properties set on the DTFA command line. The properties which
 *               are loaded by the dtf.defaults property are also used in this
 *               context but only if they start with a non dtf.xxx prefix. For 
 *               more information on this subject have a look at the DTF User's 
 *               Guide which explains the usage of default properties on agents
 *               and locking those agents based on those same properties.
 * 
 * @dtf.tag.example 
 * <local>
 *     <echo>Remote counter retrieval</echo>
 *     <lockcomponent id="DTFA1">
 *          <attrib name="type" value="DTFA"/>
 *     </lockcomponent>
 * </local>
 */
public class Attrib extends DTFProperty {

    private String testprop = "false";
    
    public Attrib() { } 
    
    public Attrib(String name, String value, boolean isTestProperty) { 
        setName(name);
        setValue(value);
        setTestProp(""+isTestProperty);
    }
    
    public void execute() throws DTFException { }
    
    public boolean matches(Attrib attrib) throws ParseException { 
        return (StringUtil.equalsIgnoreCase(getName(),attrib.getName()) &&
                StringUtil.equalsIgnoreCase(getValue(),attrib.getValue()));
    }
    
    public boolean equals(Object obj) {
        if (obj instanceof Attrib) { 
            Attrib attrib = (Attrib) obj;
            try { 
                return this.matches(attrib);
            } catch (ParseException e) {}
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        assert false: "No hash code assigned";
        return 42;
    } 
    
    public void setTestProp(String testprop) { this.testprop = testprop; } 
    public boolean getTestProp() throws ParseException { 
        return toBoolean("testprop", testprop);
    } 
    public boolean isTestProp() throws ParseException { return getTestProp(); } 
}