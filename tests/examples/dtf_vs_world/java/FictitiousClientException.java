
public class FictitiousClientException extends Exception {

    public FictitiousClientException(String message, Throwable t) { 
        super(message,t);
    }

    public FictitiousClientException(String message) { 
        super(message);
    }
}
