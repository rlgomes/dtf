package com.yahoo.dtf.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.yahoo.dtf.exception.DTFException;

public class HashUtil {

    public static String sha1(String data) throws DTFException { 
        try { 
		    MessageDigest md = MessageDigest.getInstance("SHA-1");
		    md.update(data.getBytes("UTF-8"));
		    byte[] sha1hash = md.digest();
		    return convertToHex(sha1hash);
        } catch (UnsupportedEncodingException e ) { 
            throw new DTFException("Unable to calculate SHA1.",e);
        } catch (NoSuchAlgorithmException e) {
            throw new DTFException("Unable to calculate SHA1.",e);
        }
    }
    
    public static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        }
        return buf.toString();
    }

}
