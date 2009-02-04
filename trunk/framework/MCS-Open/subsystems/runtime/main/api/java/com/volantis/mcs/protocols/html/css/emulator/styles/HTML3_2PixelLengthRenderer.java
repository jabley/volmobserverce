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

package com.volantis.mcs.protocols.html.css.emulator.styles;

import com.volantis.mcs.protocols.styles.AbstractPropertyRenderer;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.utilities.StringConvertor;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;

public abstract class HTML3_2PixelLengthRenderer
        extends AbstractPropertyRenderer {

    private final StyleProperty property;

    protected HTML3_2PixelLengthRenderer(StyleProperty property) {
        this.property = property;
    }

    public String getAsString(MutablePropertyValues propertyValues) {
        StyleValue value = propertyValues.getComputedValue(property);
        if (value instanceof StyleLength) {
            StyleLength length = (StyleLength) value;
            if (length.getUnit() == LengthUnit.PX) {
                return StringConvertor.valueOf((int) length.getNumber());
            }
        }

        return null;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 ===========================================================================
*/
