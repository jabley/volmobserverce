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
package com.volantis.mcs.cli.uaprofclient.output;

import java.util.Map;

/**
 * Instances of this interface may be used to serialise MCS device attributes.
 */ 
public interface ProfileSerialiser {

    /**
     * The copyright statement.
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Serialise the device attributes contained in the collection provided.
     * 
     * @param attributes the device attributes to be serialised.
     */ 
    void serialise(Map attributes);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Oct-03	1461/3	geoff	VBM:2003092501 Create CLI tool to translate UAProf data files into MCS xml import file (final version)

 ===========================================================================
*/
