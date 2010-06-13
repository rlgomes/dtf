package com.yahoo.dtf.actions.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.conditionals.Condition;
import com.yahoo.dtf.config.Config;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.query.QueryIntf;

/**
 * @dtf.tag compare
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc The compare tag is used to compare events from different event
 *               files that contain related data. Such as the event file from 
 *               writing data to a service and the event file from reading that
 *               same data from the service. There has to be a way to relate 
 *               the data from both event files, either with a sequence id, 
 *               record id, etc. Once you're able to identify that two events
 *               are relevant to the same data then you can use the validate
 *               child tag of compare to validate what needs to be similar or
 *               equal in order for these two events to be correct.
 *               <br/><br/>
 *               <b>
 *               Be aware that this tag is still experimental and is going to 
 *               suffer some changes soon. These will be focused on making the
 *               tag more efficient but may require more drastic changes to 
 *               the way the tag is used. 
 *               </b>
 *               <br/><br/>
 *               
 * @dtf.tag.example 
 * <compare>
 *    <query uri="storage://OUTPUT/compare_write_data.txt"
 *           event="write.event"
 *           cursor="c1">
 *       <select>
 *           <field name="recordid"/>
 *           <field name="data"/>
 *       </select>
 *    </query>
 *           
 *    <query uri="storage://OUTPUT/compare_read_data.txt"
 *           event="read.event"
 *           cursor="c2">
 *       <select>
 *           <field name="recordid"/>
 *           <field name="data"/>
 *       </select>
 *    </query>
 *    
 *    <where><eq op1="${c1.recordid}" op2="${c2.recordid}"/></where>
 *   
 *    <validate>
 *       <assert><eq op1="${c1.data}" op2="${c2.data}"/></assert>
 *    </validate>
 * </compare>
 * 
 * @dtf.tag.example 
 * <compare>
 *    <query uri="storage://OUTPUT/read_data.txt"
 *           event="write.event"
 *           cursor="c1"/>
 *    <!-- We know that our data was generated in a certain way based on a 
 *         sequence ID that we used so we can easily validate our read data 
 *         without the original data to validate against and instead using the 
 *         attributes from the event -->
 *    <validate>
 *        <if>
 *            <neq op1="${c1.data}"
 *                 op2="${dtf.stream(random,${c1.size},${c1.recordid})}"/>
 *            <then>
 *                <log level="warn">
 *                Data was not equal!!! For recordid [${recordid}]
 *                </log> 
 *            </then>
 *        </if>
 *    </validate>
 * </compare>
 * 
 */
public class Compare extends Action {

    public Compare() { }
    
    public void execute() throws DTFException {
        ArrayList<Query> query = findActions(Query.class);
        Validate validate = (Validate) findFirstAction(Validate.class);
        Where where = (Where) findFirstAction(Where.class);
        
        if ( query.size() == 1 ) {
            /*
             * Simple iteration through the data and comparing it with a easily
             * re-generated data.
             */
            Query first = query.get(0);
            QueryIntf fquery = Query.getQueryIntf(first);
            Config config = getConfig();
            
            HashMap<String, String> outtermap = null;
            while ( (outtermap = fquery.next(false)) != null ) { 
                Iterator<Entry<String,String>> entries = outtermap.entrySet().iterator();
                while ( entries.hasNext() ) { 
                    Entry<String,String> entry = entries.next();
                    config.setProperty(entry.getKey(),entry.getValue());
                }

                validate.execute();
            }
        } else if ( query.size() == 2) { 
          Query first = query.get(0);
          Query second = query.get(1);
         
          QueryIntf fquery = Query.getQueryIntf(first);
          QueryIntf squery = Query.getQueryIntf(second);
         
          Condition condition = (Condition) 
                                         where.findFirstAction(Condition.class);
          Config config = getConfig();
          
          HashMap<String, String> outtermap = null;
          while ( (outtermap = fquery.next(false)) != null ) { 
              Iterator<Entry<String,String>> entries = outtermap.entrySet().iterator();
              while ( entries.hasNext() ) { 
                  Entry<String,String> entry = entries.next();
                  config.setProperty(entry.getKey(),entry.getValue());
              }

              long maxreset = squery.getResetCount()+2;
              HashMap<String, String> innermap = null;
              while ( (innermap = squery.next(true)) != null ) { 
                  entries = innermap.entrySet().iterator();
                  while ( entries.hasNext() ) { 
                      Entry<String,String> entry = entries.next();
                      config.setProperty(entry.getKey(),entry.getValue());
                  }
                  
                  if ( condition.evaluate() ) 
                      break;
                 
                  if ( squery.getResetCount() > maxreset ) {
                      throw new DTFException("Unable to satisfy the where clause "  +
                                             "and find the next correct event.");
                  }
              }
              
              validate.execute();
          }
        } else { 
            throw new DTFException("You need 1-2 query elements in order to use this tag.");
        }
    }
}
