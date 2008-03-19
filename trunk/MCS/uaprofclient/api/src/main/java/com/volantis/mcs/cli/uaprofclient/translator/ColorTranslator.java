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
 * Custom translator for the ColorCapable and BitsPerPixel attributes.
 * <p>
 * This is as per the rules in the spreadsheet attached to the requirement. 
 */ 
public class ColorTranslator extends AttributeTranslator {

    /**
     * The JDK 1.4 Logger object to use.
     */ 
    private final static Logger logger = Logger.getLogger(
            "com.volantis.mcs.cli.uaprofclient.ColorTranslator");

    public ColorTranslator(ProfileTranslator parent) {
        super(parent);
    }

    public void processAttribute(Map inputMap, Map outputMap) {
        
        Boolean colorCapable = getBooleanValue("ColorCapable", inputMap);
        Integer bitsPerPixel = getIntegerValue("BitsPerPixel", inputMap);

        inputMap.remove("ColorCapable");
        // Can't remove BitsPerPixel, as it is used normally as well.
        
        String outputName = "rendermode";
        String outputValue = null;

        if (colorCapable != null && colorCapable.booleanValue() ) {
            if (bitsPerPixel != null && bitsPerPixel.intValue() > 8) {
                outputValue = "rgb";
            } else {
                outputValue = "palette";
            }
        } else {
            outputValue = "greyscale";
        }

        logger.fine("Copying ColorCapable=" + colorCapable + " and " + 
                " BitsPerPixel=" + bitsPerPixel + " to " + outputName + "=" + 
                outputValue);
        
        storeAttribute(outputName, outputValue, outputMap);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Oct-03	1461/8	geoff	VBM:2003092501 Create CLI tool to translate UAProf data files into MCS xml import file (review comments)

 08-Oct-03	1461/6	geoff	VBM:2003092501 Create CLI tool to translate UAProf data files into MCS xml import file (final version)

 ===========================================================================
*/
