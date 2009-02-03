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

package com.volantis.xml.pipeline.sax.dynamic;

/**
 * Enumeration of the different ways that a dynamic process can process the
 * events that it receives.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @todo later implement this stub class
 */
public class DynamicProcessingMode {

    /**
     * This is the normal mode for the dynamic process. In this mode it
     * does everything that it describes in the documentation for the
     * {@link DynamicProcess}.
     */
    public static final DynamicProcessingMode NORMAL
            = new DynamicProcessingMode("NORMAL");

    /**
     * In this mode the dynamic process simply passes all the events directly
     * through, it does not invoke element rules, or do any attribute
     * expression eveluation.
     */
    public static final DynamicProcessingMode PASS_THROUGH
            = new DynamicProcessingMode("PASS_THROUGH");

    private final String myName; // for debug only

    private DynamicProcessingMode(String name) {
        myName = name;
    }

    public String toString() {
        return myName;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 16-Jul-03	208/1	doug	VBM:2003071603 Added as part of Pipeline public API

 ===========================================================================
*/
