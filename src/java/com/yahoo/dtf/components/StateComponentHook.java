package com.yahoo.dtf.components;

import java.util.ArrayList;
import java.util.HashMap;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.state.ActionState;

/**
 * This class is responsible for knowing which components currently hold remote
 * state for threads from the runner side and will issue a RemoveState action 
 * for all the recently dead runner threads in order to free up resources on the
 * agent side at runtime.
 * 
 * The Node class itself already makes sure to clean up all the remote state 
 * generated at runtime but if we don't do some runtime clean up we can use up 
 * all the resources on an agent over a very long period of creating and 
 * destroying thread son the runner side.
 * 
 * @author rlgomes
 */
public class StateComponentHook implements ComponentHook {

    private final static String SENT_STATES = "dtf.sentstates.";
    
    private static ArrayList<String> deadThreads = new ArrayList<String>();
    
    private synchronized static ArrayList<String> getSentStates(String id) {
        HashMap<String, ArrayList<String>> map = 
                           (HashMap<String, ArrayList<String>>) 
                                      Action.getGlobalContext(SENT_STATES + id);

        if (map == null) {
            map = new HashMap<String, ArrayList<String>>();
            Action.registerGlobalContext(SENT_STATES + id, map);
        }
       
        ArrayList<String> sent = map.get(id); 
        if ( sent == null ) { 
            sent = new ArrayList<String>();
            map.put(id, sent); 
        }

        return sent;
    }
    
    public static void threadDead(String id) { 
        synchronized (deadThreads) { 
            deadThreads.add(id);
        }
    }
    
    public ArrayList<Action> handleComponent(String id) throws DTFException {
        ArrayList<Action> result = new ArrayList<Action>();
        ArrayList<String> sentState = getSentStates(id);
        
        synchronized ( sentState ) { 
            for (int i = 0; i < sentState.size(); i++) { 
                String thread = sentState.get(i);
                if ( deadThreads.contains(thread) ) { 
                    CleanUpState cs = new CleanUpState();
                    cs.setId(thread);
                    result.add(cs);
                    sentState.remove(thread);
                }
            }
        }

        String tname = ActionState.getInstance().getCurrentID();
        sentState.add(tname);
        
        return result;
    }
}
