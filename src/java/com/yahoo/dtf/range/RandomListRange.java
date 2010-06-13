package com.yahoo.dtf.range;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import com.yahoo.dtf.range.Range;
import com.yahoo.dtf.range.RangeFactory;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.RangeException;

public class RandomListRange extends Range {
   
    public final static int MAXSIZE = 10*1024;

    private String[] elements = null;
    
    private long generated = 0;
    
    private Random _rand = null;
    
    public RandomListRange() { }

    public static boolean matches(String expression) {
        return expression.startsWith("random(") && expression.endsWith(")");
    }
    
    public RandomListRange(String expression) throws RangeException {
        _rand = new Random(System.nanoTime());
        String arguments = expression.substring("random(".length(),
                                                expression.length()-1);
        
        String subrange = arguments;
        try {
            Range range = RangeFactory.getRange(subrange);
            if (range.size() < MAXSIZE) { 
                elements = new String[range.size()];
                int i = 0;
                while(range.hasMoreElements()) { 
                    String element = range.nextElement();
                    elements[i++] = element;
                }
            } else 
                throw new RangeException("Random ranges cannot exceed " + 
                                         MAXSIZE + " elements.");
        } catch (DTFException e) {
            throw new RangeException("Error parsing range.",e);
        }
    }
    
    public boolean hasMoreElements() {
        return (generated < elements.length);
    }
    
    public String nextElement() {
        int rand = _rand.nextInt();
        
        synchronized(this) { 
            generated++;
            return elements[Math.abs(rand % elements.length)];
        }
    }

    public void reset() {
        generated = 0;
    }
    
    public int size() {
        return elements.length;
    }
    
    @Override
    public void restoreState(DataInputStream dis) throws RangeException {
        _rand = new Random(System.currentTimeMillis());

        try { 
            generated = dis.readLong();
            int size = dis.readInt();
            elements = new String[size];
            for (int i = 0; i < elements.length; i++) 
                elements[i] = dis.readUTF();
        } catch (IOException e) { 
            throw new RangeException("Unable to restore range.",e);
        }
    }
    
    @Override
    public void suspendState(DataOutputStream dos) throws RangeException {
        try { 
            dos.writeLong(generated);
            dos.writeInt(elements.length);
            for (int i = 0; i < elements.length; i++) 
                dos.writeUTF(elements[i]);
        } catch (IOException e) { 
            throw new RangeException("Unable to suspend range.",e);
        }
    }
}
