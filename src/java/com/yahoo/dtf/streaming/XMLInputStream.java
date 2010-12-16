package com.yahoo.dtf.streaming;

import org.pangu.tree.decorators.XMLDecorator;

import com.yahoo.dtf.exception.ParseException;

/** 
 * @dtf.feature XML Stream Type
 * @dtf.feature.group DTF Properties
 * 
 * @dtf.feature.desc
 * 
 */
public class XMLInputStream extends PanguInputStream {
    public XMLInputStream(long size, String[] args) throws ParseException { 
        super(size,args,new XMLDecorator());
    }
}
