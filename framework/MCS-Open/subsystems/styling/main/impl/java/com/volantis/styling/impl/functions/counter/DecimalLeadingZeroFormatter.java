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
 * Format the counter value as a decimal with a leading 0 for values less than
 * 10.
 */
public class DecimalLeadingZeroFormatter implements CounterFormatter {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    /**
     * The default instance.
     */
    private static final CounterFormatter defaultInstance =
            new DecimalLeadingZeroFormatter();

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
        return STYLE_VALUE_FACTORY.getString(null,
            formatAsString(formatStyle, counterValue));
    }

    // Javadoc inherited.
    public String formatAsString(StyleKeyword formatStyle, int counterValue) {
        boolean negative = counterValue < 0;
        String sign;
        if (negative) {
            counterValue = -counterValue;
            sign = "-";
        } else {
            sign = "";
        }

        String format;
        if (counterValue < 10) {
            format = sign + "0" + String.valueOf(counterValue);
        } else {
            format = sign + String.valueOf(counterValue);
        }
        return format;
    }
}
