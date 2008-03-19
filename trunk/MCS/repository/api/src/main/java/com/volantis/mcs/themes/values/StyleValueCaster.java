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

package com.volantis.mcs.themes.values;

import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.StyleNumber;
import com.volantis.mcs.themes.StyleValueFactory;

/**
 * Responsible for casting {@link StyleValue} of one type into another.
 */
public class StyleValueCaster {
    
    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    /**
     * Attempt to cast the value to a value of the specified type.
     *
     * @param value The type of value from which the cast is being performed.
     * @param type  The type of value to which the cast would be performed.
     * @return The new value, or null if it could not be cast.
     */
    public StyleValue cast(StyleValue value, StyleValueType type) {
        StyleValue cast = null;
        if (value.getStyleValueType() == type) {
            cast = value;
        } else if (value instanceof StyleNumber) {
            double number = ((StyleNumber) value).getNumber();
            if (type == StyleValueType.INTEGER) {
                int integer = (int) number;
                if (integer == number) {
                    cast = STYLE_VALUE_FACTORY.getInteger(null, integer);
                }
            }
        }

        return cast;
    }
}
