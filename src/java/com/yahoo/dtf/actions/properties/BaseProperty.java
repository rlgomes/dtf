package com.yahoo.dtf.actions.properties;

import com.yahoo.dtf.actions.util.DTFProperty;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * This class is used to add properties to the base property of an agent so that
 * the execution of actions always has the latest state of the properties from 
 * the runner. The over head of this activity is minimal (about 5%) and yet it
 * guarantees that property resolution on the agent is done in a correct manner
 * and always relevant to the state of the properties immediately before having
 * called the component tag.
 */
public class BaseProperty extends DTFProperty {

    public BaseProperty() { }

    public void execute() throws DTFException {
        getConfig().setProperty(getName(), getValue());
    }
    
    public String getValue() throws ParseException {
        return value;
    }
}
