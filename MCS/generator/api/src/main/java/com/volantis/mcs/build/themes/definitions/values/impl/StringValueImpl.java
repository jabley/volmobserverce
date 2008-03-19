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

package com.volantis.mcs.build.themes.definitions.values.impl;

import com.volantis.mcs.build.themes.definitions.values.StringValue;

import java.io.PrintStream;

/**
 * Implementation of a string value.
 */
public class StringValueImpl implements StringValue {
    /**
     * The string represented by this value.
     */
    String stringVal;

    // Javadoc inherited
    public void setString(String newValue) {
        stringVal = newValue;
    }

    // Javadoc inherited
    public void writeConstructCode(String indent, PrintStream out) {
        out.print(indent);
        out.print("styleValueFactory.getString(null, \"");
        out.print(stringVal);
        out.print("\")");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Dec-05	10845/1	pduffin	VBM:2005121511 Moved architecture files from CVS to MCS Depot

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 21-Oct-04	5833/1	adrianj	VBM:2004082605 Fix initial values for StylePropertyDetails

 ===========================================================================
*/
