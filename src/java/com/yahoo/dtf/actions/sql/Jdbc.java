package com.yahoo.dtf.actions.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.recorder.Event;

/**
 * @dtf.tag jdbc
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc This tag can be used to talk to a database over JDBC, it 
 *               currently allows you to do so for MYSQL databases using their
 *               3.0 JDBC drivers. If you want to use other drivers for a different
 *               database make sure that those jars are in the classpath and 
 *               that you specify the driver class and name to the tag before
 *               attempting to connect to your database.
 *               </br> 
 *               </br> 
 *               Currently this tag has only be used to retrieve values and may
 *               not work as expected to issue create,insert or delete statements.
 *               In order to support those operations we may need to make some 
 *               fixes.
 *               
 * @dtf.event jdbc
 * @dtf.event.attr affectedrows
 * @dtf.event.attr.desc for update and delete returns the number of affected
 *                      rows during that operation. For a select statement this 
 *                      attribute holds the number of return results. Be aware
 *                      that the results are returned in the previously thrown
 *                      events to this last jdbc event. So you should use a
 *                      record tag inorder to record all previously thrown 
 *                      events. In other words when using a select you can get
 *                      N events thrown being the last one this jdbc event.
 *                      
 * @dtf.event jdbc 
 * @dtf.event.attr columns
 * @dtf.event.attr.desc <b>This only applies to each row returned in a select 
 *                      statement.</b> This attribute would contain a comma 
 *                      separated list of the names of the columns returned 
 *                      during a select statement.
 *
 * @dtf.event jdbc
 * @dtf.event.attr [column name]
 * @dtf.event.attr.desc <b>This only applies to each row returned in a select 
 *                      statement.</b> This attribute would contain each column 
 *                      mentioned in columns attribute will have its own 
 *                      attribute that can easily be referenced like so:
 *                      ${jdbc.column_name}. This allows you to check each of 
 *                      the fields of the returned results from your select
 *                      statement.
 *
 * @dtf.tag.example 
 * <jdbc hostname="localhost"
 *       port="3306"
 *       database="ydht"
 *       user="root"
 *       password=""
 *       sql="DROP TABLE test"/>
 *
 * @dtf.tag.example 
 * <jdbc driverclass="oracle.jdbc.driver.OracleDriver"
 *       url="jdbc:oracle:thin:asown/asown@masora104v.mail.re3.yahoo.com:1521/MASASV2"
 *       sql="SELECT * FROM refcount" />
 * 
 */
public class Jdbc extends Action {
   
    private static final String JDBC_EVENT = "jdbc";
 
    /**
     * @dtf.attr hostname
     * @dtf.attr.desc the hostname of the machine to establish the JDBC connection.
     */
    private String hostname = null;

    /**
     * @dtf.attr port
     * @dtf.attr.desc the port of the machine to establish the JDBC connection.
     */
    private String port = null;
    
    /**
     * @dtf.attr user
     * @dtf.attr.desc the user name to use when establishing the JDBC connection.
     */
    private String user = null;

    /**
     * @dtf.attr password
     * @dtf.attr.desc the password name to use when establishing the JDBC connection.
     */
    private String password = null;
    
    /**
     * @dtf.attr database
     * @dtf.attr.desc the database to talk to.
     */
    private String database = null;

    /**
     * @dtf.attr driverclass
     * @dtf.attr.desc the driver class to use, i.e. com.mysql.jdbc.Driver
     */
    private String driverclass = null;

    /**
     * @dtf.attr drivername
     * @dtf.attr.desc the driver name to use in the JDBC connection. 
     */
    private String drivername = null;
    
    /**
     * @dtf.attr encoding
     * @dtf.attr.desc charcter encoding to use on the JDBC connection properties.
     *                Deafult set to UTF-8.
     */
    private String encoding = null;
    
    /**
     * @dtf.attr sql
     * @dtf.attr.desc The SQL statement to issue on the specified JDBC connection.
     *                Executing read or write operations has a slightly different
     *                treatment internally but for all purposes the events 
     *                returned are the same. 
     *                
     */
    private String sql = null;
    
    /**
     * 
     * @dtf.attr url
     * @dtf.attr.desc The url attribute is preferred when your JDBC driver may 
     *                require a url that isn't in the format:
     *                <br/>
     *                jdbc:${drivername}://${hostname}:${port}/${database}?user=${user}&password=${password}&characterEncoding=${encoding}
     *                <br/>
     *                By using the url you can override all of the previous 
     *                construction of the URL and be able to construct one for 
     *                the specific JDBC driver being used. This is necessary for
     *                oracle and some other databases that have their own format.
     */
    private String url = null;
   
    public void execute() throws DTFException {
        try { 
            Class.forName(getDriverclass());
        } catch (ClassNotFoundException e) { 
            throw new DTFException("Unable to load driver [" + 
                                   getDriverclass() + "]", e);
        }
      
        Connection con = null;
        Statement stmt = null;
        try { 
            String jdbc_url;
            if (getUrl() != null) {
                /* User passed in a complete URL */
                jdbc_url = getUrl();
            } else {
                /* If user did not specify a complete JDBC URL in the tag,
                   we'll attempt to build one from the other attributes */
                jdbc_url = "jdbc:" + getDrivername() + "://" + getHostname() + 
                                   ":" + getPort() + "/" + getDatabase() + 
                                   "?user=" + getUser() + 
                                   "&password=" + getPassword() + 
                                   "&characterEncoding=" + getEncoding();
            }
	        con = DriverManager.getConnection(jdbc_url);

	        // Get a Statement object
	        stmt = con.createStatement();
	        String sql = getSql().trim().toLowerCase();
	      
	        if ( sql.startsWith("select") ) { 
		        ResultSet results = stmt.executeQuery(sql);
	            ResultSetMetaData md = results.getMetaData();
		        int columnCnt = md.getColumnCount();
	
		        StringBuffer columns = new StringBuffer();
	            for (int i = 1; i <= columnCnt; i ++) { 
	                columns.append(md.getColumnLabel(i) + (i >= columnCnt ? "" : "," ));
	            }
	            String columnsStr = columns.toString();
	           
		        int count = 0;
		        while (results.next()) { 
		            count++;
		            Event event = new Event(JDBC_EVENT);
		            event.start();
		            
		            for (int i = 1; i <= columnCnt; i ++) { 
		                String label = md.getColumnLabel(i);
		                String value = results.getObject(i).toString();
		                event.addAttribute(label, value);
		            }
		            event.stop();
		            event.addAttribute("succeeded", "true");
		            event.addAttribute("columns", columnsStr);
		            getRecorder().record(event);
		        }
	           
	            Event event = new Event(JDBC_EVENT);
	            event.start();
	            event.stop();
	            
	            if ( count == 0 )
	                event.addAttribute("succeeded", "false");
	            else 
	                event.addAttribute("succeeded", "true");
	                
	            event.addAttribute("columns", columnsStr);
	            event.addAttribute("affectedrows", count);
	            getRecorder().record(event);
	        } else {
	            Event event = new Event(JDBC_EVENT);
		        event.start();
	            int count =  stmt.executeUpdate(sql);
		        event.stop();
		        
	            event.addAttribute("succeeded", "true");
		        event.addAttribute("columns", "");
		        event.addAttribute("affectedrows", count);
		        getRecorder().record(event);
	        }
        } catch (SQLException e) { 
            throw new DTFException("Unable to query database.",e);
        } finally { 
            if ( con != null ) {
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new DTFException("Unable to close connection to database.",e);
                }
            }
            
            if ( stmt != null ) { 
                try { 
                    stmt.close();
                } catch (SQLException e) {
                    throw new DTFException("Unable to close statement in database.",e);
                }
            }
        }
    }

    public String getHostname() throws ParseException { return replaceProperties(hostname); }
    public void setHostname(String hostname) { this.hostname = hostname; }

    public String getPort() throws ParseException { return replaceProperties(port); }
    public void setPort(String port) { this.port = port; }

    public String getUser() throws ParseException { return replaceProperties(user); }
    public void setUser(String user) { this.user = user; }

    public String getPassword() throws ParseException { return replaceProperties(password); }
    public void setPassword(String password) { this.password = password; }

    public String getDatabase() throws ParseException { return replaceProperties(database); }
    public void setDatabase(String database) { this.database = database; }

    public String getDriverclass() throws ParseException { return replaceProperties(driverclass); }
    public void setDriverclass(String driverclass) { this.driverclass = driverclass; }

    public String getDrivername() throws ParseException { return replaceProperties(drivername); }
    public void setDrivername(String drivername) { this.drivername = drivername; }

    public String getEncoding() throws ParseException { return replaceProperties(encoding); }
    public void setEncoding(String encoding) { this.encoding = encoding; }

    public String getSql() throws ParseException { return replaceProperties(sql); }
    public void setSql(String sql) { this.sql = sql; } 

    public String getUrl() throws ParseException { return replaceProperties(url); }
    public void setUrl(String url) { this.url = url; } 
}
