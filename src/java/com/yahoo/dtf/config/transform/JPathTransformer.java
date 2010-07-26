package com.yahoo.dtf.config.transform;

import org.apache.commons.jxpath.JXPathContext;

import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.json.JSONException;
import com.yahoo.dtf.json.JSONObject;

/**
 * @dtf.feature JPath Transformer 
 * @dtf.feature.group Transformers
 * @dtf.feature.desc 
 * 
 * <p>
 * The jpath transformer is just like the xpath one where you can use XPath 
 * expressions, but in this case you're applying them to JSON objects.
 * </p>
 *                 
 * <p>
 * The JPath works very similarly to the XPath usage but the difference is the 
 * data being operated on is a JSON object. The nice thing is the language 
 * doesn't change at all so if we had a JSON object contained in a property 
 * like so:
 * </p>
 *                    
 * <pre>
 *  <property name="json_array">
 *  { 
 *    "list": [
 *             { "id": "1001", "type": "Regular" },
 *             { "id": "1002", "type": "Chocolate" },
 *             { "id": "1003", "type": "Blueberry" },
 *             { "id": "1004", "type": "Devil's Food" }
 *            ]
 *  }
 *  </property> 
 * </pre>
 *                   
 * <p>
 * Say we wanted to be able to get the 3rd element in the a list and just print 
 * the value of the attribute type, here's how we could easily log that exact 
 * part of the JSON object:
 * </p>
 *                   
 * <pre>
 *  <log>${json_array:jpath:/list[3]/@type}</log>
 * </pre>
 *                   
 * <p>
 * Its pretty easy to see that the power of using XPath expressions against JSON
 * objects allows you to do a lot more with your JSON data in your test than you
 * could possibly imagine.
 * </p>
 */
public class JPathTransformer implements Transformer {
    
    public String apply(String data, String expression) throws ParseException {
        try { 
            JSONObject json = new JSONObject(data);
            JXPathContext ctx = JXPathContext.newContext(json);
            return ctx.getValue(expression).toString();
        } catch (JSONException e) { 
            throw new ParseException("Error parsing JSON.",e);
        }
    }
}
