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

package com.volantis.shared.security.impl.acl;

import java.util.Iterator;
import java.util.Map;

/**
 * Iterator over all the entries added into the ACL.
 */
public class ACLIterator
        implements Iterator {

    /**
     * The maps embedded within the ACL.
     */
    private final Map[] maps;

    /**
     * The index within the maps array.
     */
    private int index;

    /**
     * The current iterator.
     */
    private Iterator iterator;

    /**
     * The array of maps.
     *
     * @param maps The array of maps.
     */
    public ACLIterator(Map[] maps) {
        this.maps = maps;
    }

    // Javadoc inherited.
    public void remove() {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public boolean hasNext() {
        while (!iterator.hasNext() && (++index) < maps.length) {
            Map map = maps[index];
            if (map != null) {
                iterator = map.entrySet().iterator();
            }
        }

        return index < maps.length;
    }

    // Javadoc inherited.
    public Object next() {
        return iterator.next();
    }
}
