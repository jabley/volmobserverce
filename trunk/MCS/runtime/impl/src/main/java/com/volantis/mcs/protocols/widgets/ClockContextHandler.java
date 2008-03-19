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

package com.volantis.mcs.protocols.widgets;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.widgets.attributes.ClockContentAttributes;

/**
 * Initialy contains tables with default values of elements which are
 * displayed in clock (digits, months..). 
 */
public class ClockContextHandler{

    protected static final String[] defaultDigits = {
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
    };
    
    protected static final String[] defaultMonths = {
        "Jan", "Feb", "Mar",
        "Apr", "May", "Jun",
        "Jul", "Aug", "Sep",
        "Oct", "Nov", "Dec"
    };
    
    protected static final String[] defaultDays = {
        "Sun",   "Mon", "Tue", "Wed", 
        "Thu", "Fri", "Sat"
    };
    
    protected static final String[] defaultAMPM = {
        "AM", "PM"
    };
    
    protected static final String[] defaultSeparators = {
        " ", ":"
    };
    
    protected String[] digits = new String[10];
    protected String[] months = new String[12];
    protected String[] days = new String[7];
    protected String[] ampm = new String[2];
    protected HashMap separators = new HashMap();

    
    protected String[] getSeparatorsIds() throws ProtocolException {
        String[] ret = null;
        String[] tmp = new String[separators.size()];
        if (separators.size() > 0) {
            for (int i = 0; i < separators.size() ; i++) {
                // '+1' because page author defines separators beg. with 1
                String id = (String)separators.get(String.valueOf(i+1));
                if (id == null) {
                    throw new ProtocolException(
                            "value for a separator must be a number");
                } else {
                    tmp[i] = id;
                }
            }
            //all elements are ids
            ret = getIds(tmp, null);
        } else {
            //none of the elements are ids, the first parameter
            //is array with null values, it results that the second
            //parameter is used
            ret = getIds(
                    new String[defaultSeparators.length], defaultSeparators);
        }
        return ret;
    }
    
    /**
     * 
     * @param array clock-content elements ids
     * @param defaultValues values which will be used if 'array' does not
     *                      contain respecive value
     * @return sum of array and defaultValues arrays with additional field
     *             to indicate which of the elements is supplied by the page
     *             author and which one is default value.
     */
    protected String[] getIds(String[] array, String[] defaultValues) {
        StringBuffer suffix = new StringBuffer(array.length);
        
        String[] ret = new String[array.length+1];
        for (int i = 0 ; i < array.length ; i++) {
            if (array[i] == null) {
                ret[i] = defaultValues[i];
                suffix.append('0');
            } else {
                ret[i] = array[i];
                suffix.append('1');
            }
        }
        ret[array.length] = suffix.toString();
        return ret;
    }
    /**
     * Adds id of the element to appropriate collection.
     * 
     * @param id Id of the ClockContent element
     * @param type Type of the ClockContent element
     * @param value Value for the ClockContent element
     */
    public void addClockContent(
            String id,
            String type,
            String value) throws ProtocolException {
        
        if (type.equals("digit")) {
            int intValue = Integer.parseInt(value);
            digits[intValue] = id;
        } else if (type.equals("month")) {
            // months are ordered from 1 to 12
            int intValue = Integer.parseInt(value) - 1;
            months[intValue] = id;
        } else if (type.equals("day")) {
            // months are ordered from 1 to 7
            int intValue = Integer.parseInt(value) - 1;
            days[intValue] = id;
        } else if (type.equals("ampm")) {
            if (value.equals("am")) {
                ampm[0] = id;
            } else if (value.equals("pm")) {
                ampm[1] = id;
            } else {
                throw new ProtocolException(
                        "unknown value for AM/PM marker: " + value);
            }
        } else if (type.equals("separator")) {
            try {
                Integer.parseInt(value);
            } catch (NumberFormatException nfe) {
                throw new ProtocolException(
                        "unknown value for AM/PM marker: " + value);
            }               
            separators.put(value, id);
        } else {
            throw new ProtocolException("unknown type: " + type);   
        }
    }

    /**
     *  Returns comma separated ids of the string element for specified type.
     *  If string element was not defined for some value then default value
     *  is returned.
     *  Ids are followed by the string which contains 0 or 1 for all of the
     *  following elements. '0' on the n-th position indicates that 
     *  the n-th element is a default value; '1' inidicates that it is
     *  id of the ClockContent element.
     *  @param type Type of the clock-content elements.
     */
    public String[] getClockContentIds(String type) throws ProtocolException {
        String[] ret;
        if (type.equals("digits")) {
            ret = getIds(digits, defaultDigits);
        } else if (type.equals("months")) {
            ret = getIds(months, defaultMonths);
        } else if (type.equals("days")) {
            ret = getIds(days, defaultDays);
        } else if (type.equals("ampm")) {
            ret = getIds(ampm, defaultAMPM);
        } else if (type.equals("separators")) {
            ret = getSeparatorsIds();
        } else {
            throw new ProtocolException("unknown type: " + type);   
        }       
        return ret;     
    }
    
    
    /**
     * Adds clock content from each ClockContentAttribute to tables 
     * overwriting default values.
     * @param contentAttributes List of ClockContentAttributes
     * @throws ProtocolException
     */
    public void mergeContentAttributes(List contentAttributes) 
                                        throws ProtocolException {
        Iterator i = contentAttributes.iterator();
        while (i.hasNext()){
            ClockContentAttributes c = (ClockContentAttributes)i.next();
            addClockContent(c.getId(),c.getType(),c.getValue());
        }
    }
}
