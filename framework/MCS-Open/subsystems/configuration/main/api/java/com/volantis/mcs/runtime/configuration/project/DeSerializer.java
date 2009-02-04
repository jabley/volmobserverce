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

package com.volantis.mcs.runtime.configuration.project;

/**
 * Helper class used when de/serializing values to and from XML using JIBX.
 */
public class DeSerializer {

    /**
     * Convert an unlimited non negative integer to a String.
     *
     * @param integer The unlimited non negative integer.
     * @return The string representation.
     */
    public static String unlimitedNonNegativeToString(Integer integer) {
        return integer.intValue() == Integer.MAX_VALUE ? "unlimited" :
                String.valueOf(integer);
    }

    /**
     * Convert a string to an unlimited non negative Integer.
     *
     * @param string The string.
     * @return The integer representation.
     */
    public static Integer stringToUnlimitedNonNegative(String string) {
        if (string.equals("unlimited")) {
            return new Integer(Integer.MAX_VALUE);
        } else {
            return new Integer(Integer.parseInt(string));
        }
    }
}
