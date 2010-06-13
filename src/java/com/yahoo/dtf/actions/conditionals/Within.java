package com.yahoo.dtf.actions.conditionals;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yahoo.dtf.actions.conditionals.Condition;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag within
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc Evaluates if value is within range of a specified tolerance 
 *               value. The value itself can be any numeric value while the 
 *               tolerance can be any of the following representations:
 *               <br/>
 *               <pre>
 *               5%      - op1 is within 5% of op2
 *               [5,20]% - op1 can be lower than op2 by 5% or can be higher than 
 *                         op2 by upto 20%
 *              
 *               10      - op1 can be lower than op2 by 10x
 *               </pre>
 * 
 * @dtf.tag.example 
 * <if>
 *     <within op1="${average}" op2="${value}" tolerance="[5,10]%"/>
 *     <then>
 *         <log>parameters within tolerance</log>
 *     </then>
 *     <else>
 *         <fail>The average value is more than -5,+10% off from previous value</fail>
 *     </else>
 * </if>
 * 
 * @dtf.tag.example 
 * <if>
 *     <within op1="${average}" op2="${value}" tolerance="5%"/>
 *     <else>
 *         <fail>The average value is more than 5% off from previous value</fail>
 *     </else>
 * </if>
 * 
 */
public class Within extends Condition {
   
    private String tolerance = null;
    
    public Within() { }
    
    public boolean evaluate() throws DTFException {
        double op1 = toDouble("op1", getOp1());
        double op2 = toDouble("op2", getOp2());
        Tolerance tolerance = getPercentage(getTolerance());
        
        if (op1 < op2) { 
            return Math.abs(op1 - op2) / op2 <= tolerance.lower;
        } else { 
            return Math.abs(op2 - op1) / op1 <= tolerance.upper;
        }
    }
    
    private static class Tolerance { 
        public double lower = 0;
        public double upper = 0;
    }
    
    private Tolerance getPercentage(String percentage) throws ParseException { 
        Tolerance result = new Tolerance();
        String perc = replaceProperties(percentage);
        
        if (perc.indexOf('%') != -1) {
            perc = perc.substring(0,perc.indexOf('%'));
           
            Pattern pattern = Pattern.compile("(\\[)([^,]*),([^]]*)(\\])");
            Matcher matcher = pattern.matcher(perc);
            
            if (matcher.matches()) { 
                String lowervalue = matcher.group(2);
                String uppervalue = matcher.group(3);
                result.lower = toDouble("lowervalue",lowervalue) / 100.0f;
                result.upper = toDouble("uppervalue",uppervalue) / 100.0f;
            } else { 
                result.lower = toDouble("tolerance", perc) /  100.0f;
                result.upper = toDouble("tolerance", perc) /  100.0f;
            }
        } else {
            result.lower = toDouble("tolerance", perc);
            result.upper = toDouble("tolerance", perc);
        }
        
        return result;
    }
    
    public String getTolerance() throws ParseException { return replaceProperties(tolerance); } 
    public void setTolerance(String tolerance) { this.tolerance = tolerance; } 
    
    public String explanation() throws DTFException {
        return getOp1() + " within " + getTolerance() + " of " + getOp2();
    }
}
