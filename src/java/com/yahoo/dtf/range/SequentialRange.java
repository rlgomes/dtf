package com.yahoo.dtf.range;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.yahoo.dtf.range.Range;
import com.yahoo.dtf.util.NumberUtil;
import com.yahoo.dtf.exception.RangeException;

public class SequentialRange extends Range {

    private Long start;
    private Long end;
    private Long cur;
 
    private int inc = 0;
    
    private boolean isChar = false;
    
    public SequentialRange() { }
    
    public static boolean matches(String expr) { 
        return expr.indexOf("..") != -1;
    }
    
    public SequentialRange(String expression) throws RangeException { 
        int split = expression.indexOf("..");
        
        String a = expression.substring(0,split);
        String b = expression.substring(split+2);

        if ( a.length() == 1 && b.length() == 1 ) {  
            start = (long) a.charAt(0);
            end = (long) b.charAt(0);
            isChar = true; 
        } else if ( NumberUtil.isDouble(a) && NumberUtil.isDouble(b) ) { 
            start = Double.valueOf(a).longValue();
            end = Double.valueOf(b).longValue();
        }
        
        inc = ( start < end ? 1 : -1 );
    }
    
    public boolean hasMoreElements() {
        return (cur == null || 
               (inc == 1 && cur < end) || 
               (inc == -1 && cur > end));
    }

    public String nextElement() {
        if  (cur == null )
            cur = start;
        else 
            cur = cur + inc;
       
        if ( isChar ) { 
            return "" + ((char)cur.longValue());
        } else { 
            return "" + cur;
        }
    }

    public void reset() {
        cur = null;
    }
   
    public int size() {
        return (int)((end - start) + 1);
    }
    
    @Override
    public void restoreState(DataInputStream dis) throws RangeException {
       try { 
           start = dis.readLong();
           end = dis.readLong();
           inc = dis.readInt();
           isChar = dis.readBoolean();
           
           if ( dis.readBoolean() )
               cur = dis.readLong();
       } catch (IOException e ) {
           throw new RangeException("Error restoring range.",e);
       }
    }
    
    @Override
    public void suspendState(DataOutputStream dos) throws RangeException {
       try { 
           dos.writeLong(start);
           dos.writeLong(end);
           dos.writeInt(inc);
           dos.writeBoolean(isChar);
           
           if ( cur != null ) { 
               dos.writeBoolean(true);
               dos.writeLong(cur);
           } else 
               dos.writeBoolean(false);
       } catch (IOException e ) {
           throw new RangeException("Error suspending range.",e);
       }
    }
}
