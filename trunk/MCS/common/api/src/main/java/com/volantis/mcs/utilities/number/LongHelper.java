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
package com.volantis.mcs.utilities.number;

/**
 * Helper methods working on longs.
 */
public class LongHelper {

    /**
     * Returns a long hash code for strings.
     *
     * @param string the string whose hash code to be computed
     * @return the hash code
     */
    public static long hashCode(final String string) {
        long hash = 0;
        for (int i = 0; i < string.length(); i++) {
            hash = 31 * hash + string.charAt(i);
        }
        return hash;
    }

    /**
     * Returns the specified value as int. Values greater than Integer.MAX_VALUE
     * and less than Integer.MIN_VALUE are clipped.
     *
     * @param value the value to convert
     * @return the converted value
     */
    public static int asInt(final long value) {
        final int result;
        if (value > Integer.MAX_VALUE) {
            result = Integer.MAX_VALUE;
        } else if (value < Integer.MIN_VALUE) {
            result = Integer.MIN_VALUE;
        } else {
            result = (int) value;
        }
        return result;
    }
}
