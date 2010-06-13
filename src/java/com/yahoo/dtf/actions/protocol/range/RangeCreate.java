package com.yahoo.dtf.actions.protocol.range;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.config.RangeProperty;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.util.ByteArrayUtil;

public class RangeCreate extends Action {

    private String name = null;
    
    private byte[] rangedata = null;
    
    @Override
    public void execute() throws DTFException {
        ByteArrayInputStream bais = new ByteArrayInputStream(rangedata);
        DataInputStream dis = new DataInputStream(bais);
        RangeProperty rp = RangeProperty.restoreState(dis);
        try {
            dis.close();
        } catch (IOException e) { 
            throw new ParseException("Error transporting range.",e);
        }
        getConfig().put(rp.getName(), rp);
    }
    
    public void setName(String name) { this.name = name; }
    public String getName() throws ParseException { 
        return replaceProperties(name);
    }

    public String getRangedata() {
        return ByteArrayUtil.byteArrayToHexString(rangedata,rangedata.length);
    }
    
    public void setRangedata(String string) { 
        this.rangedata = ByteArrayUtil.hexToByteArray(string);
    }
    
    public void bytes(byte[] bytes) { this.rangedata = bytes.clone(); } 
}
