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

package com.volantis.styling.unit.engine;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.Priority;

/**
 * Test cases for {@link Priority}.
 */
public class PriorityTestCase
        extends TestCaseAbstract {

    /**
     * Assert that the comparable object compares less than the other object.
     *
     * @param comparable The comparable object.
     * @param object The other object to compare against.
     */
    public void assertCompareLessThan(
            Comparable comparable, Comparable object) {

        assertTrue(comparable + " should be less than " + object,
                   comparable.compareTo(object) < 0);
        assertTrue(object + " should be greater than " + comparable,
                   object.compareTo(comparable) > 0);
    }

    /**
     * Assert that the comparable object compares greater than the other object.
     *
     * @param comparable The comparable object.
     * @param object The other object to compare against.
     */
    public void assertCompareGreaterThan(
            Comparable comparable, Comparable object) {

        // A > B implies that B < A so swap the parameters and use the less
        // than assertion.
        assertCompareLessThan(object, comparable);
    }

    /**
     * Assert that the comparable object compares equal to the other object.
     *
     * @param comparable The comparable object.
     * @param object The other object to compare against.
     */
    public void assertCompareEqual(Comparable comparable, Comparable object) {
        assertTrue(comparable + " should be equal to " + object,
                   comparable.compareTo(object) == 0);
        assertTrue(object + " should be equal to " + comparable,
                   object.compareTo(comparable) == 0);
    }

    /**
     * Test that priorities compare against each other correctly.
     */
    public void testCompareTo() {
        assertCompareEqual(Priority.NORMAL, Priority.NORMAL);
        assertCompareEqual(Priority.IMPORTANT, Priority.IMPORTANT);
        assertCompareLessThan(Priority.NORMAL, Priority.IMPORTANT);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 18-Jul-05	9029/1	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 ===========================================================================
*/
