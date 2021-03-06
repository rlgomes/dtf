package com.yahoo.dtf.distribution;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import com.yahoo.dtf.actions.protocol.CleanUpHook;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.logger.DTFLogger;

/**
 * @dtf.feature Thread Management
 * @dtf.feature.group Tag Development
 * @dtf.feature.desc 
 * <p>
 * Thread management within DTF is important so that DTF can correctly release
 * agents whenever a DTFX (runner) disconnects during a test run. For this we 
 * have ThreadMgr class, which is able to register threads that can be
 * interrupted if an agent is no longer being used and then there is an 
 * underlying utlility that can be used to detect the interruption and stop 
 * iterating.
 * </p>
 * <p>
 * Remember that if you register a thread you should always unregister it when
 * it has finished executing for good practice. Here is a small code example of
 * how you should do such a thing in your own tag:
 * </p>
 * <pre>
 * Thread t = new Thread() { 
 *      public void run() { 
 *          System.out.println("Do your thing!");
 *      } 
 * };
 * 
 * t.start();
 * ThreadMgr.registerThread(t);
 * try { 
 *      // do something else 
 *      t.join();
 * } finally { 
 *      Threadmgr.unregisterThread(t);
 * }
 * </pre>
 * <p>
 * Very simple to use and it gaurantees that DTF can always clean up any of your
 * threads even if your code does something bad or just fails to correctly clean
 * up in certain instances. Any time that the Thread Manager has to clean up a 
 * thread because the agent is being released but the Thread is still registered
 * under the ThreadMgr it will log to the screen a message like so on the agent 
 * side:
 * </p>
 * <pre>
 * ThreadMgr       - Interrupted [Thread-27,Thread-28,Thread-29]
 * </pre>
 * <p>
 * Naming your threads according to your activity would make those thread names
 * more meaningful and allow you to better understand whree they were registered.
 * </p>
 *
 */
public class ThreadMgr implements CleanUpHook {
    
    private static DTFLogger _logger = DTFLogger.getLogger(ThreadMgr.class);
    
    private static HashMap<String, Thread> _threads = 
                                                  new HashMap<String, Thread>();
    
    public static void registerThread(Thread t) { 
        synchronized (_threads) { 
            _threads.put(t.getName(), t);
        }
    }
    
    public static void unregisterThread(Thread t) { 
        synchronized (_threads) { 
            _threads.remove(t.getName());
        }
    }
    
    public void cleanup() throws DTFException {
        synchronized (_threads) { 
            StringBuffer interrupted = new StringBuffer();
            Set<Entry<String,Thread>> entries = _threads.entrySet();
            for (Entry<String,Thread> entry : entries) {
                Thread thread = entry.getValue();
                thread.interrupt();
                interrupted.append(thread.getName() + ",");
            }
            
            if ( interrupted.length() != 0 )
                _logger.info("Interrupted [" + interrupted.toString() + "]");
        }
    }
}
