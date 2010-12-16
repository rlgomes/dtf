package com.yahoo.dtf.actions.protocol.deploy;

/**
 * @dtf.tag dtfc 
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc Identifies the DTFC that is going to be deployed to startup the
 *               desired DTF setup. Within a DTFC you can identify all of the DTFA
 *               's required to create this setup as well as the exact DTFX to
 *               execute and run a given test.
 *
 * @dtf.tag.example
 *  <dtfc host="localhost" user="builduser" path="dtf/dtfc">
 *      <dtfa host="localhost" user="builduser" path="dtf/dtfa1"/>
 *  </dtfc>
 */
public class Dtfc extends DTFNode {

}
