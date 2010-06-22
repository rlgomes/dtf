package com.yahoo.dtf.actions.basic;

import java.util.ArrayList;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.results.Result;
import com.yahoo.dtf.results.Results;
import com.yahoo.dtf.results.TestSuiteResults;
import com.yahoo.dtf.state.DTFState;

/**
 * @dtf.tag testsuite
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               The testsuite tag allows you to define testsuites under which
 *               all testscript tags are executed and monitored from the 
 *               testsuite's perspective. The testsuite can be instructed to 
 *               fail on the first error or to continueOnFailure by default, by
 *               continuing you can run through the whole testsuite and identify
 *               the testscases that are failing in a suite.
 *               </p>
 *               <p>
 *               Testsuite tag will generate results that can be recorded by 
 *               the {@dtf.link Result} tag this tag has the ability to record
 *               Testsuite and Testcase results to a different format that 
 *               can later be processed and identify exactly where all the 
 *               errors occured during testing.
 *               </p>
 *               <p>
 *               Aside from being easier to read the end reports generated from
 *               the Results file it is also more convenient for keeping history
 *               of test results in this manner.
 *               </p>
 *               
 * @dtf.tag.example 
 * <testsuite name="mytests" >
 *    <testscript uri="storage://INPUT/for.xml"/>
 *    <testscript uri="storage://INPUT/parallel.xml"/>
 *    <testscript uri="storage://INPUT/parallelloop.xml"/>
 *    <testscript uri="storage://INPUT/sequence.xml"/>
 * </testsuite>
 */
public class Testsuite extends Action {

    public final static String TESTSUITE_CTX = "dtf.testsuite.ctx";
    
    /**
     * @dtf.attr name
     * @dtf.attr.desc The name of the testsuite is used when recording test 
     *                results to the results tag. This will uniquely identify
     *                the test within other testsuite results.
     */
    private String name = null;
    
    /**
     * @dtf.attr continueOnFailure
     * @dtf.attr.desc If this attribute is set to true then the failure of any 
     *                testscript within it will not stop the execution of the
     *                testsuite. By default this attribute is set to false 
     *                because in most cases if a single test fails then the 
     *                rest of the testsuite can not execute and/or the current
     *                state of the test environment is important for debugging 
     *                the issue that may have been found.
     */
    private String continueOnFailure = "false";
  
    public Testsuite() {}
    
    public void execute() throws DTFException { 
        getLogger().info("Starting testsuite: " + getName());
        
        Result result = new Result(getName());
        result.start();
        result.setTestsuite();
        
        TestSuiteResults results = new TestSuiteResults(result);
        ArrayList<DTFException> dtfes = null;
        
        Results parent = getResults();
        DTFState state = getState();
        
        try { 
            Results resulter = new Results(results);
            state.setResults(resulter);
          
            registerGlobalContext(TESTSUITE_CTX, this);
            try { 
                executeChildren();
            } finally { 
                dtfes = (ArrayList<DTFException>) 
                             getGlobalContext(Testscript.TESTSCRIPT_FAILED_CTX);
                
                unRegisterGlobalContext(TESTSUITE_CTX);
            }
        } finally { 
            state.setResults(parent);
            if (dtfes != null) {
                result.stop();
                
                for (int i = 0; i < dtfes.size(); i++) { 
                    getLogger().error("Testscript failed.",dtfes.get(i));
                }
                
                result.setFailResult(dtfes.get(dtfes.size()-1));
                getResults().recordResult(result);
                throw dtfes.get(dtfes.size()-1);
            } else { 
                result.stop();
                result.setPassResult();
                getResults().recordResult(result);
            }
        }
    }
   
    public String getName() throws ParseException { return replaceProperties(name); }
    public void setName(String name) { this.name = name; }

    public boolean getContinueonfailure() throws ParseException { 
        return toBoolean("continueOnFailure",continueOnFailure);
    }
    
    public void setContinueOnFailure(String continueOnFailure) { 
        this.continueOnFailure = continueOnFailure; 
    } 
}
