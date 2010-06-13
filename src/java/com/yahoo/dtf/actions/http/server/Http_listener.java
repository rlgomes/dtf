package com.yahoo.dtf.actions.http.server;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag http_listener
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc This tag is used to setup an HTTP listener for the HTTP server
 *               created with the http_server tag. Without this the HTTP server
 *               will not respond to any requests. When using this tag you can
 *               easily identify how to handle each type of request and even 
 *               process each of the requests individually depending on the 
 *               requests headers/body/etc.
 *               <br/>
 *               Within the http_listener tag you have special properties that
 *               will identify the list of headers that were passed to this 
 *               listener along with the method name. Other attributes can be
 *               added later such as client IP, etc.  See event information 
 *               below for more information. For each method you'll have the
 *               same events you had if you were on the client side using the 
 *               HTTP tags available in DTF. So you'll have the 
 *               ${http.get.headerin.[headername]} and you have ${http.get.uri}.
 *               See all other HTTP tags for event information: 
 *               {@dtf.link Http_put},{@dtf.link Http_get},{@dtf.link Http_head},
 *               {@dtf.link Http_post},{@dtf.link Http_delete},
 *               {@dtf.link Http_request},etc.
 *               
 * @dtf.event http.[methodname]
 * @dtf.event.attr headerin
 * @dtf.event.attr.desc A comma separated list of the header names that were 
 *                      sent with the current HTTP request. You can then use 
 *                      these names to reference the 
 *                      ${http.[method_name].headerin.[headername]} properties.
 *               
 * @dtf.tag.example 
 * <http_server port="8082">
 *      <http_listener path="/echo-data" method="PUT">
 *           <log>received [${http.put.body}] in the HTTP body</log>
 *           <log>received headers [${http.headerin}]</log>
 *      </http_listener>
 * </http_server>
 *
 * @dtf.tag.example 
 * <http_server port="8083">
 *      <http_listener path="/write" method="PUT">
 *           <log>received a write operation</log>
 *      </http_listener>
 *      <http_listener path="/read" method="GET">
 *           <log>received a read operation</log>
 *      </http_listener>
 * </http_server>
 * 
 * @dtf.tag.example 
 * <http_server port="8080">
 *      <http_listener path="*" method="*">
 *      </http_listener>
 * </http_server>
 */
public class Http_listener extends Action {

    /**
     * @dtf.attr path
     * @dtf.attr.desc The URl path that will lead to this listener being called.
     */
    private String path = null;
    
    /**
     * @dtf.attr method
     * @dtf.attr.desc The method name for which this listener will actually be
     *                handling the HTTP request. All other method names will 
     *                result in an HTTP Bad Request response. In order to allow
     *                any HTTP method just use the name * in place of the method
     *                name.
     */
    private String method = null;

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
   
    
    @Override
    public void execute() throws DTFException {
        executeChildren();
    }

    public String getPath() throws ParseException { return replaceProperties(path); }
    public void setPath(String path) { this.path = path; }

    public String getMethod() throws ParseException { return replaceProperties(method); }
    public void setMethod(String method) { this.method = method; }

    public String getPerfrun() throws ParseException { 
        return replaceProperties(perfrun);
    }
    
    public boolean isPerfrun() throws ParseException { 
        return toBoolean("perfrun", perfrun);
    }
    
    public void setPerfrun(String perfrun) { 
        this.perfrun = perfrun;
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
