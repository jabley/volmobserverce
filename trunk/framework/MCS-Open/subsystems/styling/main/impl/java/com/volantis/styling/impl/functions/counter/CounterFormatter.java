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

/**
 * Formatting a counter result as a {@link StyleValue}.
 *
 * @mock.generate
 */
public interface CounterFormatter {

    /**
     * Format the counter value as a {@link StyleValue}.
     *
     * @param formatStyle  The style to use.
     * @param counterValue The counter value.
     * @return The style value.
     */
    StyleValue formatAsStyleValue(StyleKeyword formatStyle, int counterValue);

    /**
     * Format the counter value as a {@link String}.
     *
     * @param formatStyle  The style to use.
     * @param counterValue The counter value.
     * @return The style value as a string.
     */
    String formatAsString(StyleKeyword formatStyle, int counterValue);

}
