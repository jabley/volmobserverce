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

/**
 * Test {@link FixedIteratorSizeConstraint}.
 */
public class FixedConstraintTestCase
        extends AbstractConstraintTestAbstract {

    // Javadoc inherited.
    protected IteratorSizeConstraint createConstraint(int maxInclusive) {
        return new FixedIteratorSizeConstraint(maxInclusive);
    }

    /**
     * Test that creating an object works and the returned object has the
     * correct characteristics.
     */
    public void testCreation() {
        IteratorSizeConstraint constraint = createConstraint(5);
        assertEquals("Fixed", true, constraint.isFixed());
        assertEquals("Maximum Value", 5, constraint.getMaximumValue());
        assertEquals("Constrained 1", 1, constraint.getConstrained(1));
        assertEquals("Constrained 10", 5, constraint.getConstrained(10));
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
