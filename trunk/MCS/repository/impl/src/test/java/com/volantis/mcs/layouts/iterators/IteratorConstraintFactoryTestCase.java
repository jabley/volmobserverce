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
 * Test that the constraint factory works properly.
 */
public class IteratorConstraintFactoryTestCase
    extends TestCaseAbstract{

    /**
     * Test that constraints are constructed properly.
     */
    public void testCreateConstraint() {
        doTestCreateConstraint(0, "fixed", false, Integer.MAX_VALUE);
        doTestCreateConstraint(0, "variable", false, Integer.MAX_VALUE);
        doTestCreateConstraint(1, "fixed", true, 1);
        doTestCreateConstraint(1, "variable", false, 1);
    }

    /**
     * Test that the spatial format iterator correctly determines the correct
     * column constraint to use based on the attributes that have been set.
     *
     * @param count The value of the column count attribute.
     * @param fixed The value of the columns attribute.
     * @param expectedFixed The expected value of the constraint's fixed
     * property.
     * @param expectedMaxValue The expected value of the constraint's maximum
     * value property.
     */
    private void doTestCreateConstraint(
            final int count, final String fixed,
            final boolean expectedFixed,
            final int expectedMaxValue) {

        IteratorConstraintFactory factory = new IteratorConstraintFactory();
        IteratorSizeConstraint constraint =
                factory.createConstraint(count, fixed.equals("fixed"));

        assertEquals("Fixed", expectedFixed, constraint.isFixed());
        int actualMaxValue = constraint.getMaximumValue();
        assertEquals("Maximum value", expectedMaxValue, actualMaxValue);
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
