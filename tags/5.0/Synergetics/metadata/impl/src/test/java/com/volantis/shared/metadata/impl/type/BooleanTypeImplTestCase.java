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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.shared.metadata.impl.type;

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.metadata.MetaDataFactory;
import com.volantis.shared.metadata.type.VerificationError;
import com.volantis.shared.metadata.type.constraint.mutable.MutableEnumeratedConstraint;
import com.volantis.shared.metadata.type.mutable.MutableBooleanType;
import com.volantis.shared.metadata.value.StringValue;
import com.volantis.shared.metadata.value.mutable.MutableBooleanValue;

import java.util.Collection;
import java.util.List;

/**
 * Test case for implementations of <code>BooleanType</code>.
 */
public class BooleanTypeImplTestCase extends MetaDataTypeImplTestCaseAbstract {

    // Javadoc inherited.
    public BooleanTypeImplTestCase(String name) throws Exception {
        super(name);
    }

    // Javadoc inherited.
    protected MutableInhibitor getMutableInhibitor() {
        return new MutableBooleanTypeImpl();
    }

    // Javadoc inherited.
    protected ImmutableInhibitor getImmutableInhibitor() {
        return new ImmutableBooleanTypeImpl();
    }

    // Javadoc inherited.
    public void testEqualsAndHashcodeImplementedCorrectly() {
        MutableBooleanType booleanType1 =
                createBooleanTypeForTestEquals();
        MutableBooleanType booleanType2 =
                createBooleanTypeForTestEquals();

        // test that both the values are equal and they have the same hash codes
        assertEquals("Object 1 should be equal to object 2", booleanType1, booleanType2);
        assertEquals("Object 2 should be equal to object 1", booleanType2, booleanType1);

        int booleanType1Hashcode = booleanType1.hashCode();
        int booleanType2Hashcode = booleanType2.hashCode();
        assertTrue("Objects which are equal should have the same hash codes. Were : "
                + booleanType1Hashcode + " and " + booleanType2Hashcode,
                booleanType1Hashcode == booleanType2Hashcode);

        // change a property in one of the objects and ensure they are not equal
        booleanType2.setEnumeratedConstraint(null);

        assertNotEquals(booleanType1, booleanType2);
        assertNotEquals(booleanType2, booleanType1);

        // ensure that the hashcodes are different for these two objects
        booleanType1Hashcode = booleanType1.hashCode();
        booleanType2Hashcode = booleanType2.hashCode();
        assertFalse("Objects which are not equal should ideally have different hash codes",
                booleanType1Hashcode == booleanType2Hashcode);
    }

    /**
     * Helper method which creates a <code>MutableBooleanType</code> which can be used for
     * testing.
     * @return a mutable boolean type.
     */
    private MutableBooleanType createBooleanTypeForTestEquals() {
        MutableBooleanTypeImpl mutableBooleanType = new MutableBooleanTypeImpl();

        MutableEnumeratedConstraint enumeratedConstraint =
                MetaDataFactory.getDefaultInstance().getTypeFactory().
                getConstraintFactory().createEnumeratedConstraint();

        MutableBooleanValue booleanValue = MetaDataFactory.getDefaultInstance().
                getValueFactory().createBooleanValue();

        booleanValue.setValue(Boolean.TRUE);
        enumeratedConstraint.getMutableEnumeratedValues().add(booleanValue);
        mutableBooleanType.setEnumeratedConstraint(enumeratedConstraint);
        return mutableBooleanType;
    }

    public void testVerify() {
        final MutableBooleanType booleanType = TYPE_FACTORY.createBooleanType();

        // normal case
        MutableBooleanValue booleanValue = VALUE_FACTORY.createBooleanValue();
        Collection errors = booleanType.verify(booleanValue);
        assertEquals(0, errors.size());

        // invalid type
        StringValue stringValue = VALUE_FACTORY.createStringValue();
        errors = booleanType.verify(stringValue);
        assertEquals(1, errors.size());
        VerificationError error = (VerificationError) errors.iterator().next();
        assertEquals(VerificationError.TYPE_INVALID_IMPLEMENTATION,
            error.getType());
        assertEquals("", error.getLocation());
        assertEquals(stringValue, error.getInvalidValue());
        assertNull(error.getConstraint());

        // enumerated constraint
        final MutableEnumeratedConstraint enumeratedConstraint =
            CONSTRAINT_FACTORY.createEnumeratedConstraint();
        final List enumeratedValues =
            enumeratedConstraint.getMutableEnumeratedValues();
        final MutableBooleanValue constraintValue =
            VALUE_FACTORY.createBooleanValue();
        constraintValue.setValue(Boolean.FALSE);
        enumeratedValues.add(constraintValue);
        booleanType.setEnumeratedConstraint(enumeratedConstraint);
        // check constraint with right value
        booleanValue.setValue(Boolean.FALSE);
        errors = booleanType.verify(booleanValue);
        assertEquals(0, errors.size());
        // check constraint violation
        booleanValue.setValue(Boolean.TRUE);
        errors = booleanType.verify(booleanValue);
        assertEquals(1, errors.size());
        error = (VerificationError) errors.iterator().next();
        assertEquals(VerificationError.TYPE_CONSTRAINT_VIOLATION,
            error.getType());
        assertEquals("", error.getLocation());
        assertEquals(booleanValue, error.getInvalidValue());
        assertEquals(enumeratedConstraint, error.getConstraint());
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
