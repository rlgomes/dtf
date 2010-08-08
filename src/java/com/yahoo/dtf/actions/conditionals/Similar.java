package com.yahoo.dtf.actions.conditionals;

import com.yahoo.dtf.actions.conditionals.Condition;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag similar
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc 
 * <p>
 * Evaluates if op1 and op2 are within the tolerance level of similarity that is
 * desired for these two pieces of data. The value of tolerance can be expressed
 * like so:
 * </p>
 * <br/>
 * <pre>
 *  50%      - op1 is at least 50% similar to op2
 *  0.15     - op1 is at least 15% similar to op2
 * </pre>
 * <p>
 * The actual similarity is calculated based on how many bytes are actually the
 * same between the two pieces of data. There is no attempt to calculate if
 * the data has been shifted and therefore the comparison is a direct position 
 * to position comparison.
 * </p>
 * 
 * @dtf.tag.example 
 * <if>
 *     <similar op1="AAAAAAA" op2="AAAAAAB" value="15%"/>
 *     <else>
 *         <fail>The average value is more than 5% off from previous value</fail>
 *     </else>
 * </if>
 * 
 */
public class Similar extends Condition {
   
    private String value = null;
    
    public Similar() { }
    
    public boolean evaluate() throws DTFException {
        String op1 = getOp1();
        String op2 = getOp2();
        
        double value = getValue();
        boolean result = true;
        
        byte[] bytes1 = op1.getBytes();
        byte[] bytes2 = op2.getBytes();
        
        double similarity = similarity(bytes1,bytes2);
       
        if ( similarity < value ) { 
            String msg = op1 + " is not " + value + "% similar to " + op2;
            registerContext(ASSERT_EXP_CTX, msg);
            result = false;
        }
        
        return result;
    }
    
    public static double similarity(byte[] s, byte[] t)
    {
        double diff = 0;
        double count = 0;
        
        int m = s.length;
        int n = t.length;
        int i = 0, j = 0;
       
        // figure out what are the actual differences
        while(i < m && j < n) {
            count++;
            if (s[i] != t[j]) 
                diff++;
            i++;
            j++;
        }
        
        // go through the remaining values of the longest array
        while( i < m || j < n ) {
            count++;
            if (i == m) { // in s but not in t
                j++;
            } else if (j == n) { // in t but not in s 
                i++;
            }
            diff++;
        }

        return (count-diff)/count;
    }
    
    public double getValue() throws ParseException { 
        double result = 0;
        String perc = replaceProperties(value);
        if (perc.indexOf('%') != -1) {
            perc = perc.substring(0,perc.indexOf('%'));
            result = toDouble("value", perc)/100.0f;
        } else { 
            result = toDouble("value", perc);
        }
        
        return result;
    } 
    
    public void setValue(String value) { this.value = value; } 
}
