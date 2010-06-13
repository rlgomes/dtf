package com.yahoo.dtf.range;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.yahoo.dtf.range.Range;
import com.yahoo.dtf.exception.RangeException;

public class EnumeratedRange extends Range {

    private String regexp = null;
    private String original = null;
    
    private boolean _done = false;
    
    public EnumeratedRange() { 
        
    }
    
    public EnumeratedRange(String expression) throws RangeException {
        this.regexp = expression;
        this.original = regexp;
        
        if (expression.length() == 0)
            _done = true;
    }
    
    public boolean hasMoreElements() {
        return !_done;
    }
    
    public String nextElement() {
        int indexOfComa = regexp.indexOf(",");
        String result = null;
        
        if (indexOfComa == -1) {
            _done = true;
            result = regexp;
        } else {
            result = regexp.substring(0,indexOfComa);
            regexp = regexp.substring(indexOfComa+1,regexp.length());
        }
     
        return result;
    }

    public void reset() {
        regexp = original;
        _done = false;
    }
    
    public int size() {
        return original.split(",").length;
    }
    
    @Override
    public void restoreState(DataInputStream dis) throws RangeException {
        try { 
	        regexp = dis.readUTF();
	        original = dis.readUTF();
	        _done = dis.readBoolean();
        } catch (IOException e) { 
            throw new RangeException("Error restoring range.",e);
        }
    }
    
    @Override
    public void suspendState(DataOutputStream dos) throws RangeException {
        try { 
	        dos.writeUTF(regexp);
	        dos.writeUTF(original);
	        dos.writeBoolean(_done);
        } catch (IOException e) { 
            throw new RangeException("Error suspending range.",e);
        }
    }
}
