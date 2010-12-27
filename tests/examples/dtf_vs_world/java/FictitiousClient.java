
import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PutMethod;

/**
 * This is a test client that is used to do a comparison between writing tests 
 * in Java vs DTF. The full comparison can be read here: 
 * 
 * https://github.com/rlgomes/dtf/wiki/Dtf-vs-the-world
 * 
 * @author rlgomes
 */
public class FictitiousClient {
   
    private String putURL = null;
    private String getURL = null;
    private HttpClient _client = null;
    
    public FictitiousClient(String hostname, int port) { 
        _client = new HttpClient();
       
        putURL = "http://" + hostname + ":" + port + "/store/";
        getURL = "http://" + hostname + ":" + port + "/retrieve/";
    }

    public void store(String name, String data)
           throws FictitiousClientException { 
        PutMethod put = new PutMethod(putURL + name);
        
        try { 
            ByteArrayInputStream bais = new ByteArrayInputStream(data.getBytes());
            InputStreamRequestEntity isre = new InputStreamRequestEntity(bais);
            put.setRequestEntity(isre);
            int status = _client.executeMethod(put);
           
            if ( status != HttpStatus.SC_OK ) {
                throw new FictitiousClientException(
                       "Error execution retrieve, got status [" + status + "]");
            }
        } catch (HttpException e ) { 
            throw new FictitiousClientException("Error execution store.",e);
        } catch (IOException e) {
            throw new FictitiousClientException("Error execution store.",e);
        }
    }
    
    public String retrieve(String name) throws FictitiousClientException {
        GetMethod get = new GetMethod(getURL + name);
        try { 
	        int status = _client.executeMethod(get);
	       
	        if ( status != HttpStatus.SC_OK ) {
	            throw new FictitiousClientException(
	                   "Error execution retrieve, got status [" + status + "]");
	        }
	        
	        return get.getResponseBodyAsString();
        } catch (HttpException e ) { 
            throw new FictitiousClientException("Error execution retrieve.",e);
        } catch (IOException e) {
            throw new FictitiousClientException("Error execution retrieve.",e);
        }
    }
}
