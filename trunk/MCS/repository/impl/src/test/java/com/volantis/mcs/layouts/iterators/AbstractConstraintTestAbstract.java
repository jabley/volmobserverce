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

package com.volantis.mcs.layouts.iterators;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Base class for constraint tests.
 */
public abstract class AbstractConstraintTestAbstract
        extends TestCaseAbstract {

    /**
     * Create the constraint to test.
     *
     * @param maxInclusive The maximum inclusive value.
     *
     * @return The newly created constraint.
     */
    protected abstract IteratorSizeConstraint createConstraint(
            int maxInclusive);

    /**
     * Test that error checking is done properly.
     */
    public void testErrorChecking() {
        doTestCreationErrorChecking(-1);
        doTestCreationErrorChecking(0);
    }

    /**
     * Create the object with an invalid value and make sure that it is
     * detected.
     *
     * @param maxInclusive An invalid value.
     */
    private void doTestCreationErrorChecking(final int maxInclusive) {
        try {
            createConstraint(maxInclusive);
            fail("Failed to detect illegal argument");
        } catch(IllegalArgumentException expected) {
            // Expected.
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
