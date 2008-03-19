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

import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Translates a single input attribute into a single output attribute, 
 * just changing it's name.
 */ 
public class SimpleTranslator 
        extends IteratorValueTranslator {

    /**
     * The JDK 1.4 Logger object to use.
     */ 
    private final static Logger logger = Logger.getLogger(
            "com.volantis.mcs.cli.uaprofclient.SimpleTranslator");

    public SimpleTranslator(Iterator iterator, String inputName,
            Object inputValue, String outputName, ProfileTranslator parent) {
        super(iterator, inputName, inputValue, outputName, parent);
    }

    protected void processAttribute(Map outputMap) {
        
        logger.fine("Copying simple attribute " + inputName + "=" + 
                inputValue + " to " + outputName);

        // Add the output attribute to the output map.
        storeAttribute(outputName, inputValue.toString(), outputMap);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Jun-04	4640/4	geoff	VBM:2004060402 UA Prof tool : cannot build uaprof tool

 09-Oct-03	1461/6	geoff	VBM:2003092501 Create CLI tool to translate UAProf data files into MCS xml import file (review comments)

 08-Oct-03	1461/4	geoff	VBM:2003092501 Create CLI tool to translate UAProf data files into MCS xml import file (final version)

 ===========================================================================
*/
