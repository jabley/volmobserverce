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

package com.volantis.mcs.protocols.styles;

import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StylePair;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.values.LengthUnit;

/**
 * Treats lengths greater than 0 as significant.
 */
public class PositivePixelLengthChecker extends ValueCheckerImpl {

    // Javadoc inherited.
    public void visit(StyleLength value, Object object) {
        LengthUnit units = value.getUnit();
        if (units == LengthUnit.PX) {
            int number = (int) value.getNumber();
            significant = number > 0;
        }
    }


    // Javadoc inherited.
    public void visit(StylePair value, Object object) {
        StyleValue first = value.getFirst();
        StyleValue second = value.getSecond();

        first.visit(this, object);
        second.visit(this, object);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
