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

import java.util.Map;
import java.util.logging.Logger;

/**
 * Translates a single input attribute into a single output attribute by 
 * changing it's name and translating its value by keyword lookup.
 */ 
public class KeywordTranslator 
        extends MapValueTranslator {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The JDK 1.4 Logger object to use.
     */ 
    private final static Logger logger = Logger.getLogger(
            "com.volantis.mcs.cli.uaprofclient.KeywordTranslator");

    private String outputName;
    
    private Map keywordMap;

    public KeywordTranslator(String inputName, String outputName,
            Map keywordMap, ProfileTranslator parent) {
        super(inputName, parent);
        this.outputName = outputName;
        this.keywordMap = keywordMap;
    }

    public void processAttribute(String inputName, Object inputValue, 
            Map outputMap) {

        // Here we don't bother to check the type of attribute. We assume that
        // if a name that is registered is found, that is enough of a check.
        // This means that collection types will probably fail :-). We could
        // detect these and apply mapping to each value, but we don't need 
        // that at the moment.
        
        String outputValue = (String) keywordMap.get(
                inputValue.toString());
        if (outputValue == null) {
            logger.warning("Unable to find keyword " +
                    "mapping for attribute " + inputName +
                    " value of " + inputValue + ", ignoring");
            return;
        }
            
        logger.fine("Translating keyword attribute " + inputName + 
                "=" + inputValue + " to " + outputValue);
    
        // Add the output attribute to the output map.
        storeAttribute(outputName, outputValue, outputMap);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Jun-04	4640/4	geoff	VBM:2004060402 UA Prof tool : cannot build uaprof tool

 09-Oct-03	1461/7	geoff	VBM:2003092501 Create CLI tool to translate UAProf data files into MCS xml import file (review comments)

 08-Oct-03	1461/5	geoff	VBM:2003092501 Create CLI tool to translate UAProf data files into MCS xml import file (final version)

 ===========================================================================
*/
