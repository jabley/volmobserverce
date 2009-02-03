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

package com.volantis.shared.metadata.impl.type;

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.metadata.MetaDataFactory;
import com.volantis.shared.metadata.type.constraint.mutable.MutableMemberTypeConstraint;
import com.volantis.shared.metadata.value.mutable.MutableCollectionValue;

/**
 * Test case for {@link SetTypeImpl}.
 */
public class SetTypeImplTestCase extends CollectionTypeImplTestCaseAbstract {

    /**
     * Constructor.
     * @param name The name of this test case.
     */
    public SetTypeImplTestCase(String name) throws Exception {
        super(name);
    }

    // Javadoc inherited.
    protected MutableInhibitor getMutableInhibitor() {
        return new MutableSetTypeImpl();
    }

    // Javadoc inherited.
    protected ImmutableInhibitor getImmutableInhibitor() {
        return new ImmutableSetTypeImpl();
    }

    // Javadoc inherited.
    public void testEqualsAndHashcodeImplementedCorrectly() {
        MutableSetTypeImpl setType1 = createSetTypeForTests();
        MutableSetTypeImpl setType2 = createSetTypeForTests();

        // ensure that the two objects are equal
        assertEquals("Object 1 should  be equal to object 2", setType1, setType2);
        assertEquals("Object 2 should  be equal to object 1", setType1, setType2);

        // ensure that they have the same hash code
        int listValue1Hashcode = setType1.hashCode();
        int listValue2Hashcode = setType2.hashCode();
        assertTrue("Objects which are equal should have the same hash codes. Were : "
                + listValue1Hashcode + " and " + listValue2Hashcode,
                listValue1Hashcode == listValue2Hashcode);

        // now change a single item in each list and ensure that they are different
        setType2.setMemberTypeConstraint(null);
        assertNotEquals(setType1, setType2);

        // see if the hashcodes are different
        listValue1Hashcode = setType1.hashCode();
        listValue2Hashcode = setType2.hashCode();
        assertFalse("Objects which are not equal should ideally not have the same hash " +
                "codes. Were : " + listValue1Hashcode + " and " + listValue2Hashcode,
                listValue1Hashcode == listValue2Hashcode);
    }

    /**
     * Helper method which creates a <code>MutableSetTypeImpl</code> which has a constraint
     * and can be used for testing.
     * @return a mutable set type.
     */
    private MutableSetTypeImpl createSetTypeForTests() {
        MutableSetTypeImpl setType = new MutableSetTypeImpl();
        MutableMemberTypeConstraint constraint = MetaDataFactory.getDefaultInstance().
                getTypeFactory().getConstraintFactory().createMemberTypeConstraint();
        MutableBooleanTypeImpl booleanType = new MutableBooleanTypeImpl();
        constraint.setMemberType(booleanType);
        setType.setMemberTypeConstraint(constraint);
        return setType;
    }

    // javadoc inherited
    protected MutableCollectionValue createEmptyValue() {
        return VALUE_FACTORY.createSetValue();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jan-05	6560/3	tom	VBM:2004122401 Added Inhibitor base class

 10-Jan-05	6560/1	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
