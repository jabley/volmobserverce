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

package com.volantis.styling.impl.functions.counter;

import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;

/**
 * Format the counter value as a plain decimal.
 */
public class DecimalFormatter implements CounterFormatter {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    /**
     * The default instance.
     */
    private static final CounterFormatter defaultInstance =
            new DecimalFormatter();

    /**
     * Get the default instance.
     *
     * @return The default instance.
     */
    public static CounterFormatter getDefaultInstance() {
        return defaultInstance;
    }

    // Javadoc inherited.
    public StyleValue formatAsStyleValue(StyleKeyword formatStyle, int counterValue) {
        // This is the default, return it as a StyleInteger as the normal
        // conversion for that is the same as decimal and it means that
        // the counter function can be used within an mcs-container-instance
        // function.
        return STYLE_VALUE_FACTORY.getInteger(null, counterValue);
    }

    // Javadoc inherited.
    public String formatAsString(StyleKeyword formatStyle, int counterValue) {
        return String.valueOf(counterValue);
    }

}
