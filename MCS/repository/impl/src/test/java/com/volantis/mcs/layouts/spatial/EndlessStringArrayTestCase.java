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

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.layouts.spatial.EndlessStringArray;
import com.volantis.mcs.layouts.spatial.EndlessStringArray;

/**
 * Test getting strings from an {@link EndlessStringArray}.
 */
public class EndlessStringArrayTestCase
        extends TestCaseAbstract {

    /**
     * Test getting strings from a null array.
     */
    public void testGetNull() {

        EndlessStringArray array = new EndlessStringArray(null);
        String result;

        result = array.get(1);
        assertNull("Result should be null", result);
        result = array.get(359051);
        assertNull("Result should be null", result);
    }

    /**
     * Test getting strings from an empty array.
     */
    public void testGetEmpty() {

        EndlessStringArray array = new EndlessStringArray(new String[0]);
        String result;

        result = array.get(1);
        assertNull("Result should be null", result);
        result = array.get(359051);
        assertNull("Result should be null", result);
    }

    /**
     * Test getting strings from a non empty, non null array.
     */
    public void testGet() {

        EndlessStringArray array = new EndlessStringArray(new String[] {
            "alpha", "beta"
        });
        String result;

        result = array.get(0);
        assertEquals("get(0)", "alpha", result);
        result = array.get(1);
        assertEquals("get(1)", "beta", result);
        result = array.get(359051);
        assertEquals("get(359051)", "beta", result);
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
