package com.yahoo.dtf.debug.xmltrace;

import java.io.PrintWriter;
import java.util.Enumeration;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.state.ActionState;
import com.yahoo.dtf.state.DTFState;

public class XMLTrace {
    
    private static String pad(String str, int count) { 
        int length = str.length();
        StringBuffer result = new StringBuffer(str);
        
        if ( length > count ) { 
            return "..." + str.substring((length-count)+3);
        }
        
        for (int i = length; i < count ; i++) {
            if ( i % 2 == 0 )
                result.append(" ");
            else  
                result.insert(0, " ");
        }
       
        return result.toString();
    }
    
    public static void writeXMLTrace(PrintWriter pw) { 
        ActionState as = ActionState.getInstance();
        Enumeration<String> stateIds = as.getStates();
        
        pw.println("");
        pw.println("--------------------------------------------------------------------------------");
        pw.println("  Thread ID    |              XML Trace                |      Action State      ");
        pw.println("--------------------------------------------------------------------------------");
        while (stateIds.hasMoreElements()) { 
            String id = stateIds.nextElement();
            DTFState state = as.getState(id);
            Action action = state.getAction();
          
            /*
             * By setting the state we can be sure we'll resolve any properties
             * that maybe necessary to do the toString on the Action being
             * executed.
             */
            as.setState(state);
            if ( action != null ) { 
                pw.println(pad(id,15) + "|" +
                           pad(action.getFilename() +  
                           ":" + action.getLine() + 
                           "," + action.getColumn(),39) + "| " +
                           action.toString());
            } else { // XXX: hide unknown threads for now 
                //trace.append(pad(id,15) + "| UNKOWN ");
            }
            as.delState();
        }
        pw.println("--------------------------------------------------------------------------------");
        pw.flush();
    }
}
