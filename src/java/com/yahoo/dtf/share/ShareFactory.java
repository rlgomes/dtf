package com.yahoo.dtf.share;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.yahoo.dtf.exception.ShareException;

public class ShareFactory {

    private static HashMap<String, Class> _shares = new HashMap<String, Class>();
    
    public static Share getShare(String type, String id) throws ShareException { 
        Class sc = _shares.get(type);
        
        if (sc == null)
            throw new ShareException("Unsupported share type [" + type + "]");
        
        Class[] parameters = new Class[] { String.class };
        Object[] args = new Object[] { id };
        
        try {
            Share share = (Share) sc.getConstructor(parameters).newInstance(args);
            return share;
        } catch (IllegalArgumentException e) {
            throw new ShareException("Unable to instantiate share [" + type + "].",e);
        } catch (SecurityException e) {
            throw new ShareException("Unable to instantiate share [" + type + "].",e);
        } catch (InstantiationException e) {
            throw new ShareException("Unable to instantiate share [" + type + "].",e);
        } catch (IllegalAccessException e) {
            throw new ShareException("Unable to instantiate share [" + type + "].",e);
        } catch (InvocationTargetException e) {
            throw new ShareException("Unable to instantiate share [" + type + "].",e);
        } catch (NoSuchMethodException e) {
            throw new ShareException("Unable to instantiate share [" + type + "].",e);
        }
    }
    
    public static <T extends Share> 
           void registerShare(String name, Class<T> shareclass) { 
        _shares.put(name, shareclass);
    }
    
    public static ArrayList<String> getShareNames() { 
        ArrayList<String> names = new ArrayList<String>();
        Iterator<String> iter = _shares.keySet().iterator();
        
        while (iter.hasNext()) 
            names.add(iter.next());
        
        return names;
    }
}
