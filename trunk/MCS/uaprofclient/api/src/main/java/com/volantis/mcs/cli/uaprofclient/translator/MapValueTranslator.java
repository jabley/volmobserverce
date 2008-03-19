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

/**
 * Map value translators translate a single input attribute into one or more 
 * output attributes, removing the input attribute from the input map directly.
 * <p>
 * These translators are used to implement the hardcoded custom rules for
 * individual special attributes that we are interested in.  
 */ 
public abstract class MapValueTranslator 
        extends AttributeTranslator {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    protected String inputName;

    public MapValueTranslator(String inputName, ProfileTranslator parent) {
        super(parent);
        this.inputName = inputName;
    }

    public void processAttribute(Map inputMap, Map outputMap) {
        Object inputValue = inputMap.get(inputName);
        if (inputValue != null) {
            // Remove our input attribute from the input map, directly. 
            if (inputMap.remove(inputName) == null) {
                throw new IllegalStateException("Unable to remove attribute " +
                        inputName + " from input map");
            }

            processAttribute(inputName, inputValue, outputMap);
        }
    }

    protected abstract void processAttribute(String inputName, 
            Object inputValue, Map outputMap);
    
    // todo: separate single input and multiple input translators into 
    // separate interfaces. 
    // multiple take the input map itself as input (these are custom ones)
    // single input take the attribute as input, and we have adaptors to adapt
    // them to either fixed or variable style, which remove the attribute from
    // either an iterator or a map as necessary. Thus we avoid passing in the
    // input map to variable single translators when it is redundant.
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Oct-03	1461/5	geoff	VBM:2003092501 Create CLI tool to translate UAProf data files into MCS xml import file (final version)

 ===========================================================================
*/
