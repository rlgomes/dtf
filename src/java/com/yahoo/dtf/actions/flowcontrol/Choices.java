package com.yahoo.dtf.actions.flowcontrol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.regex.Pattern;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag choices
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc Choices tag can be used at any point you want to execute a 
 *               group of different actions with a probability associated with
 *               executing each of the actions. This way you can generate a load
 *               pattern that is a bit more complex than just the number of 
 *               operations per second.
 * 
 * @dtf.tag.example 
 * <distribute workers="3" iterations="1..10">
 *     <choices>
 *         <choose howoften="25%">
 *             <event name="test.event2"/>
 *         </choose>
 *         <choose howoften="25%">
 *             <event name="test.event1"/>
 *         </choose>
 *         <choose howoften="50%">
 *             <event name="test.event3"/>
 *         </choose>
 *     </choices> 
 * </distribute>
 *
 * @dtf.tag.example 
 * <for property="i" range="1..1000">
 *     <choices>
 *         <choose howoften="5%">
 *             <event name="test.event2"/>
 *         </choose>
 *         <choose howoften="5%">
 *             <event name="test.event1"/>
 *         </choose>
 *         <choose howoften="90%">
 *             <event name="test.event3"/>
 *         </choose>
 *     </choices> 
 * </for>
 *
 */
public class Choices extends Action {
  
    private static final String CHOICESTATE = "dtf.choices.state";
   
    private static class Chosen {
        public Choose choose = null;
        public double chosen = 0;
        public double howoften = 0;
    }
    
    private static class ChoiceState { 
        public long totalchoices = 1;
        public ArrayList<Chosen> chosen = null;
    }
    
    private static Pattern pattern = Pattern.compile("[0-9]?[0-9]?[0-9]%");
    private Integer percToInt(String value) throws ParseException {
        if (!pattern.matcher(value).matches())
            throw new ParseException("Percentage value expected for ["
                    + value + "] should match " + pattern.toString());
        
        try { 
            value = value.replace("%","");
            return new Integer(value);
        } catch (NumberFormatException e) { 
            throw new ParseException("Value is not a Integer [" + value + "]",e);
        }
    }
   
    private static Object _csLock = new Object();
    private HashMap<String, ChoiceState> getChoiceStates() { 
        synchronized (_csLock) {
	        HashMap<String, ChoiceState> cs = 
	           (HashMap<String, ChoiceState>)getGlobalContext(CHOICESTATE);
	        if (cs == null) { 
	            cs = new HashMap<String, ChoiceState>();
	            registerGlobalContext(CHOICESTATE, cs);
	        }
	        return cs;
        }
    }
    
    public void execute() throws DTFException {
        HashMap<String, ChoiceState> css = getChoiceStates();
        ChoiceState cs = null;
       
        // synchronize on the Choices list for all threads to find if we already
        // have the ChoiceState object created otherwise create it just once.
        synchronized (css) { 
	        cs = css.get(""+ this.hashCode());
	        if (cs == null) { 
	            cs = new ChoiceState();
	            ArrayList<Choose> choices = findActions(Choose.class);
	            
	            // sort the choices by the percentage number ;)
	            Collections.sort(choices, new Comparator<Choose>() { 
	                public int compare(Choose o1, Choose o2) {
	                    try {
	                        Integer thisWhen = percToInt(o1.getHowoften()); 
	                        Integer otherWhen = percToInt(o2.getHowoften()); 
	                        // reverse order
	                        return otherWhen.compareTo(thisWhen);
	                    } catch (ParseException e) {
	                        return 0;
	                    }
	                }
	            });
	              
	            ArrayList<Chosen> chosen = new ArrayList<Chosen>();
	            for(int i = 0; i < choices.size(); i++) { 
	                Chosen aux = new Chosen();
	                aux.choose = choices.get(i);
	                aux.howoften = percToInt(aux.choose.getHowoften())/100.0f; 
	                chosen.add(aux);
	            }
	            
	            cs = new ChoiceState();
	            cs.chosen = chosen;
	          
	            css.put(""+this.hashCode(), cs);
	        }
        }
	     
        Chosen chosen = null;
        
        synchronized (cs) { 
	        int whonext = -1;
	        double maxdist = Long.MIN_VALUE;
	       
	        int total = 0;
	        for (int i = 0; i < cs.chosen.size(); i++) { 
	            Chosen aux = cs.chosen.get(i);
	          
	            /*
	             * Calculate the percentage of times I have been chosen and then 
	             * calculate the distance between that and the actual percentage of
	             * tiem I should have been chosen.
	             */
	            double got = (aux.chosen/cs.totalchoices);
	            double distance = aux.howoften-got;
	            total += aux.howoften * 100;
	            /*
	             * The next choice should be the one with the biggest distance left
	             * to go to be at the percentage that we are requiring.
	             */
	            if (distance > maxdist) { 
	                whonext = i;
	                maxdist = distance;
	            }
	        }
	      
	        if ( total > 100 )  
	            throw new ParseException("Adding up choices yields more than 100% at [" + total + "]");
	      
	        chosen = cs.chosen.get(whonext);
	        if ((chosen.chosen/cs.totalchoices) <= chosen.howoften) {
	            chosen.chosen++;
	        } else { 
	            chosen = null;
	        }

	        cs.totalchoices++;
        }
        
        if ( chosen != null ) 
            chosen.choose.execute();
    }
}
