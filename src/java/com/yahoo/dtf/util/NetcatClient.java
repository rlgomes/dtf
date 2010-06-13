package com.yahoo.dtf.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.telnet.TelnetClient;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;

public class NetcatClient {

	private TelnetClient _telnet = null;
	
	private OutputStream    _output = null;
	private BufferedReader  _input = null;
	
	public NetcatClient(String host, int port) throws DTFException { 
	    this(host,port,30000);
	}
	
	public NetcatClient(String host, int port, int timeout) throws DTFException { 
		_telnet = new TelnetClient();
	    
		try {
			_telnet.connect(host,port);
			_telnet.setSoTimeout(timeout);
			_telnet.setSoLinger(true, 1000);
			// if we're unable to connect we should terminate the agent.
		} catch (SocketException e) {
			throw new DTFException("Unable to connect to command port on " 
					               + host + ":" + port,e);
		} catch (IOException e) {
			throw new DTFException("Unable to connect to command port on " 
					               + host + ":" + port,e);
		}	
		
		_output = _telnet.getOutputStream();
		_input  = new BufferedReader(new InputStreamReader(_telnet.getInputStream()));
	}
	
	public void pushCommand(String command) throws DTFException { 
		try {
    		_output.write((command + "\n").getBytes());
    		_output.flush();
		} catch (IOException e) {
			throw new DTFException("Unable to pushCommand.",e);
		}
	}

	public void push(String command) throws DTFException { 
		try {
    		_output.write(command.getBytes());
    		_output.flush();
		} catch (IOException e) {
			throw new DTFException("Unable to pushCommand.",e);
		}
	}
	
	public String readLine() throws DTFException { 
		try {
			return _input.readLine();
		} catch (IOException e) {
			throw new DTFException("Unable to read line.",e);
		}
	}

	public char read() throws DTFException { 
		try {
			return (char)_input.read();
		} catch (IOException e) {
			throw new DTFException("Unable to read line.",e);
		}
	}
	
	public void waitForDisconnect() throws DTFException {
		close();
		try {
			while (_input.readLine() != null) {
				Action.getLogger().info("Waiting for disconnect.");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) { }
			}
		} catch (IOException ignore) { }
	}
	
	public void close() throws DTFException { 
		try {
    		_telnet.disconnect();
		} catch (IOException e) {
			throw new DTFException("Error closing telnet connection.",e);
		}
	}
}
