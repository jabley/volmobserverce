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

import com.volantis.shared.metadata.MetaDataFactory;
import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.metadata.type.BooleanType;

/**
 * Test case for {@link MemberTypeConstraintImpl}.
 */
public class MemberTypeConstraintImplTestCase extends ConstraintImplTestCaseAbstract {
    /**
     * Constructor
     * @param name The name of this test case.
     */
    public MemberTypeConstraintImplTestCase(String name) throws Exception {
        super(name);
    }

    // Javadoc inherited.
    protected MutableInhibitor getMutableInhibitor() {
        return new MutableMemberTypeConstraintImpl();
    }

    // Javadoc inherited.
    protected ImmutableInhibitor getImmutableInhibitor() {
        return new ImmutableMemberTypeConstraintImpl();
    }

    // Javadoc inherited.
    public void testEqualsAndHashcodeImplementedCorrectly() {

        MutableMemberTypeConstraintImpl memberTypeConstraint1 = createMutableMemberTypeConstraintImplForTests();
        MutableMemberTypeConstraintImpl memberTypeConstraint2 = createMutableMemberTypeConstraintImplForTests();

        // ensure that the two objects are equal
        assertEquals("Object 1 should  be equal to object 2", memberTypeConstraint1, memberTypeConstraint2);

        // ensure that they have the same hash code
        int listValue1Hashcode = memberTypeConstraint1.hashCode();
        int listValue2Hashcode = memberTypeConstraint2.hashCode();
        assertTrue("Objects which are equal should have the same hash codes. Were : "
                + listValue1Hashcode + " and " + listValue2Hashcode,
                listValue1Hashcode == listValue2Hashcode);

        // now change a single externall visible item and ensure the two objects are
        // different
        memberTypeConstraint2.setMemberType(null);
        assertNotEquals(memberTypeConstraint1, memberTypeConstraint2);

        // see if the hashcodes are different
        listValue1Hashcode = memberTypeConstraint1.hashCode();
        listValue2Hashcode = memberTypeConstraint2.hashCode();
        assertFalse("Objects which are not equal should ideally not have the same hash " +
                "codes. Were : " + listValue1Hashcode + " and " + listValue2Hashcode,
                listValue1Hashcode == listValue2Hashcode);
    }

    /**
     * Helper method for which creates a MutableMemberTypeConstraintImpl with its member
     * type constraint set to a {@link com.volantis.shared.metadata.type.BooleanType} -
     * (value is false).
     * @return a <code>MutableMemberTypeConstraintImpl</code>.
     */
    private MutableMemberTypeConstraintImpl createMutableMemberTypeConstraintImplForTests() {
        MutableMemberTypeConstraintImpl memberTypeConstraint
                = new MutableMemberTypeConstraintImpl();
        BooleanType booleanType = MetaDataFactory.getDefaultInstance().getTypeFactory().
                createBooleanType();
        memberTypeConstraint.setMemberType(booleanType);
        return memberTypeConstraint;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6670/5	adrianj	VBM:2005010506 Implementation of resource asset continued

 14-Jan-05	6560/7	tom	VBM:2004122401 Added Inhibitor base class

 14-Jan-05	6560/5	tom	VBM:2004122401 Completed Metadata API Implementation

 13-Jan-05	6560/3	tom	VBM:2004122401 More Metadata API implementation

 10-Jan-05	6560/1	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
