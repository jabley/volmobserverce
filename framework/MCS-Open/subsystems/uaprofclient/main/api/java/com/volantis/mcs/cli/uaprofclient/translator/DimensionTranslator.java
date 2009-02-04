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

import javax.ccpp.uaprof.Dimension;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;

/**
 * Translates a single input attribute into two output attributes by 
 * translating it's value from a Dimension into two separate values and 
 * creating two separate new names for them.
 */ 
public class DimensionTranslator 
        extends MapValueTranslator {

    /**
     * The JDK 1.4 Logger object to use.
     */ 
    private final static Logger logger = Logger.getLogger(
            "com.volantis.mcs.cli.uaprofclient.DimensionTranslator");

    private String outputBaseName;
    
    private String nameExtensionX;
    
    private String nameExtensionY;

    public DimensionTranslator(String inputName,
            String outputBaseName, String nameExtensionX,
            String nameExtensionY, ProfileTranslator parent) {
        super(inputName, parent);
        this.outputBaseName = outputBaseName;
        this.nameExtensionX = nameExtensionX;
        this.nameExtensionY = nameExtensionY;
    }

    public void processAttribute(String inputName, Object inputValue,
            Map outputMap) {

        boolean parsed = false;
        String outputValueX = null;
        String outputValueY = null;
        
        if (inputValue instanceof Dimension) {
            Dimension dimension = (Dimension) inputValue;
            outputValueX = "" + dimension.getWidth();
            outputValueY = "" + dimension.getHeight();
            parsed = true;
        } else {
            logger.warning("Found attribute " + 
                    inputName + "=" + inputValue + 
                    ", expected Dimension, got " + inputValue.getClass() + 
                    ", attempting to parse from string");
            StringTokenizer tok = new StringTokenizer(inputValue.toString(), 
                    "x");
            if (tok.countTokens() == 2) {
                outputValueX = tok.nextToken();
                outputValueY = tok.nextToken();
                try {
                    Integer.parseInt(outputValueX);
                    Integer.parseInt(outputValueY);
                    parsed = true;
                } catch (NumberFormatException e) {
                    // numbers invalid; ignore and fall through
                }
            }
            // else wrong number of tokens; ignore and fall through.
        }

        if (parsed) {
            String outputNameX = outputBaseName + nameExtensionX;
            String outputNameY = outputBaseName + nameExtensionY;
    
            logger.fine("Copying Dimension attribute " + inputName + 
                    "=" + inputValue + " to " + 
                    outputNameX + "=" + outputValueX + " and " + 
                    outputNameY + "=" + outputValueY);
        
            // Add the output attributes to the output map.
            storeAttribute(outputNameX, outputValueX, outputMap);
            storeAttribute(outputNameY, outputValueY, outputMap);
        } else {
            logger.warning("Found attribute " + 
                    inputName + "=" + inputValue + 
                    ", expected Dimension, got " + inputValue.getClass() + 
                    ", unable to parse, ignoring");
        }
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
