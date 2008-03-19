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

package com.volantis.mcs.model.descriptor;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.model.impl.descriptor.MostSpecificClassMap;

public class MostSpecificClassMapTestCase
        extends TestCaseAbstract {

    /**
     * Test that an empty map always returns null.
     */
    public void testEmpty() {
        MostSpecificClassMap map = new MostSpecificClassMap();

        assertNull(map.get(Object.class));
        assertNull(map.get(String.class));
    }

    /**
     * Test that if the map contains only the Object.class that it is always
     * returned.
     */
    public void testObject() {

        MostSpecificClassMap map = new MostSpecificClassMap();
        map.put(Object.class, "Object");

        assertEquals("Object", map.get(Object.class));
        assertEquals("Object", map.get(String.class));
        assertEquals("Object", map.get(Integer.class));
    }

    /**
     * Test that if the map contains Object and String then everything apart
     * from String returns the value associated with Object.
     */
    public void testString() {

        MostSpecificClassMap map = new MostSpecificClassMap();
        map.put(Object.class, "Object");
        map.put(String.class, "String");

        assertEquals("Object", map.get(Object.class));
        assertEquals("String", map.get(String.class));
        assertEquals("Object", map.get(Integer.class));
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
