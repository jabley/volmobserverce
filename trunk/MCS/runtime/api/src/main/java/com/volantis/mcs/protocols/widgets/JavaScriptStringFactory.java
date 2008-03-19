/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.widgets;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Creates render-ready JavaScript strings. 
 */
public class JavaScriptStringFactory {
    /**
     * A singleton instance of this class.
     */
    private final static JavaScriptStringFactory instance =
        new JavaScriptStringFactory();

    /**
     * Returns singleton instance of this class.
     * 
     * @return singleton instance of this class.
     */
    public static JavaScriptStringFactory getInstance() {
        return instance;
    }
    
    /**
     * Creates JavaScript string representation for given string.
     * 
     * <p>
     * Currently, it works by escaping specified string, and enclosing it with
     * quotes.
     * </p>
     * 
     * @param string The string to convert
     * @return the JavaScript string
     */
    public String createJavaScriptString(String string) {
        return '\'' + escapeJavaScriptString(string) + '\'';
    }
    
    /**
     * Creates JavaScript string containing JavaScript property name for
     * specified MemberName instance.
     * 
     * @param memberName The member name.
     * @return The JavaScript string with member name.
     */
    public String createJavaScriptString(MemberName memberName) {
        return createJavaScriptString(memberName.getName());
    }

    /**
     * Generates JavaScript espaced string from specified string.
     *  
     * @param string The satring to escape.
     * @return The escaped string.
     */
    private String escapeJavaScriptString(String string) {
        return StringEscapeUtils.escapeJavaScript(string);
    }
    
    /**
     * Camelizes specified string.
     * 
     * @param string The string to camelize.
     * @return The camelized string.
     */
    private String camelize(String string) {
        StringBuffer buffer = new StringBuffer(string.length());
        
        int lastMinusIndex = -1;
        
        do {
            int minusIndex = string.indexOf('-', lastMinusIndex + 1);

            // Extract next name component
            String component;
            
            if (minusIndex != -1) {
                // If minus sign was found, strip the name component from
                // previous up to the next minus sign.
                component = string.substring(lastMinusIndex + 1, minusIndex);
            } else {
                // If minus sign was not found, strip the name component from
                // previous minus to the end of the string.
                component = string.substring(lastMinusIndex + 1);
            }
            
            // If this is not the first name component, make it title-case.
            // Note, that the component may be empty, in case of double minus signs.
            if (lastMinusIndex != -1 && component.length() != 0) {
                component = Character.toUpperCase(component.charAt(0)) + component.substring(1);
            }
            
            // Append the component.
            buffer.append(component);
            
            // Store the position of last minus sign for next loop iteration.
            lastMinusIndex = minusIndex;
        } while (lastMinusIndex != -1);
        
        return buffer.toString();
    }
    
    /**
     * Creates and returns a string, which may be renderer in JavaScript code as a member reference.
     * 
     * @param reference The member reference to create string for.
     * @return The string.
     */
    public String createJavaScriptExpression(MemberReference reference) {
        final StringBuffer buffer = new StringBuffer();
        
        buffer.append("$W(")
            .append(createJavaScriptString(reference.getWidgetId()))
            .append(")");
        
        Iterator iterator = reference.getMemberNames().iterator();
        
        while(iterator.hasNext()) {
            MemberName memberName = (MemberName) iterator.next();

            buffer.append(".getMember(")
                .append(createJavaScriptString(memberName))
                .append(")");
        };
        
        return buffer.toString();
    }
    
    /**
     * Creates a string with JavaScript Object (literal enclosed with brackets),
     * containing mapping between keys and values.
     * 
     * Following assumptions are made:
     * <li>keys are Strings
     * <li>values are Strings containing pre-rendered JavaScript expression.
     * 
     * @param map
     * @return
     */
    public String createJavaScriptObject(Map map) {
        StringBuffer buffer = new StringBuffer("{");
        
        Iterator keyIterator = map.keySet().iterator();
        
        while (keyIterator.hasNext()) {
            String key = (String) keyIterator.next();
            String value = (String) map.get(key);
            
            buffer.append(key)
            //buffer.append(createJavaScriptString(key))
                .append(": ")
                .append(value);
            
            if (keyIterator.hasNext()) {
                buffer.append(", ");
            }
        }
        
        buffer.append("}");
        
        return buffer.toString();
    }   

    /**
     * Creates a string with JavaScript Array,
     * containing content of the list.
     * 
     * Following assumptions are made:
     * <li>values of a list are Strings containing pre-rendered JavaScript expressions.
     * 
     * @param list A list
     * @return
     */
    public String createJavaScriptArray(List list) {
        StringBuffer buffer = new StringBuffer("[");
        
        Iterator iterator = list.iterator();
        
        while (iterator.hasNext()) {
            String value = (String) iterator.next();
            
            buffer.append(value);
            
            if (iterator.hasNext()) {
                buffer.append(", ");
            }
        }
        
        buffer.append("]");
        
        return buffer.toString();
    }   
}
