package com.yahoo.dtf.actions.protocol.deploy;

import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag dtfa
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc identifies the DTFA that is going to be deployed to startup the
 *               desired DTF setup.
 *
 * @dtf.tag.example 
 *  <dtfx host="${host}"
 *        user="${user}"
 *        path="dtf/dtfx"
 *        test="tests/ut/echo.xml"
 *        logs="tests/ut/output/ut_results.xml">
 *  </dtfx>
 *  
 * @dtf.tag.example 
 *  <dtfx host="testmachine2.corp.network.com"
 *        user="testuser1"
 *        test="tests/ut/ut.xml"/>
 */
public class Dtfx extends DTFNode {

    /**
     * @dtf.attr test
     * @dtf.attr.desc identifies the test to execute on the DTFX that is 
     *                specified by this XML tag.
     */
    private String test = null; 

    /**
     * @dtf.attr logs
     * @dtf.attr.desc a comma separated list of the remote files to save from 
     *                the remote machine and the location is relative to the 
     *                path you identified with the path attribute. 
     */
    private String logs = null;
    
    public String getTest() throws ParseException { return replaceProperties(test); }
    public void setTest(String test) { this.test = test; }

    public String getLogs() throws ParseException { return replaceProperties(logs); }
    public void setLogs(String logs) { this.logs = logs; }
}
