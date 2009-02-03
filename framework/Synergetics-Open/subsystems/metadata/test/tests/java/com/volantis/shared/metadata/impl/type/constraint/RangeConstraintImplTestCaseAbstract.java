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

import com.volantis.shared.metadata.type.constraint.mutable.MutableRangeConstraint;

/**
 * Test case for {@link RangeConstraintImpl}.
 */
public abstract class RangeConstraintImplTestCaseAbstract extends ConstraintImplTestCaseAbstract {

    /**
     * Constructor
     * @param name The name of this test case.
     */
    public RangeConstraintImplTestCaseAbstract(String name) throws Exception {
        super(name);
    }

    // Javadoc inherited.
    public void testEqualsAndHashcodeImplementedCorrectly() {

        MutableRangeConstraint rangeConstraint1 =
                (MutableRangeConstraint) getMutableInhibitor();
        MutableRangeConstraint rangeConstraint2 =
                (MutableRangeConstraint) getMutableInhibitor();

        // ensure that the two objects are equal
        assertEquals("Object 1 should  be equal to object 2", rangeConstraint1, rangeConstraint2);

        // ensure that they have the same hash code
        int listValue1Hashcode = rangeConstraint1.hashCode();
        int listValue2Hashcode = rangeConstraint2.hashCode();
        assertTrue("Objects which are equal should have the same hash codes. Were : "
                + listValue1Hashcode + " and " + listValue2Hashcode,
                listValue1Hashcode == listValue2Hashcode);

        // now change a single externally visible field and ensure that the two
        // objects are different
        rangeConstraint2.setInclusive(true);
        assertNotEquals(rangeConstraint1, rangeConstraint2);

        // see if the hashcodes are different
        listValue1Hashcode = rangeConstraint1.hashCode();
        listValue2Hashcode = rangeConstraint2.hashCode();
        assertFalse("Objects which are not equal should ideally not have the same hash " +
                "codes. Were : " + listValue1Hashcode + " and " + listValue2Hashcode,
                listValue1Hashcode == listValue2Hashcode);

        // reset the object
        rangeConstraint2 =
                (MutableRangeConstraint) getMutableInhibitor();

        // now change another single externally visible field and ensure that they are
        // the two objects are different
        rangeConstraint2.setLimit(new Double(1));
        assertNotEquals(rangeConstraint1, rangeConstraint2);

        // see if the hashcodes are different
        listValue1Hashcode = rangeConstraint1.hashCode();
        listValue2Hashcode = rangeConstraint2.hashCode();
        assertFalse("Objects which are not equal should ideally not have the same hash " +
                "codes. Were : " + listValue1Hashcode + " and " + listValue2Hashcode,
                listValue1Hashcode == listValue2Hashcode);

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jan-05	6560/3	tom	VBM:2004122401 Added Inhibitor base class

 14-Jan-05	6560/1	tom	VBM:2004122401 Completed Metadata API Implementation

 13-Jan-05	6560/1	tom	VBM:2004122401 More Metadata API implementation

 ===========================================================================
*/
