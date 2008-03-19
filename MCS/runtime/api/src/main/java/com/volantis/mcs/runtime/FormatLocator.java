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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime;

import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.FormatNamespace;

/**
 * Allows a format to be obtained in various ways.
 */
public interface FormatLocator {
    /**
     * Returns a Format appropriate to the given name and format namespace.
     *
     * @param name the name of the format to be retrieved
     * @param namespace the namespace for the format
     * @return the appropriate format or null if one cannot be found
     */
    Format getFormat(String name, FormatNamespace namespace);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Apr-05	7459/1	tom	VBM:2005032101 Added SmartClientSkin protocol

 ===========================================================================
*/
