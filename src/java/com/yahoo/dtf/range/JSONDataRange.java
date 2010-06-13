package com.yahoo.dtf.range;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.jxpath.JXPathContext;

import com.yahoo.dtf.exception.RangeException;
import com.yahoo.dtf.json.JSONException;
import com.yahoo.dtf.json.JSONObject;

public class JSONDataRange extends Range {
    
    private String expression = null;
    private long nextcalled = 0;
    
    private String jpath = null;
    private String json = null;
    
    private ArrayList<Object> names = null;
    
    public JSONDataRange() { }

    public static boolean matches(String expression) throws RangeException {
         if  ( expression.startsWith("jpath(") ) { 
             if ( expression.endsWith(")") ) { 
                 return true;
             }
             throw new RangeException("JPath range is invalid [" + expression + "]");
         }
         return false;
    }
    
    public JSONDataRange(String expression) throws RangeException {
        this.expression = expression;
        init();
    }
    
    public void init() throws RangeException {
        String arguments = expression.substring("jpath(".length(),
                                                expression.length()-1);
        
        String[] args = arguments.split(",/");
        json = args[0];
        jpath = "/" + args[1];
        
        try { 
            JSONObject obj = new JSONObject(json);
            JXPathContext ctx = JXPathContext.newContext(obj);
            Iterator iter = ctx.iterate(jpath);
            
            names = new ArrayList<Object>();
            while (iter.hasNext()) { 
                names.add(iter.next());
            }
        } catch (JSONException e) { 
            throw new RangeException("Error parsing JSON.",e);
        }
    }
    
    public boolean hasMoreElements() {
        return (names.size() != 0);
    }
    
    public String nextElement() throws RangeException {
        Object obj = names.remove(0);
        nextcalled++;
        return obj.toString();
    }

    public void reset() throws RangeException {
        init();
        nextcalled = 0;
    }
    
    public int size() {
        return names.size();
    }
    
    @Override
    public void restoreState(DataInputStream dis) throws RangeException {
        try { 
            expression = dis.readUTF();
            init();
            long calls = dis.readLong();
        
            // move this range into the same position.
            for(long i = 0; i < calls ; i++) 
                nextElement();
        } catch (IOException e) { 
            throw new RangeException("Error suspending state.",e);
        }
    }
    
    @Override
    public void suspendState(DataOutputStream dos) throws RangeException {
        try { 
            dos.writeUTF(expression);
            dos.writeLong(nextcalled);
        } catch (IOException e) { 
            throw new RangeException("Error suspending state.",e);
        }
    }
}
