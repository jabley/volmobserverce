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
package com.volantis.synergetics;

import junit.framework.TestCase;

/**
 * Test case for ArrayUtils.
 */
public class ArrayUtilsTestCase extends TestCase {

    /**
     * Test the formatting version of toString().
     */
    public void testFormattingToString() {
        String array [] = {"one", "two", "three"};
        String result = ArrayUtils.toString(array, ", ", true);
        assertEquals("\"one\", \"two\", \"three\"", result);

        result = ArrayUtils.toString(array, ", ", false);
        assertEquals("one, two, three", result);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Jun-05	491/1	allan	VBM:2005062308 Move ArrayUtils into Synergetics and add a new toString

 ===========================================================================
*/
