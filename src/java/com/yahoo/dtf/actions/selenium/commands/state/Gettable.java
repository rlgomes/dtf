package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag getTable
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Gets the text from a cell of a table. The cellAddress syntax 
 *               tableLocator.row.column, where row and column start at 0.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://someplace.com">
 *      <open url="/"/>
 *      <getTable tableCellAddress="foo.1.4" property="foo.1.4.value"/>
 *  </selenium>
 */
public class Gettable extends SeleniumGetStateTag {
   
    /**
     * @dtf.attr tableCellAddress
     * @dtf.attr.desc a cell address, e.g. "foo.1.4".
     */
    private String tableCellAddress = null;
    
    @Override
    public Object getValue() throws DTFException {
        return getSelenium().getTable(getTableCellAddress());
    }
    
    public String getTableCellAddress() throws ParseException {
        return replaceProperties(tableCellAddress);
    }
    
    public void setTableCellAddress(String tableCellAddress) {
        this.tableCellAddress = tableCellAddress;
    }
}
