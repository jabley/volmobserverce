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
 * Translates a single input attribute into two output attributes with 
 * different names and identical values.
 */ 
public class DuplicateTranslator extends MapValueTranslator {

    /**
     * The JDK 1.4 Logger object to use.
     */ 
    private final static Logger logger = Logger.getLogger(
            "com.volantis.mcs.cli.uaprofclient.DuplicateTranslator");

    private String outputName1;
    private String outputName2;

    public DuplicateTranslator(String inputName, String outputName1,
            String outputName2, ProfileTranslator parent) {
        super(inputName, parent);
        this.outputName1 = outputName1;
        this.outputName2 = outputName2;
    }

    protected void processAttribute(String inputName, Object inputValue,
            Map outputMap) {

        String outputValue = inputValue.toString();
        
        logger.fine("Duplicating attribute " + inputName + "=" + 
                outputValue + " to " + outputName1 + " and " +
                outputName2);

        // Add the output attribute to the output map.
        storeAttribute(outputName1, outputValue, outputMap);
        storeAttribute(outputName2, outputValue, outputMap);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Oct-03	1461/5	geoff	VBM:2003092501 Create CLI tool to translate UAProf data files into MCS xml import file (review comments)

 08-Oct-03	1461/3	geoff	VBM:2003092501 Create CLI tool to translate UAProf data files into MCS xml import file (final version)

 ===========================================================================
*/
