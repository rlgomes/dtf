package com.yahoo.dtf.actions.plugin;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Structure;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.recorder.Event;

public class Jna_gettimeofday extends Action {

    public interface CLib extends Library {
        CLib INSTANCE = (CLib) Native.
               loadLibrary((Platform.isWindows() ? "msvcrt" : "c"), CLib.class);

        // int gettimeofday(struct timeval *tv, struct timezone *tz);
        int gettimeofday(TIMEVAL tv, TIMEZONE tz);
    }
    
    /*
     *  struct timeval {
     *      time_t      tv_sec; 
     *      suseconds_t tv_usec;  
     *  };
     */
    public static class TIMEVAL extends Structure {
        public int tv_sec;
        public int tv_usec;
    }

    /*
     *  struct timezone {
     *      int tz_minuteswest;
     *      int tz_dsttime;     
     *  };
     */
    public static class TIMEZONE extends Structure {
        public int tz_minuteswest;
        public int tz_dsttime;
    }
   
    @Override
    public void execute() throws DTFException {
        Event event = new Event("jna_gettimeofday");
        
        TIMEVAL tv = new TIMEVAL();
        event.start();
        int rc = CLib.INSTANCE.gettimeofday(tv, null);
        event.stop();
       
        if ( rc != 0 ) 
            throw new DTFException("Error running gettimeofday got [" + 
                                   rc + "] return code.");
        
        event.addAttribute("seconds", tv.tv_sec);
        event.addAttribute("nanoseconds", tv.tv_usec);
        getRecorder().record(event);
    }
}
