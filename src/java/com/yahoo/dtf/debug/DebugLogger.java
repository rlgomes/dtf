package com.yahoo.dtf.debug;

import java.io.PrintWriter;

import org.apache.log4j.Logger;

import com.yahoo.dtf.logger.DTFLogger;

public class DebugLogger extends DTFLogger {

    private PrintWriter _writer = null;
    
    protected DebugLogger(PrintWriter writer, Logger logger) {
        super(logger);
        _writer = writer;
    }
    
    @Override
    public void info(Object message) {
        _writer.println(message.toString());
    }
    
    @Override
    public void info(Object message, Throwable t) {
        _writer.println(message.toString());
        t.printStackTrace(_writer);
    }
    
    @Override
    public void error(Object message) {
        _writer.println(message.toString());
    }
    
    @Override
    public void error(Object message, Throwable t) {
        _writer.println(message.toString());
        t.printStackTrace(_writer);
    }
    
    @Override
    public void warn(Object message) {
        _writer.println(message.toString());
    }
    
    @Override
    public void warn(Object message, Throwable t) {
        _writer.println(message.toString());
        t.printStackTrace(_writer);
    }
    
    @Override
    public void debug(Object message) {
        _writer.println(message.toString());
    }
    
    @Override
    public void debug(Object message, Throwable t) {
        _writer.println(message.toString());
        t.printStackTrace(_writer);
    }

}
