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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.styles;

import com.volantis.mcs.themes.StyleInteger;

/**
 *  Handles the rendering of integer based values.
 */

public class IntegerHandler
        extends ValueRendererChecker {


    public void visit(StyleInteger value, Object object) {
        int i = value.getInteger();
        if (i != -1) {
            string = new Integer(i).toString();
        } else {
            string = null;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/2	pduffin	VBM:2005111405 Massive changes for performance

 07-Nov-05	10096/2	ianw	VBM:2005101918 Change rendering of mcs-rows to use correct styling, also fix ClassCastException in AssetGroups

 ===========================================================================
*/
