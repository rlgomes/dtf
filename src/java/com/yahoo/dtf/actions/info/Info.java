package com.yahoo.dtf.actions.info;

import com.yahoo.dtf.actions.util.CDATA;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag info
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc This node has no executional value and is only used by external
 *               XSLT stylesheets that can aggregate the information available
 *               in each testcase to generate human readable reports.
 * 
 * @dtf.tag.example 
 *  <info>
 *      <author>
 *          <name>Some Authore</name>
 *          <email>author@server.com</email>
 *      </author>
 *      <description>DTF test.</description>
 *  </info>
 */
public class Info extends CDATA {
    public void execute() throws DTFException {
        executeChildren();
    }
}
