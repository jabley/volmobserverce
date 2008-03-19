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
 * Instances select the appropriate formatter to use.
 *
 * @mock.generate
 */
public interface CounterFormatterSelector {

    /**
     * Select the formatter for the specified counter value
     *
     * @param formatStyle  The style of the format.
     * @param counterValue The value of the counter, needed because some
     *                     formatters only work for specific ranges of values.
     * @return The formatter.
     */
    CounterFormatter selectFormatter(
            StyleKeyword formatStyle,
            int counterValue);

}
