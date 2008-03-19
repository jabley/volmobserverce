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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.descriptorstore.impl;

import com.volantis.synergetics.descriptorstore.ParameterNames;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Default implementation of the Parameter Name.
 */
public class DefaultParameterNames implements ParameterNames {

    /**
     * Delegate to a underlying SortedSet for actual storage. SortedSet
     * allows us to comapare two sets for the same content.
     */
    private SortedSet set  = new TreeSet();

    // javadoc inherited
    public void setName(String name) {
        set.add(name);
    }

    // javadoc inherited
    public boolean containsName(String name) {
        return set.contains(name);
    }

    // javadoc inherited
    public Iterator iterator() {
        return set.iterator();
    }

    // javadoc inherited
    public boolean equals(Object obj) {
        boolean result = false;
        if (getClass() == obj.getClass()) {
            DefaultParameterNames other = (DefaultParameterNames) obj;
            result = set.equals(other.set);
        }
        return result;
    }

    // javadoc inherited
    public int hashCode() {
        return set.hashCode();
    }
}
