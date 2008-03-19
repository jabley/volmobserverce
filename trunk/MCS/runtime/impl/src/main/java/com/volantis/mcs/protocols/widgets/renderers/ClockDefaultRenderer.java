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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.widgets.renderers;

import java.util.HashMap;

import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.widgets.JavaScriptStringFactory;

public abstract class ClockDefaultRenderer extends WidgetDefaultRenderer {
    
    /**
     * Calls addUsedWidgetId for each clock content id
     */
    protected void addUsedWidgetIds(String[] array) {
        if (array != null) {
            String last = array[array.length-1];
            for (int i = 0 ; i < array.length-1; i++) {
                if (last.charAt(i) == '1') {
                    addUsedWidgetId(array[i]);
                }
            }
        }		
    }
	
    /**
     * @param array [a,b,c]
     * @return "'a','b','c'"
     */
    protected String splitIds(String[] array) {
        StringBuffer ret = new StringBuffer();
        JavaScriptStringFactory jsStringFactory = 
            JavaScriptStringFactory.getInstance();
        for (int i = 0 ; i < array.length-1 ; i++) {
            ret.append(jsStringFactory.createJavaScriptString(array[i]))
            .append(",");
        }
        ret.append(jsStringFactory.createJavaScriptString(array[array.length-1]));
        return ret.toString();
    }
	
    /**
     * Checks if format is correctm, and if is creates a string like 
     * ['%i','%1','%s']
     */
    protected String getDateTimeFormat(String dateTime,char [] specialMarks) 
    	throws ProtocolException {	
        HashMap elems = new HashMap();
    
        char current;
        int i = 0; //index of the char in the dateTime string
        int j = 0; //index of the ret 'array'
        while (i < dateTime.length()) {
            current = dateTime.charAt(i);
            if (current == '%') {
                i++;
                if (i < dateTime.length()) {
                    current = dateTime.charAt(i);
                    boolean isSpecialCharacter = false;
                    //check whether it is special character
                    for (int k = 0; k < specialMarks.length ; k++) {
                        if (current == specialMarks[k]) {
                            isSpecialCharacter = true;
                            break;
                        }
                    }
                    if (isSpecialCharacter) {
                        elems.put(String.valueOf(j), "%" + current);
                        i++;
                        j++;
                    } else {
                        //check whether it is a number (for separators)
                        boolean isNumber = true;
                        boolean stop = false;
                        StringBuffer number = new StringBuffer();
                        try {
                            Integer.parseInt(String.valueOf(current));
                        } catch (NumberFormatException nfe) {
                            isNumber = false;
                            stop = true;
                        }
                        number.append("%").append(current);
                        while (!stop) {
                            i++;
                            if (i < dateTime.length()) {
                                current = dateTime.charAt(i);
                                try {
                                    Integer.parseInt(String.valueOf(current));
                                    number.append(current);
                                } catch (NumberFormatException nfe) {
                                    stop = true;
                                }
                            } else {
                                stop = true;
                            }
                        }
                        if (isNumber) {
                            elems.put(String.valueOf(j), number.toString());
                            j++;
                        } else {
                            //nither special char nor number after '%'
                            throw new ProtocolException(
                                    "nither special char nor number after '%': "
                                    + dateTime);
                        }			
                    }
                } else {
                    //no more chars after '%'
                    throw new ProtocolException(
                            "missed char after '%': " + dateTime);
                }
            } else {
                //literal separator
                StringBuffer str = new StringBuffer();
                while (current != '%') {
                    i++;
                    str.append(current);
                    if (i < dateTime.length()) {
                        current = dateTime.charAt(i);
                    } else {
                        break;
                    }
                }   
                if (str.length() > 0) {
                    elems.put(String.valueOf(j), str.toString());
                    j++;
                }
            }
        }
		
        StringBuffer ret = new StringBuffer();
        if (elems.size() > 0) {
            for (int k = 0; k < elems.size() - 1; k++) {
                ret.append(
                        createJavaScriptString((String) elems.get(
                                String.valueOf(k)))).append(",");
            }
            ret.append(
                    createJavaScriptString((String) elems.get(
                            String.valueOf(elems.size()-1))));
        }
        return ret.toString();
    }
}
