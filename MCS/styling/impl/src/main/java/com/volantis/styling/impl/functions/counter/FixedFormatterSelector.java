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

/**
 * Encapsulates a fixed formatter.
 *
 * <p>Simply returns the formatter that was supplied on the constructor.</p>
 */
public class FixedFormatterSelector
        implements CounterFormatterSelector {

    /**
     * The fixed formatter.
     */
    private final CounterFormatter formatter;

    /**
     * Initialise.
     *
     * @param formatter The fixed formatter.
     */
    public FixedFormatterSelector(CounterFormatter formatter) {
        this.formatter = formatter;
    }

    // Javadoc inherited.
    public CounterFormatter selectFormatter(
            StyleKeyword formatStyle, int counterValue) {
        return formatter;
    }
}
