package com.yahoo.dtf.actions.selenium;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleniumException;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.selenium.server.SeleniumServerFactory;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag selenium
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               This is the root of all Selenium interactions that allow you to
 *               write your selenium tests using a very simple set of DTF XML 
 *               tags. There is also a formatter written for the FireFox 
 *               Selenium plug-in which allows you to easily record and export
 *               your interactions on any given website. For more information on
 *               the formatter please read about the {@dtf.link Selenium Formatter}.
 *               </p>
 *              
 *               <p>
 *               The selenium tags have been split up into a few command groups:
 *               </p>
 *                
 *               <h2>Base Commands</h2>
 *               {@dtf.link answerOnNextPrompt}
 *               {@dtf.link open}
 *               {@dtf.link openWindow}
 *               {@dtf.link runScript}
 *               {@dtf.link select}
 *               {@dtf.link selectFrame} 
 *               {@dtf.link selectWindow}
 *               {@dtf.link submit}
 *               {@dtf.link type}
 *               {@dtf.link waitForPageToLoad}
 *
 *               <h2>Browser Commands</h2>
 *               {@dtf.link goBack}
 *               {@dtf.link refresh}
 *               {@dtf.link windowMaximize}
 *
 *               <h2>Checkbox Commands</h2>
 *               {@dtf.link check}
 *               {@dtf.link uncheck}
 *
 *               <h2>Confirmation Commands</h2>
 *               {@dtf.link choosecancelonnextconfirmation}
 *               {@dtf.link chooseokonnextconfirmation}
 *
 *               <h2>Conditionals</h2>
 *               {@dtf.link isAlertPresent}
 *               {@dtf.link isChecked}
 *               {@dtf.link isConfirmationPresent}
 *               {@dtf.link isEditable}
 *               {@dtf.link isElementPresent}
 *               {@dtf.link isOrdered}
 *               {@dtf.link isSomethingSelected}
 *               {@dtf.link isTextPresent}
 *               {@dtf.link isVisible}
 *               
 *               <h2>Cookie Commands</h2>
 *               {@dtf.link createCookie}
 *               {@dtf.link deleteCookie}
 * 
 *               <h2>Keyboard Commands</h2>
 *               {@dtf.link altKeyDown}
 *               {@dtf.link altKeyUp}
 *               {@dtf.link controlKeyDown}
 *               {@dtf.link controlKeyUp}
 *               {@dtf.link keyDown}
 *               {@dtf.link keyUp}
 *               {@dtf.link metaKeyDown}
 *               {@dtf.link metaKeyUp}
 *               {@dtf.link shiftKeyDown}
 *               {@dtf.link shiftKeyUp}
 *               
 *               <h2>Mouse Commands</h2>
 *               {@dtf.link click}
 *               {@dtf.link clickAt}
 *               {@dtf.link doubleClick}
 *               {@dtf.link doubleClickAt}
 *               {@dtf.link dragAndDrop}
 *               {@dtf.link dragAndDropToObject}
 *               {@dtf.link mouseDown}
 *               {@dtf.link mouseDownAt}
 *               {@dtf.link mouseMove}
 *               {@dtf.link mouseMoveAt}
 *               {@dtf.link mouseOut}
 *               {@dtf.link mouseOver}
 *               {@dtf.link mouseUp}
 *               {@dtf.link mouseUpAt}
 *               
 *               <h2>Selection Commands</h2>
 *               {@dtf.link addSelection}
 *               {@dtf.link removeSelection}
 *               {@dtf.link removeAllSelections}
 *               
 *               <h2>Get State Commands</h2>
 *               {@dtf.link getAlert}
 *               {@dtf.link getAllButtons}
 *               {@dtf.link getAllFields}
 *               {@dtf.link getAllLinks}
 *               {@dtf.link getAllWindowIds}
 *               {@dtf.link getAllWindowNames}
 *               {@dtf.link getAllWindowTitles}
 *               {@dtf.link getAttribute}
 *               {@dtf.link getAttributeFromAllWindows}
 *               {@dtf.link getBodyText}
 *               {@dtf.link getConfirmation}
 *               {@dtf.link getCookie}
 *               {@dtf.link getCursorPosition}
 *               {@dtf.link getElementHeight}
 *               {@dtf.link getElementIndex}
 *               {@dtf.link getElementPositionLeft}
 *               {@dtf.link getElementPositionTop}
 *               {@dtf.link getElementWidth}
 *               {@dtf.link getEval}
 *               {@dtf.link getExpression}
 *               {@dtf.link getHtmlSource}
 *               {@dtf.link getLocation}
 *               {@dtf.link getMouseSpeed}
 *               {@dtf.link getPrompt}
 *               {@dtf.link getSelectedId}
 *               {@dtf.link getSelectedIds}
 *               {@dtf.link getSelectedIndex}
 *               {@dtf.link getSelectedLabel}
 *               {@dtf.link getSelectedLabels}
 *               {@dtf.link getSelectedValue}
 *               {@dtf.link getSelectedValues}
 *               {@dtf.link getSelectOptions}
 *               {@dtf.link getTable}
 *               {@dtf.link getText}
 *               {@dtf.link getTitle}
 *               {@dtf.link getValue}
 *               {@dtf.link getWhetherThisFrameMatchFrameExpression}
 *               {@dtf.link getWhetherThisWindowMatchWindowExpression}
 *               {@dtf.link getXpathCount}
 *
 *               <h2>Set State Commands</h2>
 *               {@dtf.link setContext}
 *               {@dtf.link setCursorPosition}
 *               {@dtf.link setExtensionJs}
 *               {@dtf.link setMouseSpeed}
 *               {@dtf.link setSpeed}
 *               {@dtf.link setTimeout}
 */
public class Selenium extends Action {
   
    /**
     * @dtf.attr host
     * @dtf.attr.desc the host address to use when starting the Selenium server.
     */
    private String host = null;
    
    /**
     * @dtf.attr port
     * @dtf.attr.desc  the host port to use when starting the Selenium server.
     */
    private String port = null;
    
    /**
     * @dtf.attr browser
     * @dtf.attr.desc 
     */
    private String browser = null;
    
    /**
     * @dtf.attr baseurl
     * @dtf.attr.desc
     */
    private String baseurl = null;
    
    /**
     * Returns the currently initialized selenium instance to be used by other
     * selenium tags when executing selenium commands.
     *  
     * @return
     * @throws DTFException
     */
    protected DefaultSelenium getSelenium() throws DTFException { 
        DefaultSelenium sel = (DefaultSelenium) getContext("dtf.selenium");
 
        if ( sel == null ) { 
            throw new DTFException("There is no selenium instance initialized.");
        }

        return sel;
    }
    
    @Override
    public void execute() throws DTFException {
        // start up server if its not already running
        SeleniumServerFactory.startServer(getPort());

        DefaultSelenium selenium = new DefaultSelenium(getHost(),
                                                       getPort(),
                                                       getBrowser(),
                                                       getBaseurl());

        selenium.start();
        registerContext("dtf.selenium", selenium);
        try {
            executeChildren();
        } catch (SeleniumException e) { 
            throw new DTFException("Selenium error.",e);
        } finally { 
            selenium.stop();
            unRegisterContext("dtf.selenium");
        }
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public String getHost() throws ParseException {
        return replaceProperties(host);
    }
    
    public void setPort(String port) {
        this.port = port;
    }
    
    public int getPort() throws ParseException {
        return toInt("port",port);
    }
    
    public void setBrowser(String browser) {
        this.browser = browser;
    }
    
    public String getBrowser() throws ParseException {
        return replaceProperties(browser);
    }
    
    public void setBaseurl(String baseurl) {
        this.baseurl = baseurl;
    }
    
    public String getBaseurl() throws ParseException {
        return replaceProperties(baseurl);
    }
}
