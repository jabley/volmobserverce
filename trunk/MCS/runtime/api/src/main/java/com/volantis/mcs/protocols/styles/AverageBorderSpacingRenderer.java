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
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueVisitorStub;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.styling.Styles;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.styling.values.PropertyValues;

/**
 * A value handler that averages the border spacing values.
 */
public class AverageBorderSpacingRenderer
        extends StyleValueVisitorStub
        implements PropertyHandler {

    private int count;

    private int total;

    // Javadoc inherited.
    public boolean isSignificant(PropertyValues propertyValues) {
        int average = doCalculation(propertyValues);
        return average > 0;
    }

    private int doCalculation(PropertyValues propertyValues) {
        StyleValue styleValue = propertyValues.getComputedValue(
                StylePropertyDetails.BORDER_SPACING);
        count = 0;
        total = 0;
        if (styleValue != null) {
            styleValue.visit(this, null);
        }

        return count > 0 ? (total / count) : 0;
    }

    // Javadoc inherited.
    public boolean isSignificant(Styles styles) {
        return isSignificant(styles.getPropertyValues());
    }

    // Javadoc inherited.
    public String getAsString(MutablePropertyValues propertyValues) {
        int average = doCalculation(propertyValues);
        if (average > 0) {
            return String.valueOf(average);
        }
        return null;
    }

    // Javadoc inherited.
    public String getAsString(Styles styles) {
        return getAsString(styles.getPropertyValues());
    }

    // Javadoc inherited.
    public void visit(StyleLength value, Object object) {
        LengthUnit units = value.getUnit();
        if (units == LengthUnit.PX) {
            total += (int) value.getNumber();
            count += 1;
        }
    }

    // Javadoc inherited.
    public void visit(StylePair value, Object object) {
        StyleValue first = value.getFirst();
        if (first != null) {
            first.visit(this, null);
        }
        StyleValue second = value.getSecond();
        if (second != null) {
            second.visit(this, null);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
