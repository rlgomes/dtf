package com.yahoo.dtf.actions.http.server;

import java.util.ArrayList;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.http.Entity;
import com.yahoo.dtf.actions.http.Header;
import com.yahoo.dtf.actions.http.Headergroup;
import com.yahoo.dtf.actions.http.cookies.Cookie;
import com.yahoo.dtf.actions.http.cookies.Cookiegroup;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag http_response
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc This tag is used to define the HTTP response sent back from an
 *               {@dtf.link Http_listener}, if you don't use this tag then the
 *               HTTP response is either 200 OK or 500 with some exception 
 *               message being sent as the response message to the client. Now
 *               if you choose to use this tag you can define any HTTP code 
 *               (even something outside of the 1xx-5xx range, which could be 
 *               used by your application for identifying certain scenarios). 
 *               With this response you can also send back headers, cookies and
 *               even an entity (which is a stream of data) to the HTTP client.
 *               
 * @dtf.tag.example 
 * <http_response status="400" message="Something is wrong">
 *     <header name="result" value="false"/>
 * </http_response>
 *
 * @dtf.tag.example 
 * <http_response status="200" message="OK!">
 *     <header name="header1" value="value1"/>
 *     <entity>${dtf.stream(random,1024,1234)}</entity>
 * </http_response>
 */
public class Http_response extends Action {
    
    public final static String  HTTP_RESPONSE_CODE = "dtf.http.response.code";
    public final static String  HTTP_RESPONSE_MSG = "dtf.http.response.message";

    public final static String  HTTP_RESPONSE_HEADERS = "dtf.http.response.headers";
    public final static String  HTTP_RESPONSE_COOKIES = "dtf.http.response.cookies";

    public final static String  HTTP_RESPONSE_ENTITY = "dtf.http.response.entity";

    /**
     * @dtf.attr status
     * @dtf.attr.desc The status code to set in the HTTP response.
     */
    private String status = null;
   
    /**
     * @dtf.attr message
     * @dtf.attr.desc The message to set in the HTTP response.
     */
    private String message = null;
    
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
        int status = getStatus();
        registerContext(HTTP_RESPONSE_CODE, status);
        String message = getMessage();
        
        if ( message != null )
            registerContext(HTTP_RESPONSE_MSG, message);

        ArrayList<Header> headers = findActions(Header.class);
        ArrayList<Headergroup> hgroups = findActions(Headergroup.class);
        for (int i = 0; i < hgroups.size(); i++) { 
            Headergroup hgroup = hgroups.get(i);
            ArrayList<Header> aux = hgroup.findActions(Header.class);
            headers.addAll(aux);
        }
        registerContext(HTTP_RESPONSE_HEADERS, headers);
        
        ArrayList<Cookie> cookies = findActions(Cookie.class);
        ArrayList<Cookiegroup> cgroups = findActions(Cookiegroup.class);
        for (int i = 0; i < cgroups.size(); i++) { 
            Cookiegroup cgroup = cgroups.get(i);
            ArrayList<Cookie> aux = cgroup.findActions(Cookie.class);
            cookies.addAll(aux);
        }
        registerContext(HTTP_RESPONSE_COOKIES, cookies);
        
        Entity entity = (Entity) findFirstAction(Entity.class);
        
        if ( entity != null ) 
            registerContext(HTTP_RESPONSE_ENTITY, entity);
    }

    public Integer getStatus() throws ParseException { return toInt("status",status); }
    public void setStatus(String status) { this.status = status; }
    
    public String getMessage() throws ParseException { return replaceProperties(message); }
    public void setMessage(String message) { this.message = message; }

    public String getBandwidth() throws ParseException {
        return replaceProperties(bandwidth);
    }
    public void setBandwidth(String bandwidth) { this.bandwidth = bandwidth; }
}
