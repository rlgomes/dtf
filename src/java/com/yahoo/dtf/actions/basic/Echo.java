package com.yahoo.dtf.actions.basic;

import com.yahoo.dtf.actions.util.CDATA;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag echo
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc The echo tag is used to print out test case information during 
 *               the execution of the test case. It is preferred that you use 
 *               the tag {@dtf.link log} instead of echo since you can easily 
 *               set the logging level and that way have control over what is
 *               being logged in your test run just by editing the 
 *               dtf.properties file and changing the log4j properties.
 * 
 * @dtf.tag.example 
 * <echo message="Hello World"/>
 * 
 * @dtf.tag.example
 * <echo>Hello World</echo>
 */
public class Echo extends CDATA {
   
    /**
     * @dtf.attr message
     * @dtf.attr.desc The message attribute contains the message to be logged to
     *                the test execution log.
     */
    private String message = null;

    public Echo() { }
    
    public void execute() throws DTFException {
        String msg = getMessage();
        String cdata = getCDATA();
        if (msg != null)
            getLogger().info(msg);
        else if (cdata != null)
            getLogger().info(cdata);
        else
            throw new DTFException("Echo does not contain a message to be printed.");
    }

    public String getMessage() throws ParseException { return replaceProperties(message); }
    public void setMessage(String message) { this.message = message; }
}
