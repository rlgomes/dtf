package com.yahoo.dtf.actions.plugin;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * This is a simple Java only DTF tag showing how the attribute names match up 
 * with the XSD definition in the plugin.xsd file and how the methods for 
 * getting/setting should be written.
 * 
 * ATTENTION: the name of the class has to match the name of the ELEMENT defined
 *            in the XSD and have only the first letter of that name in upper 
 *            case.
 */
public class Plugin_action extends Action {

	private String attribute1 = null;
	
	public void execute() throws DTFException {
		
	}
	
	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}
	
	public String getAttribute1() throws ParseException {
		return replaceProperties(attribute1);
	}

}
