package com.yahoo.dtf.streaming;

import org.pangu.tree.decorators.JSONDecorator;

import com.yahoo.dtf.exception.ParseException;

/** 
 * @dtf.feature JSON Stream Type
 * @dtf.feature.group DTF Properties
 * 
 * @dtf.feature.desc
 * 
 */
public class JSONInputStream extends PanguInputStream {
    public JSONInputStream(long size, String[] args) throws ParseException { 
        super(size,args,new JSONDecorator());
    }
}