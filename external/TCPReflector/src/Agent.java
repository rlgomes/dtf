import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.Integer;


class Agent implements Runnable
{
   private InputStream in           = null;
   private OutputStream out         = null;
   private AgentMonitor am          = null;
   private byte buffer[]            = null;
   private static int BUFFER_SIZE   = 4096;
   private String name              = null;
   private boolean echo             = false;
      	 // Format the current time in a nice iso 8601 format.
   private static SimpleDateFormat formatter = 
                          new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss.S");
         
   public Agent(
      InputStream in,
      OutputStream out,
      AgentMonitor am,
      String name,
      boolean echo
   )
   {
      this.in   = in;
      this.out  = out;
      this.am   = am;
      this.name = new String(name);
      this.echo = echo;

      buffer = new byte[BUFFER_SIZE];

      Thread t = new Thread(this);
      t.start();
   }

   public void run()
   {
      try 
      {
         int bytesRead = 0;

         while(true)
         {
            if((bytesRead = in.read(buffer, 0, BUFFER_SIZE)) == -1)
               break;

            if(echo)
               doEcho(buffer, bytesRead);

            out.write(buffer, 0, bytesRead);
         }
      }
      catch(IOException e) {}
      am.agentHasDied(this);
   }

   private void doEcho(byte buffer[], int nBytes)
   {
      synchronized(System.out)
      {
         Date currentTime = new Date();
         System.out.println("[" + name + ", (" + nBytes + " bytes) " + formatter.format(currentTime) +"]");
         StringBuffer sb = new StringBuffer(nBytes);		// formatted string
         StringBuffer pb = new StringBuffer(4 * nBytes);	// formatted binary string
         String hb = new String();			// hex buffer string
         String tb = new String();			// text buffer string
         
         int j = 0;					// line position counter 
         int c = 0;					// byte counter
         float k = 0;					// binary character counter 

         for(int i = 0; i < nBytes; i ++)
         {
            int value = (buffer[i] & 0xFF);		// 32 bit Unicode to 16 bit ASCII
            if(value == '\r' || value == '\n' || value == '\t' ||
               (value >= ' ' && value <= '~')) {
               sb.append((char)value);			// text character
            }
            else {
               sb.append("[" + hexPad(Integer.toHexString(value), 2) + "]");	// binary character
               k++;
            }

            if (value >= ' ' && value <= '~') {
            	tb = tb + (char)value;			// "printable" character
            } else {
            	tb = tb + ".";				// non-printable 
	    }

	    hb = hb + hexPad(Integer.toHexString(value), 2);
            if (j == 3 || j == 7 || j == 11) {		// for readability, space every 4 chars
            	hb = hb + " ";
            	tb = tb + " ";
            }            	
            
            if (j == 15) {				// 16 characters per line
            	pb.append(hexPad(Integer.toHexString(c), 4) +":  " + hb + "    " + tb + "\n");
            	j = 0;
            	c += 16;
            	hb = "";
            	tb = "";
            } else {
            	j++;
            }
         }
         for (int i = hb.length(); i < 35; i++) {
         	hb = hb + " ";				// pad to length of other lines
         }
         pb.append(hexPad(Integer.toHexString(c), 4) +":  " + hb + "    " + tb + "\n");
         
         if ((k/nBytes) < .05) {			// less than 5% binary?
         	System.out.println(sb.toString());	// print as text 
         } else {
         	System.out.println(pb.toString());	// print as hex dump
         }
      }
   }


	/* prepend 0's to a hex string
	 */
	private String hexPad(String a, int length)
	{
		for (int j = a.length(); j < length; j++) {
			a = "0" + a;
		}
	return a;
	}
}