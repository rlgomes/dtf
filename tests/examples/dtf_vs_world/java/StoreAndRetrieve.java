import junit.framework.Assert;

import org.junit.Test;

/**
 * This is a test that is used in a comparison between writing tests directly
 * in Java to writing tests in DTF. The full comparison can be read here: 
 * 
 * https://github.com/rlgomes/dtf/wiki/Dtf-vs-the-world
 * 
 * @author rlgomes
 */
public class StoreAndRetrieve {

    @Test
    public void storeAndRetrieveTest() throws FictitiousClientException { 
        FictitiousClient client = new FictitiousClient("localhost", 44444);
        
        client.store("object1", "sometestdata");
        String data = client.retrieve("object1");
        Assert.assertEquals(data, "sometestdata");
    }
}
