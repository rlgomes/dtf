package com.yahoo.dtf.actions.images;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

import com.yahoo.dtf.actions.util.CDATA;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.recorder.Event;

/**
 * @dtf.tag read_image
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes 
 * 
 * @dtf.tag.desc This tag is able to read various formats of image files and 
 *               make the data as well as some attributes (ie height, width, etc)
 *               available at runtime for testing purposes.
 *               
 * @dtf.event [read.image|name_of_event]
 * @dtf.event.attr height
 * @dtf.event.attr.desc the number of pixels in height of the image read 
 *
 * @dtf.event [read.image|name_of_event]
 * @dtf.event.attr width
 * @dtf.event.attr.desc the number of pixels in width of the image read 
 *
 * @dtf.event [read.image|name_of_event]
 * @dtf.event.attr data
 * @dtf.event.attr.desc the data that represents the images RGBA values with 8
 *                      bits of precision per color component. This value is then
 *                      encoded in Base64 to be easily represented as a string.
 *                      
 * @dtf.tag.example
 * <read_image event="blue.jpg" uri="storage://INPUT/blue.jpg"/>

 * @dtf.tag.example
 * <read_image uri="storage://INPUT/image.jpg"/>
 * 
 */
public class Read_image extends CDATA {
    
    public final static String READ_IMAGE_EVENT = "read.image";
    
    /**
     * @dtf.attr uri
     * @dtf.attr.desc The uri that points to the image file to read. The supported
     *                file types are registered with the java IIORegistry and 
     *                you can find more information <a href="http://download.oracle.com/docs/cd/E17476_01/javase/1.4.2/docs/api/javax/imageio/spi/IIORegistry.html">here</a>
     */
    private String uri = null;
   
    /**
     * @dtf.attr event
     * @dtf.attr.desc The event name to be used to record the read image data 
     *                and attributes. The default event name is read.image. 
     */
    private String event = null;
   
    public void execute() throws DTFException {
        URI uri = getUri();
        InputStream is = getStorageFactory().getInputStream(uri);
        try {
            String name = getEvent();
            if ( name == null ) name = READ_IMAGE_EVENT;
            
            Event event = new Event(name);
           
            event.start();
            BufferedImage bi = ImageIO.read(is);
            event.stop();
            
            if ( bi == null )
                throw new DTFException("No reader for image [" + uri + "]");
            
            int width = bi.getWidth();
            int height = bi.getHeight();
           
            event.addAttribute("width", width);
            event.addAttribute("height", height);
            
            int[] rgbs = new int[width * height];
            bi.getRGB(0, 0, width, height, rgbs, 0, width);
            byte[] bytes = new byte[rgbs.length*4];
         
            int i = 0;
            for (int rgb : rgbs) { 
                Color c = new Color(rgb);
                bytes[i++] = (byte) c.getRed();
                bytes[i++] = (byte) c.getGreen();
                bytes[i++] = (byte) c.getBlue();
                bytes[i++] = (byte) c.getAlpha();
            }
            
            bytes = Base64.encodeBase64(bytes);
            String data = new String(bytes);
            getConfig().setProperty(name + ".data", data);
            getRecorder().record(event);
        } catch (IOException e) {
            throw new DTFException("Error reading image [" + uri + "]",e);
        }
    }

    public void setUri(String uri) { this.uri = uri; }
    public URI getUri() throws ParseException { return parseURI(uri); }
    
    public void setEvent(String event) { this.event = event; } 
    public String getEvent() throws DTFException { return replaceProperties(event); }
}
