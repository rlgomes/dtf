package com.yahoo.dtf.actions.http;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.recorder.Event;
import com.yahoo.dtf.util.TimeUtil;

public abstract class HttpBase extends Action {

    public static final String HTTP_POST_EVENT      = "http.post";
    public static final String HTTP_GET_EVENT       = "http.get";
    public static final String HTTP_PUT_EVENT       = "http.put";
    public static final String HTTP_DELETE_EVENT    = "http.delete";
    public static final String HTTP_HEAD_EVENT      = "http.head";
   
    // event attribute names
    public static final String HTTP_EVENT_URI       = "uri";
    
    public static final String HTTP_EVENT_DATA      = "data";
    public static final String HTTP_EVENT_DATA_HASH = "datahash";
    public static final String HTTP_EVENT_DATA_SIZE = "datasize";
 
    public static final String HTTP_EVENT_HASH_ALGO = "hashalgo";
    
    public static final String HTTP_EVENT_BODY      = "body";
    public static final String HTTP_EVENT_BODY_HASH = "bodyhash";
    public static final String HTTP_EVENT_BODY_SIZE = "bodysize";
    
    public static final String HTTP_EVENT_STATUS    = "status";
    public static final String HTTP_EVENT_STATUSMSG = "statusmsg";
    
    public static final String HTTP_EVENT_HEADER_IN  = "headerin";
    public static final String HTTP_EVENT_HEADER_OUT = "headerout";

    public static final String HTTP_EVENT_COOKIE_IN  = "cookiein";
    public static final String HTTP_EVENT_COOKIE_OUT = "cookieout";

    /**
     * @dtf.attr.name uri
     * @dtf.attr.desc the uri is the exact hostname, port and path to where the
     *                following http request should be sent. The format looks 
     *                like so: http://server:port/path
     */
    private String uri = null;
   
    /**
     * @dtf.attr.name acceptredirects
     * @dtf.attr.desc this attribute will dictate if the http client should 
     *                accept redirects. When set to 'false' if the server requests
     *                an http redirect then the client will fail because it was
     *                set to not follow redirects.
     *                <b>Redirects are only automatically followed for GET and 
     *                   HEAD methods, the HTTP spec says these are simply 
     *                   followed while other methods can't just be re-issued
     *                   to a subsequent URL.</b>
     *                <br/>
     *                By default the redirects are accepted since most http 
     *                servers use this feature to do some form of load balancing
     *                or to recover from temporary service inavailability.
     */
    private String acceptredirects = "false";
    
    /**
     * @dtf.attr.name perfrun
     * @dtf.attr.desc the perfrun attribute controls the amount of data that is
     *                recorded about each HTTP request. When set to 'true' the 
     *                HTTP request will only log the start and stop timestamp
     *                for the request as well as the status code and message. 
     *                <br/><br/>
     *                The data sent and the data received through the body 
     *                response is not going to be recorded unless the perfrun 
     *                attribute is set to false.
     *                <br/><br/>
     *                <b>Additional attributes recorded when perfrun is set 
     *                   to true:</b>
     *                <ul>
     *                  <li>bodyhash</li>
     *                  <li>datahash</li>
     *                </ul>
     *                
     *                Currently the bodyhash and datahash are recorded instead
     *                of the contents of the body or data being used by the 
     *                current http request. This performs really well and still
     *                allows the test case writer to validate the data 
     *                correctness by comparing hash values.
     *                
     *                Note: you can disable hash calculation all together for 
     *                performance testing by using the hash attribute and 
     *                setting that to 'none'.
     */
    private String perfrun = "false";
    
    /**
     * @dtf.attr.name onFailure
     * @dtf.attr.desc this attribute maybe used to control the behavior of when
     *                operations return an http status code != 200. When set to 
     *                'continue' even if an operation fails the test will continue
     *                to execute and will just log the request has having a 
     *                status code != 200. This is useful when running performance
     *                tests or long running tests that may hit timeouts or other
     *                acceptable errors that we later need to triage. 
     *                <br/>
     *                When set to 'fail' the http request will throw an exception
     *                if the request returns with an http status code != 200. 
     *                This is useful in a scenario where we dont' have a huge 
     *                amount of load pointed at the http server being tested and
     *                we know that a failure at this point would just be 
     *                unacceptable.
     */
    private String onFailure = "continue";
   
    /**
     * @dtf.attr connecttimeout
     * @dtf.attr.desc the connect timeout for this http request. The default is
     *                5 seconds. The value of this attribute can be any of the 
     *                following exampels 5s - 5 seconds, 5m - 5 minutes. etc.
     */
    private String connecttimeout = "5000";

    /**
     * @dtf.attr hash 
     * @dtf.attr.desc specifies the hash algorithm to use, by default sha1.
     *                Can be set to md5, none, etc. 
     *                
     *                The hashing algorithm used has a minimal effect on the 
     *                amount of operations per second you can do for very small
     *                objects but has some impact on the through put that you 
     *                can do per single stream on PUT,POST activities. So be 
     *                aware when designing performance tests that disabling
     *                SHA1 may be a needed option when getting official 
     *                performance numbers.
     */
    private String hash = null;

    /**
     * @dtf.attr bandwidth
     * @dtf.attr.desc This attribute will limit the bandwidth when sending or 
     *                receiving data in the body of the HTTP request or 
     *                response. This means the headers will still be streamed
     *                at network speed but the body of the request/response will
     *                only have the bandwidth specified by this attribute. The 
     *                bandwidth value can be defined with the follow suffixes: 
     *                <table border="1">
     *                  <tr>
     *                      <th>Value</th> 
     *                      <th>Description</th> 
     *                  </tr>
     *                  <tr>
     *                      <td>b</td>
     *                      <td>bit</td>
     *                  </tr>
     *                  <tr>
     *                      <td>Kb</td>
     *                      <td>Kilobit</td>
     *                  </tr>
     *                  <tr>
     *                      <td>Mb</td>
     *                      <td>Megabit</td>
     *                  </tr>
     *                  <tr>
     *                      <td>Gb</td>
     *                      <td>Gigabit</td>
     *                  </tr>
     *                  <tr>
     *                      <td>B</td>
     *                      <td>Byte</td>
     *                  </tr>
     *                  <tr>
     *                      <td>KB</td>
     *                      <td>KiloByte</td>
     *                  </tr>
     *                  <tr>
     *                      <td>MB</td>
     *                      <td>MegaByte</td>
     *                  </tr>
     *                  <tr>
     *                      <td>GB</td>
     *                      <td>GigaByte</td>
     *                  </tr>
     *                </table>
     *                
     */
    private String bandwidth = null;
   
    private static HttpOp op = null;
    
    protected HttpBase() {       
        synchronized(this) { 
            if ( op == null ) { 
		        op = new ApacheHttpOp();
		        op.init();
            }
        }
    }
  
    /**
     * 
     * @return
     * @throws DTFException
     */
    public abstract Event executeOp() throws DTFException;
    
    @Override
    final public void execute() throws DTFException {
        getRecorder().record(executeOp());
    }
    
    public HttpOp getOp() { 
        return op;
    }
   
    public static void releaseConnections() { 
        if ( op != null )
            op.shutdown();
    }
   
    @Deprecated
    public String getURI() throws ParseException {
        return replaceProperties(uri);
    }
    
    public String getUri() throws ParseException {
        return replaceProperties(uri);
    }

    public void setURI(String uri) {
        this.uri = uri;
    }
    
    public String getPerfrun() throws ParseException { 
        return replaceProperties(perfrun);
    }
    
    public boolean isPerfrun() throws ParseException { 
        return toBoolean("perfrun", perfrun);
    }
    
    public void setPerfrun(String perfrun) { 
        this.perfrun = perfrun;
    }

    public boolean getAcceptredirects() throws ParseException { 
        return toBoolean("acceptredirects",acceptredirects);
    }

    public void setAcceptredirects(String acceptredirects) { 
        this.acceptredirects = acceptredirects;
    }
    
    public String getOnfailure() throws ParseException { 
        return replaceProperties(onFailure);
    }
    
    public void setOnfailure(String onFailure) { this.onFailure = onFailure; }
    public boolean continueOnFailure() throws ParseException {
        return getOnfailure().equals("continue");
    }
    
    public void setConnectTimeout(String connecttimeout) { 
        this.connecttimeout = connecttimeout;
    }
    public Integer getConnecttimeout() throws ParseException { 
        return TimeUtil.parseTimeToInt("connecttimeout", connecttimeout);
    }
    
    public void setHash(String hash) { this.hash = hash; } 
    public String getHash() throws ParseException {
        return replaceProperties(hash);
    }
    
    public String getBandwidth() throws ParseException {
        return replaceProperties(bandwidth);
    }
    public void setBandwidth(String bandwidth) { this.bandwidth = bandwidth; }
}
