package com.yahoo.dtf.actions.protocol.deploy;

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
 *  <dtfa host="localhost" user="testuser">
 *      <property name="node.id" value="1"/>
 *  </dtfa>
 *  
 * @dtf.tag.example
 *  <dtfa host="testmachine.corp.network.com" user="testuser" path="dtf/dtfa2">
 *      <property name="region" value="east"/>
 *      <property name="id" value="m1"/>
 *  </dtfa>
 */
public class Dtfa extends DTFNode {

}
