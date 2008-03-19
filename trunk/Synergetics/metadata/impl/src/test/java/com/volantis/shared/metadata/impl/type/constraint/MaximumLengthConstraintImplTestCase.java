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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.shared.metadata.impl.type.constraint;

import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.metadata.type.constraint.mutable.MutableMaximumLengthConstraint;

/**
 * Test case for {@link MaximumLengthConstraintImpl}.
 */
public class MaximumLengthConstraintImplTestCase
        extends ConstraintImplTestCaseAbstract {

    /**
     * Constructor
     * @param name The name of this test case.
     */
    public MaximumLengthConstraintImplTestCase(final String name) throws Exception {
        super(name);
    }

     // Javadoc inherited.
    protected MutableInhibitor getMutableInhibitor() {
        return new MutableMaximumLengthConstraintImpl();
    }

    // Javadoc inherited.
    protected ImmutableInhibitor getImmutableInhibitor() {
        return new ImmutableMaximumLengthConstraintImpl();
    }

    // Javadoc inherited.
    public void testEqualsAndHashcodeImplementedCorrectly() {

        MutableMaximumLengthConstraint maximumLengthConstraint1 =
            (MutableMaximumLengthConstraint) getMutableInhibitor();
        MutableMaximumLengthConstraint maximumLengthConstraint2 =
            (MutableMaximumLengthConstraint) getMutableInhibitor();

        // ensure that the two objects are equal
        assertEquals("Object 1 should  be equal to object 2",
            maximumLengthConstraint1, maximumLengthConstraint2);

        // ensure that they have the same hash code
        int hashCode1 = maximumLengthConstraint1.hashCode();
        int hashCode2 = maximumLengthConstraint2.hashCode();
        assertTrue("Objects which are equal should have the same hash codes. " +
                "Were : " + hashCode1 + " and " + hashCode2,
            hashCode1 == hashCode2);

        // now change the externally visible field and ensure that the two
        // objects are different
        maximumLengthConstraint2.setLimit(1);
        assertNotEquals(maximumLengthConstraint1, maximumLengthConstraint2);

        // see if the hashcodes are different
        hashCode1 = maximumLengthConstraint1.hashCode();
        hashCode2 = maximumLengthConstraint2.hashCode();
        assertFalse("Objects which are not equal should ideally not have the " +
                "same hash codes. Were : " + hashCode1 + " and " + hashCode2,
            hashCode1 == hashCode2);
    }
}
