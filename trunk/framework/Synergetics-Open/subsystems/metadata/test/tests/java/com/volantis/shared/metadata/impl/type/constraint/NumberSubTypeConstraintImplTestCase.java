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

package com.volantis.shared.metadata.impl.type.constraint;

import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.metadata.type.constraint.mutable.MutableNumberSubTypeConstraint;

/**
 * Test case for {@link NumberSubTypeConstraintImpl}.
 */
public class NumberSubTypeConstraintImplTestCase extends ConstraintImplTestCaseAbstract {

    /**
     * Constructor
     * @param name The name of this test case.
     */
    public NumberSubTypeConstraintImplTestCase(String name) throws Exception {
        super(name);
    }

    // Javadoc inherited.
    protected MutableInhibitor getMutableInhibitor() {
        return new MutableNumberSubTypeConstraintImpl();
    }

    // Javadoc inherited.
    protected ImmutableInhibitor getImmutableInhibitor() {
        return new ImmutableNumberSubTypeConstraintImpl();
    }

    // Javadoc inherited.
    public void testEqualsAndHashcodeImplementedCorrectly() {

         MutableNumberSubTypeConstraint subTypeConstraint1 =
                createMutableNumberSubTypeConstraint();
        MutableNumberSubTypeConstraint subTypeConstraint2 =
                createMutableNumberSubTypeConstraint();

        // test that both the values are equal and they have the same hash codes
        assertEquals("Object 1 should be equal to object 2", subTypeConstraint1,
                subTypeConstraint2);

        int subTypeConstraint1Hashcode = subTypeConstraint1.hashCode();
        int subTypeConstraint2Hashcode = subTypeConstraint2.hashCode();
        assertTrue("Objects which are equal should have the same hash codes. Were : "
                + subTypeConstraint1Hashcode + " and " + subTypeConstraint2Hashcode,
                subTypeConstraint1Hashcode == subTypeConstraint2Hashcode);

        subTypeConstraint2.setNumberSubType(Integer.class);

        assertNotEquals(subTypeConstraint1, subTypeConstraint2);
        assertNotEquals(subTypeConstraint2, subTypeConstraint1);

        // ensure that the hashcodes are different for these two objects
        subTypeConstraint1Hashcode = subTypeConstraint1.hashCode();
        subTypeConstraint2Hashcode = subTypeConstraint2.hashCode();
        assertFalse("Objects which are not equal should ideally have different hash codes",
                subTypeConstraint1Hashcode == subTypeConstraint2Hashcode);

    }

    /**
     * Helper method which creates a <code>MutableNumberSubTypeConstraint</code> which has
     * a the sub class set as the default - Number.class.
     * @return a mutable enumerated constraint.
     */
    private MutableNumberSubTypeConstraint createMutableNumberSubTypeConstraint() {
       return new MutableNumberSubTypeConstraintImpl();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jan-05	6560/3	tom	VBM:2004122401 Added Inhibitor base class

 13-Jan-05	6560/1	tom	VBM:2004122401 More Metadata API implementation

 ===========================================================================
*/
