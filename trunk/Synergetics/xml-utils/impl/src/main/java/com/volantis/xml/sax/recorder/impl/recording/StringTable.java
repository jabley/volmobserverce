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

package com.volantis.xml.sax.recorder.impl.recording;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A string table that ensures that all strings stored in the recording are
 * unique and have a unique index.
 *
 * <p>This minimizes the number of objects that have to be managed by the
 * garbage collector and therefore improves performance and memory usage.</p>
 */
public class StringTable {

    /**
     * The list of unique strings, in the order they were added.
     */
    private final List strings;

    /**
     * The map from {@link String} to its {@link Integer} index.
     */
    private final Map string2Index;

    /**
     * The resulting array.
     */
    private String[] stringArray;

    /**
     * Initialise.
     */
    public StringTable() {
        strings = new ArrayList();
        string2Index = new HashMap();
    }

    /**
     * Get an index for the string.
     *
     * <p>If the string is already in the table then it returns its index,
     * otherwise it adds the string to the end of the table and stores it with
     * its index that will be returned the next time it is used.</p>
     *
     * @param string The string whose index is required.
     * @return The index.
     */
    public int getIndex(String string) {
        Integer index = (Integer) string2Index.get(string);
        if (index == null) {
            index = new Integer(strings.size());
            strings.add(string);
            string2Index.put(string, index);
        }

        return index.intValue();
    }

    /**
     * Intern the {@link String}.
     *
     * <p>If the string exists in the table then it returns that string,
     * otherwise it adds the string to the table and returns it.</p>
     *
     * @param string The string to intern.
     * @return The interned string.
     */
    public String intern(String string) {
        int index = getIndex(string);
        return (String) strings.get(index);
    }

    /**
     * Convert the table into an array that can be indexed with the returned
     * indeces.
     *
     * @return The array.
     */
    public String[] toArray() {
        if (stringArray == null) {
            stringArray = new String[strings.size()];
            strings.toArray(stringArray);
        }
        return stringArray;
    }
}
