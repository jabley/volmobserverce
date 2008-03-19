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
 * Iterator value translators translate a single input attribute into one 
 * output attribute, removing the input attribute from the input map via an 
 * iterator.
 * <p>
 * These translators are used to implement generic rules for translating
 * normal attributes that have their names defined in the name properties file.  
 */ 
public abstract class IteratorValueTranslator 
        extends AttributeTranslator {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The JDK 1.4 Logger object to use.
     */ 
    private final static Logger logger = Logger.getLogger(
            "com.volantis.mcs.cli.uaprofclient.IteratorValueTranslator");

    private Iterator iterator;
    protected String inputName;
    protected Object inputValue;
    protected String outputName;

    public IteratorValueTranslator(Iterator iterator, 
            String inputName, Object inputValue, String outputName,
            ProfileTranslator parent) {
        super(parent);
        this.iterator = iterator;
        this.inputName = inputName;
        this.inputValue = inputValue;
        this.outputName = outputName;
    }

    public void processAttribute(Map inputMap, Map outputMap) {
        
        // Remove our input attribute from the input map, via it's iterator. 
        iterator.remove();

        processAttribute(outputMap);

    }

    protected abstract void processAttribute(Map outputMap);
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
