package com.yahoo.dtf.actions.http.server;

import java.io.IOException;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpException;
import org.apache.http.HttpServerConnection;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpService;

import com.yahoo.dtf.logger.DTFLogger;

class HTTPRunnable implements Runnable {
    
    private DTFLogger logger = DTFLogger.getLogger(HTTPRunnable.class);

    private HttpService httpservice = null ;
    private HttpServerConnection conn = null;
    private RequestListener rl = null;

    public HTTPRunnable(HttpService httpservice,
                        HttpServerConnection conn,
                        RequestListener rl) {
        super();
        this.httpservice = httpservice;
        this.conn = conn;
        this.rl = rl;
    }

    public void run() {
        HttpContext context = new BasicHttpContext(null);
        try {
            while (!Thread.interrupted() && this.conn.isOpen()) {
                this.httpservice.handleRequest(this.conn, context);
            }
        } catch (ConnectionClosedException e) {
            if (!rl.isDown() ) { 
                logger.error("Client closed connection.",e);
            }
        } catch (IOException e) {
            if (!rl.isDown() ) { 
                logger.error("I/O error.",e);
            }
        } catch (HttpException e) {
            if (!rl.isDown() ) { 
                logger.error("Unrecoverable HTTP protocol violation.", e);
            }
        } catch (IllegalStateException e) { 
            if (!rl.isDown() ) { 
                logger.error("Unrecoverable HTTP protocol violation.", e);
            }
        } finally {
            try {
                this.conn.shutdown();
            } catch (IOException ignore) { }
        }
    }
}
