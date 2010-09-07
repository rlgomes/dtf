package com.yahoo.dtf.actions.selenium.commands.base;

import com.yahoo.dtf.actions.selenium.Selenium;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag runScript
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               This selenium tag allows you to inject javascript into the 
 *               currently focused window. The javascript is instantly executed
 *               within the page and can be used to test javascript elements
 *               that you would otherwise not have a way of testing. In the 
 *               example we're just demonstrating how you can use this command 
 *               to manipulate elements on the page easily.
 *               </p>
 * 
 * @dtf.tag.example 
 * <selenium baseurl="http://www.google.com" browser="*firefox">
 *     <property name="tornadoscript">
 *     javascript:R=0; x1=.1; y1=.05; x2=.25; y2=.24; x3=1.6; y3=.24; x4=300; y4=200; x5=300; y5=200; DI=document.getElementsByTagName("img"); DIL=DI.length; function A(){for(i=0; i-DIL; i++){DIS=DI[ i ].style; DIS.position='absolute'; DIS.left=(Math.sin(R*x1+i*x2+x3)*x4+x5)+"px"; DIS.top=(Math.cos(R*y1+i*y2+y3)*y4+y5)+"px"}R++}setInterval('A()',5); void(0);
 *     </property> 
 *     <runScript script="${tornadoscript}"/>
 *     <sleep time="2s"/>
 * </selenium>
 */
public class Runscript extends Selenium {
   
    /**
     * @dtf.attr script
     * @dtf.attr.desc the javascript to execute in the browser 
     */
    private String script = null;

    @Override
    public void execute() throws DTFException {
        getSelenium().runScript(getScript());
    }
    
    public String getScript() throws ParseException {
        return replaceProperties(script);
    }
    
    public void setScript(String script) {
        this.script = script;
    }
}
