package com.yahoo.dtf.actions.event;

import com.yahoo.dtf.actions.util.ActionContexts;
import com.yahoo.dtf.actions.util.DTFProperty;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag attribute
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc Attribute tag identifies attributes that are recorded by the 
 *               event tag. Attributes have at least a name and value and can 
 *               also carry information about the type of the attribute and its
 *               length.
 * 
 * @dtf.tag.example 
 * <event name="logevent">
 *     <attribute name="iteration" value="${iter}"/>
 *     <log>xxxx</log>
 * </event>
 *
 */
public class Attribute extends DTFProperty {

    /**
     * @dtf.attr index
     * @dtf.attr.desc Identifies if this attribute is indexable or not.
     */
    private String index = null;
    
    /**
     * @dtf.attr type
     * @dtf.attr.desc <p>Identifies the type of attribute being recorded.</p>
     *                <b>Supported DTF types:</br>
     *                <ul>
     *                <table border="1">
     *                    <tr>
     *                        <th>Type</th> 
     *                        <th>Description</th> 
     *                    </tr>
     *                    <tr>
     *                         <td>int</td>
     *                         <td>Integer type</td>
     *                    </tr>
     *                    <tr>
     *                         <td>long</td>
     *                         <td>Long type</td>
     *                    </tr>
     *                    <tr>
     *                         <td>string</td>
     *                         <td>String type</td>
     *                    </tr>
     *               </table> 
     *               </ul>
     */
    private String type = null;

    public Attribute() { }
    
    public void execute() throws DTFException {
        com.yahoo.dtf.recorder.Event event = (com.yahoo.dtf.recorder.Event)
                                       getContext(ActionContexts.EVENT_CONTEXT);
        if (event != null) {
            event.addAttribute(getName(), getValue(), getLength(), isIndex());
        } else {
            getRecorder().addDefaultAttribute(getName(), getValue(), getLength());
        }
    }

    public boolean isIndex() throws ParseException { return toBoolean("index",index); }
    public String getIndex() throws ParseException { return replaceProperties(index); }
    public void setIndex(String index) { this.index = index; }
    public void setIndex(boolean index) { this.index = ""+index; }

    public String getType() throws ParseException { return replaceProperties(type); }
    public void setType(String type) { this.type = type; }
    
    public boolean equals(Object obj) {
        if ( obj instanceof Attribute ) { 
            Attribute other = (Attribute) obj;
            try {
                return other.getName().equals(this.getName());
            } catch (ParseException e) { 
                getLogger().error("Issue tryign to compare Attribute.",e);
            }
        }
        
        return false;
    }
    
    @Override
    public int hashCode() {
        assert false: "No hash code assigned";
        return 42;
    } 
    
}