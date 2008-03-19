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

package com.volantis.mcs.layouts.spatial;

/**
 * An array of strings that never ends, it simply loops back around on itself.
 *
 * @mock.generate
 */
public class EndlessStringArray {

    /**
     * The underlying array of strings, may be null, empty or full.
     */
    private final String [] strings;

    /**
     * Initialise.
     *
     * @param strings The strings, these should probably be copied but for
     * now will just be used directly. Null is treated as an empty array.
     */
    public EndlessStringArray(String[] strings) {
        this.strings = strings;
    }

    /**
     * Get the string at position i.
     *
     * @param i The index of the string requested.
     *
     * @return The string requested, or null.
     */
    public String get(int i) {
        if (strings == null || strings.length == 0) {
            return null;
        } else {
            return strings[i % strings.length];
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 ===========================================================================
*/
