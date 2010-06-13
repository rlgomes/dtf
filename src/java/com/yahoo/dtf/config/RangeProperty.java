package com.yahoo.dtf.config;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.yahoo.dtf.config.DynamicProperty;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.exception.RangeException;
import com.yahoo.dtf.range.Range;

public class RangeProperty implements DynamicProperty {
  
    private String _name = null;
    private Range _range = null;
    private boolean _recycle = false;
    
    public RangeProperty(String name, Range range, boolean recycle) { 
        _name = name;
        _range = range;
        _recycle = recycle;
    }

    public synchronized String getValue(String args) throws ParseException {
        try { 
	        if (!_range.hasMoreElements())
	            if (_recycle)
	                _range.reset();
            else 
                throw new ParseException("Range [" + _name + "] out of elements.");
       
            return _range.nextElement();
        } catch (RangeException e) { 
            throw new ParseException("Error handling range.",e);
        }
    }
    
    public String getName() { return _name; }
   
    /**
     * We control the suspending/restoring from this point because this way we
     * can easily keep track of the exact Range class that was used and restore
     * correctly without having to put that logic into each of the Range classes.
     * 
     * @param dos
     */
    public void suspendState(DataOutputStream dos) throws RangeException { 
        String classname = _range.getClass().getName();
        try {
            dos.writeUTF(classname);
            dos.writeUTF(_name);
            dos.writeBoolean(_recycle);
            _range.suspendState(dos);
        } catch (IOException e) {
            throw new RangeException("Error suspending range.",e);
        }
    }
   
    
    /**
     * We control the suspending/restoring from this point because this way we
     * can easily keep track of the exact Range class that was used and restore
     * correctly without having to put that logic into each of the Range classes.
     * 
     * @param dis
     */
    public static RangeProperty restoreState(DataInputStream dis) throws RangeException { 
        try {
            String classname = dis.readUTF();
            String name = dis.readUTF();
            boolean recycle = dis.readBoolean();
            
            Object obj = Class.forName(classname).newInstance();
            
            if ( !(obj instanceof Range) )  {
                throw new RangeException("Serious issue, this isn't even a Range ["
                                         + obj.getClass().getName() + "]");
            }
            
            Range range = (Range)obj;
            range.restoreState(dis);
           
            RangeProperty rp = new RangeProperty(name,range,recycle);
            return rp;
        } catch (IOException e) {
            throw new RangeException("Error restoring range.",e);
        } catch (ClassNotFoundException e) {
            throw new RangeException("Error restoring range.",e);
        } catch (InstantiationException e) {
            throw new RangeException("Error restoring range.",e);
        } catch (IllegalAccessException e) {
            throw new RangeException("Error restoring range.",e);
        }
    }
   
    @Override
    public int hashCode() {
        return _name.hashCode() + _range.hashCode() + (_recycle ? 1 : 0); 
    }
}
