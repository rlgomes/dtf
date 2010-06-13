package com.yahoo.dtf.actions.function;

import com.yahoo.dtf.actions.util.DTFProperty;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag param
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc Define a parameter for a function.
 *
 * @dtf.tag.example 
 * <function name="func3">
 *      <param name="nomore" type="optional"/>  
 *      <local><echo>In func3</echo></local>
 * </function>
 * 
 * @dtf.tag.example 
 * <function name="func2">
 *      <param name="nomore" type="required"/>  
 *      <param name="iterations" default="1000"/>
 *      
 * </function>
 * 
 */
public class Param extends DTFProperty {
  
    public final static String REQUIRED_PARAM = "required";
    public final static String OPTIONAL_PARAM = "optional";
   
    /**
     * @dtf.attr type
     * @dtf.attr.desc There are only two types of parameters: required, 
     *                optional.
     */
    private String type = null;
    
    /**
     * @dtf.attr default
     * @dtf.attr.desc default value for this optional paramter.
     * 
     */
    private String Default = null;
    
    public void execute() throws DTFException { 
        if (getType().equals(OPTIONAL_PARAM)) { 
            if (getConfig().getProperty(getName()) == null && 
                getDefault() != null) { 
                getConfig().setProperty(getName(), getDefault());
            }
        }
    }
    
    public String getDefault() { return Default; }
    public void setDefault(String Default) { this.Default = Default; }

    public String getType() throws ParseException { return replaceProperties(type); }
    public void setType(String type) { this.type = type; }
    
    public boolean isRequired() { return type.equalsIgnoreCase(REQUIRED_PARAM); }
    public boolean isOptional() { return type.equalsIgnoreCase(OPTIONAL_PARAM); }
}
