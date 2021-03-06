package com.yahoo.dtf.streaming;

import org.pangu.tree.decorators.XMLDecorator;

import com.yahoo.dtf.exception.ParseException;

/** 
 * @dtf.feature XML Stream Type
 * @dtf.feature.group DTF Properties
 * 
 * @dtf.feature.desc
 * <p>
 * The xml stream type generates a XML document from the XSD location you 
 * passed as an argument as well as the size you wanted as a target size. The 
 * one thing to remember is that since we have to respect the grammar being 
 * used we may have to use more or less bytes in order to conform to the grammar
 * and be off of your desired size by a small percentage. 
 * </p>
 * <p>
 * Using the xml streaming property is as simple as referencing the property
 * like so:
 * </p>
 * <pre>
 * ${dtf.stream(xml,storage://INPUT/list.xsd,128)}
 * </pre>
 * <p>
 * The previous property will generate a XML document that respects the 
 * list.xsd referenced and will attempt to make it exactly 128 bytes in length.
 * </p>
 */
public class XMLInputStream extends PanguInputStream {
    public XMLInputStream(long size, String[] args) throws ParseException { 
        super(size,args,new XMLDecorator());
    }
}
