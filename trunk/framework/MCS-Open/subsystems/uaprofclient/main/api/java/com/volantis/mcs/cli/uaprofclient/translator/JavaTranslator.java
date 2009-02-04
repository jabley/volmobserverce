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
 * Custom translator for the JavaEnabled and JavaPlatform attributes.
 * <p>
 * This is as per the rules in the spreadsheet attached to the requirement. 
 */ 
public class JavaTranslator extends AttributeTranslator {

    /**
     * The JDK 1.4 Logger object to use.
     */ 
    private final static Logger logger = Logger.getLogger(
            "com.volantis.mcs.cli.uaprofclient.JavaTranslator");

    public JavaTranslator(ProfileTranslator parent) {
        super(parent);
    }

    public void processAttribute(Map inputMap, Map outputMap) {
        
        Boolean enabled = getBooleanValue("JavaEnabled", inputMap);
        String platform = getCommaSeparatedValue("JavaPlatform", inputMap);

        inputMap.remove("JavaEnabled");
        inputMap.remove("JavaPlatform");
        
        // If Java is enabled and a platform was provided,
        if (enabled != null && enabled.booleanValue() && platform != null) {

            // Copy the platform into the java attribute.
            storeAttribute("java", platform, outputMap);
            
        }
        
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Oct-03	1461/7	geoff	VBM:2003092501 Create CLI tool to translate UAProf data files into MCS xml import file (review comments)

 08-Oct-03	1461/5	geoff	VBM:2003092501 Create CLI tool to translate UAProf data files into MCS xml import file (final version)

 ===========================================================================
*/
