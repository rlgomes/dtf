package com.yahoo.dtf.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public abstract class StreamUtil {

	public static Vector<String> readToVector(InputStream in) throws IOException {
		BufferedReader out = new BufferedReader(new InputStreamReader(in));
		
		Vector<String> result = new Vector<String>();
		String line = null;
		while ((line = out.readLine()) != null) {
			result.add(line);
		}
		
		return result;
	}
	
    public static String readToString(InputStream in) throws IOException {
        BufferedReader out = new BufferedReader(new InputStreamReader(in));
        
        StringBuffer result = new StringBuffer();
        String line = null;
        while ((line = out.readLine()) != null) {
            result.append(line + "\n");
        }
        
        return result.toString();
    }
	
	public static void consume(InputStream in)  throws IOException {
		BufferedReader out = new BufferedReader(new InputStreamReader(in));
		while (out.readLine() != null);
	}
}
