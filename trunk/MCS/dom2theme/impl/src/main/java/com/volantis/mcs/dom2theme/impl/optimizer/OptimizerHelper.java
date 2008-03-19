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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom2theme.impl.optimizer;

import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.values.MarkerStyleValue;

/**
 * Helper class containing useful methods for a number of different areas.
 */
public class OptimizerHelper {

    /**
     * A special value that indicates that the actual value of the property is
     * irrelevant, e.g. if it is ignored because of the value of another
     * property.
     */
    public static final StyleValue ANY = new MarkerStyleValue("ANY");

    /**
     * Compare the two values to see whether they are equal.
     *
     * <p>If either of them are {@link OptimizerHelper#ANY}, both null, or
     * equal according to their {@link com.volantis.mcs.themes.StyleValue#equals(Object)} method then
     * they are equal, otherwise they are not.</p>
     *
     * @param first  The first value.
     * @param second The second value.
     * @return True if they are equal, false otherwise.
     */
    public static boolean styleValueEqualsWithAny(
            final StyleValue first,
            final StyleValue second) {
        final boolean result;
        if (first == ANY || second == ANY) {
            result = true;
        } else if (first != null) {
            result = first.equals(second);
        } else {
            result = second == null;
        }
        return result;
    }
}
