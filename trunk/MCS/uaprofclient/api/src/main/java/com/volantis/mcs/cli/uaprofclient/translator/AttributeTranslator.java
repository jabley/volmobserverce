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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.cli.uaprofclient.translator;

import com.volantis.mcs.cli.uaprofclient.ProfileTranslator;

import java.util.Collection;
import java.util.Map;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * Instances of this class translate individual CC/PP UAProf device 
 * attribute(s) into MCS device attribute(s).
 * <p>
 * This is designed to take a Map as input and a Map as output which allows 
 * each translator to take as input a single attribute or a set of attributes, 
 * and similarly create a single translated attribute or set of translated
 * attributes.  
 */ 
public abstract class AttributeTranslator {

    /**
     * The JDK 1.4 Logger object to use.
     */ 
    private final static Logger logger = Logger.getLogger(
            "com.volantis.mcs.cli.uaprofclient.AttributeTranslator");

    private ProfileTranslator parent;

    protected AttributeTranslator(ProfileTranslator parent) {
        this.parent = parent;
    }

    /**
     * Process a (set of) CC/PP UAProf device attribute(s) from the input map 
     * into a (set of) MCS device attribute(s) in the output map. 
     * <p>
     * The input map is in String:Object format, where the key is a CC/PP 
     * UAProf attribute name and the value is the object returned by 
     * CC/PP API Attribute.getValue(). 
     * <p>
     * The output map is in String:String format, where both the key and value
     * must be valid MCS device attribute names and values.
     * <p>
     * Attributes should generally be removed from the input map once 
     * processed to prevent re-processing, and only added to the output map 
     * once to prevent overwriting.
     *  
     * @param inputMap the map of CC/PP device attributes.
     * @param outputMap the map of MCS device attributes
     */ 
    public abstract void processAttribute(Map inputMap, Map outputMap);
    
    protected void storeAttribute(String outputName, String outputValue, 
            Map outputMap) {

        char[] warnOnChars = parent.getWarnOnChars();
        for (int i = 0; i < warnOnChars.length; i++) {
            char warnOnChar = warnOnChars[i];
            if (outputValue.indexOf(warnOnChar) != -1) {
                logger.warning("Output attribute " + outputName + "=" +
                        outputValue + " contains character '" + warnOnChar +
                        "'");
            }
        }

        if (outputMap.containsKey(outputName)) {
            throw new IllegalStateException(
                    "attempt to add same attribute twice");
        }
        outputMap.put(outputName, outputValue);
    }
    
    protected Boolean getBooleanValue(String inputName, Map inputMap) {
        Boolean booleanValue = null;
        Object value = inputMap.get(inputName);
        if (value != null) {
            if (value instanceof Boolean) {
                booleanValue = (Boolean) value;
            } else {
                logger.warning("Attribute " + inputName + "=" +
                        value.toString() + " is not a " +
                        "Boolean, converting");
                booleanValue = Boolean.valueOf(value.toString());
            }
        }
        return booleanValue;
    }

    protected Integer getIntegerValue(String inputName, Map inputMap) {
        Integer integerValue = null;
        Object value = inputMap.get(inputName);
        if (value != null) {
            if (value instanceof Integer) {
                integerValue = (Integer) value;
            } else {
                logger.warning("Attribute " + inputName + "=" +
                        value.toString() + " is not an " +
                        "Integer, attempting to convert");
                try {
                    integerValue = Integer.valueOf(value.toString());
                } catch (NumberFormatException e) {
                    logger.severe("Attribute " + inputName + "=" +
                            value.toString() + " is not an " +
                            "Integer, and cannot be converted");
                }

            }
        }
        return integerValue;
    }
    
    protected String getCommaSeparatedValue(String inputName, Map inputMap) {
        String csvValue = null;
        Object value = inputMap.get(inputName);
        if (value != null) {
            csvValue = getCommaSeparatedValue(inputName, value);
        }
        return csvValue;
    }

    protected String getCommaSeparatedValue(String inputName, 
            Object inputValue) {
        String csvValue;
        if (inputValue instanceof Collection) {
            Collection collection = (Collection) inputValue;
            csvValue = "";
            for (Iterator values = collection.iterator(); values.hasNext(); ) {
                String value = (String) values.next();
                csvValue += value;
                if (values.hasNext()) {
                    csvValue += ", ";
                }
            }
        } else {
            logger.warning("Attribute " + inputName + "=" +
                    inputValue.toString() + " is not a " +
                    "Collection, converting");
            csvValue = inputValue.toString();
        }
        return csvValue;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Jun-04	4640/4	geoff	VBM:2004060402 UA Prof tool : cannot build uaprof tool

 09-Oct-03	1461/12	geoff	VBM:2003092501 Create CLI tool to translate UAProf data files into MCS xml import file (review comments)

 08-Oct-03	1461/10	geoff	VBM:2003092501 Create CLI tool to translate UAProf data files into MCS xml import file (final version)

 ===========================================================================
*/
