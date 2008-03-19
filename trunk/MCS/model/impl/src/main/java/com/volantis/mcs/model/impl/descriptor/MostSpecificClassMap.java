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

package com.volantis.mcs.model.impl.descriptor;

import java.util.List;
import java.util.ArrayList;

/**
 * Map from a class to the most specific class in the map and return the
 * associated object.
 */
public class MostSpecificClassMap {

    private List entries;

    public MostSpecificClassMap() {
        entries = new ArrayList();
    }

    public void put(Class key, Object value) {
        for (int i = 0; i < entries.size(); i++) {
            Entry entry = (Entry) entries.get(i);
            if (entry.key == key) {
                entries.remove(i);
                break;
            }
        }
        
        entries.add(new Entry(key, value));
    }

    public Object get(Class key) {
        Entry best = null;
        for (int i = 0; i < entries.size(); i++) {
            Entry entry = (Entry) entries.get(i);
            if (entry.key.isAssignableFrom(key)) {
                if (best == null) {
                    best = entry;
                } else if (best.key.isAssignableFrom(entry.key)) {
                    // Best is less specific than the entry so the
                    // entry is better.
                    best = entry;
                } else {
                    // Best is more specific than the entry so it is better.
                }
            }
        }

        if (best == null) {
            return null;
        } else {
            return best.value;
        }
    }

    private static class Entry {

        final Class key;

        final Object value;

        public Entry(Class key, Object value) {
            this.key = key;
            this.value = value;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Fixed issue with mapping classes to type descriptors

 ===========================================================================
*/
