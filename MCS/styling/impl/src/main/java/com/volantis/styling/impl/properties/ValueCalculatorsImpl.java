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

package com.volantis.styling.impl.properties;

import com.volantis.styling.properties.ValueCalculators;
import com.volantis.styling.properties.ValueCalculator;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.PropertyValues;
import com.volantis.mcs.themes.StyleValue;

public class ValueCalculatorsImpl
        implements ValueCalculators {

    private static final ValueCalculator DEFAULT_CALCULATOR =
            new ValueCalculator() {
                public StyleValue calculateValue(StyleProperty property,
                                                 PropertyValues values,
                                                 PropertyValues parentValues) {
                    return values.getSpecifiedValue(property);
                }
            };

    public ValueCalculator getValueCalculator(StyleProperty property) {
        return DEFAULT_CALCULATOR;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 21-Nov-05	10347/2	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
