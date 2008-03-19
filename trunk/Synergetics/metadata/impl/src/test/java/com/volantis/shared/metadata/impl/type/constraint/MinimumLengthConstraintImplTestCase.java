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

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.metadata.type.constraint.mutable.MutableMinimumLengthConstraint;

/**
 * Test case for {@link MinimumLengthConstraintImpl}.
 */
public class MinimumLengthConstraintImplTestCase
        extends ConstraintImplTestCaseAbstract {

    /**
     * Constructor
     * @param name The name of this test case.
     */
    public MinimumLengthConstraintImplTestCase(final String name) throws Exception {
        super(name);
    }

     // Javadoc inherited.
    protected MutableInhibitor getMutableInhibitor() {
        return new MutableMinimumLengthConstraintImpl();
    }

    // Javadoc inherited.
    protected ImmutableInhibitor getImmutableInhibitor() {
        return new ImmutableMinimumLengthConstraintImpl();
    }

    // Javadoc inherited.
    public void testEqualsAndHashcodeImplementedCorrectly() {

        MutableMinimumLengthConstraint minimumLengthConstraint1 =
            (MutableMinimumLengthConstraint) getMutableInhibitor();
        MutableMinimumLengthConstraint minimumLengthConstraint2 =
            (MutableMinimumLengthConstraint) getMutableInhibitor();

        // ensure that the two objects are equal
        assertEquals("Object 1 should  be equal to object 2",
            minimumLengthConstraint1, minimumLengthConstraint2);

        // ensure that they have the same hash code
        int hashCode1 = minimumLengthConstraint1.hashCode();
        int hashCode2 = minimumLengthConstraint2.hashCode();
        assertTrue("Objects which are equal should have the same hash codes. " +
                "Were : " + hashCode1 + " and " + hashCode2,
            hashCode1 == hashCode2);

        // now change the externally visible field and ensure that the two
        // objects are different
        minimumLengthConstraint2.setLimit(1);
        assertNotEquals(minimumLengthConstraint1, minimumLengthConstraint2);

        // see if the hashcodes are different
        hashCode1 = minimumLengthConstraint1.hashCode();
        hashCode2 = minimumLengthConstraint2.hashCode();
        assertFalse("Objects which are not equal should ideally not have the " +
                "same hash codes. Were : " + hashCode1 + " and " + hashCode2,
            hashCode1 == hashCode2);
    }
}
