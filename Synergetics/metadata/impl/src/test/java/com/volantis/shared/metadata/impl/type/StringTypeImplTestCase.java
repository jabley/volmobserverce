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
import com.volantis.shared.metadata.impl.MetaDataTestCaseHelper;
import com.volantis.shared.metadata.type.VerificationError;
import com.volantis.shared.metadata.type.constraint.mutable.MutableEnumeratedConstraint;
import com.volantis.shared.metadata.type.constraint.mutable.MutableMinimumLengthConstraint;
import com.volantis.shared.metadata.type.constraint.mutable.MutableMaximumLengthConstraint;
import com.volantis.shared.metadata.type.mutable.MutableStringType;
import com.volantis.shared.metadata.value.BooleanValue;
import com.volantis.shared.metadata.value.mutable.MutableBooleanValue;
import com.volantis.shared.metadata.value.mutable.MutableStringValue;

import java.util.Collection;
import java.util.List;
import java.util.Iterator;

/**
 * Test case for {@link com.volantis.shared.metadata.impl.type.StringTypeImpl}.
 */
public class StringTypeImplTestCase extends MetaDataTypeImplTestCaseAbstract {

    // Javadoc inherited.
    public StringTypeImplTestCase(String name) throws Exception {
        super(name);
    }

    // Javadoc inherited.
    protected MutableInhibitor getMutableInhibitor() {
        return new MutableStringTypeImpl();
    }

    // Javadoc inherited.
    protected ImmutableInhibitor getImmutableInhibitor() {
        return new ImmutableStringTypeImpl();
    }

    // Javadoc inherited.
    public void testEqualsAndHashcodeImplementedCorrectly() {
        MutableStringType stringType1 =
                createStringTypeForTestEquals();
        MutableStringType stringType2 =
                createStringTypeForTestEquals();

        // test that both the values are equal and they have the same hash codes
        assertEquals("Object 1 should be equal to object 2", stringType1, stringType2);
        MetaDataTestCaseHelper.ensureHashcodesEqual(stringType1, stringType2);

        // now null the constraints and check that the objects are no longer equal
        // and ideally have different hashcodes
        stringType2.setEnumeratedConstraint(null);

        assertNotSame(stringType1, stringType2);
        MetaDataTestCaseHelper.ensureHashcodesNotEqual(stringType1, stringType2);

    }

    /**
     * Helper method which creates a <code>MutableBooleanType</code> which can be used for
     * testing.
     * @return a mutable boolean type.
     */
    private MutableStringType createStringTypeForTestEquals() {
        MutableStringTypeImpl mutableBooleanType = new MutableStringTypeImpl();

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
        final MutableStringType stringType = TYPE_FACTORY.createStringType();

        // normal case
        MutableStringValue stringValue = VALUE_FACTORY.createStringValue();
        stringValue.setValue("Foo");
        Collection errors = stringType.verify(stringValue);
        assertEquals(0, errors.size());

        // null value
        stringValue = VALUE_FACTORY.createStringValue();
        errors = stringType.verify(stringValue);
        assertEquals(1, errors.size());
        VerificationError error =
            (VerificationError) errors.iterator().next();
        assertEquals(VerificationError.TYPE_NULL_VALUE,
            error.getType());
        assertEquals("", error.getLocation());
        assertEquals(stringValue, error.getInvalidValue());
        assertNull(error.getConstraint());

        // invalid type
        BooleanValue booleanValue = VALUE_FACTORY.createBooleanValue();
        errors = stringType.verify(booleanValue);
        assertEquals(1, errors.size());
        error = (VerificationError) errors.iterator().next();
        assertEquals(VerificationError.TYPE_INVALID_IMPLEMENTATION,
            error.getType());
        assertEquals("", error.getLocation());
        assertEquals(booleanValue, error.getInvalidValue());
        assertNull(error.getConstraint());

        // enumerated constraint
        final MutableEnumeratedConstraint enumeratedConstraint =
            CONSTRAINT_FACTORY.createEnumeratedConstraint();
        final List enumeratedValues =
            enumeratedConstraint.getMutableEnumeratedValues();
        final MutableStringValue constraintValue =
            VALUE_FACTORY.createStringValue();
        constraintValue.setValue("one");
        enumeratedValues.add(constraintValue.createImmutable());
        constraintValue.setValue("two");
        enumeratedValues.add(constraintValue.createImmutable());
        stringType.setEnumeratedConstraint(enumeratedConstraint);
        // check constraint with right value
        stringValue.setValue("two");
        errors = stringType.verify(stringValue);
        assertEquals(0, errors.size());
        // check constraint violation
        stringValue.setValue("three");
        errors = stringType.verify(stringValue);
        assertEquals(1, errors.size());
        error = (VerificationError) errors.iterator().next();
        assertEquals(VerificationError.TYPE_CONSTRAINT_VIOLATION,
            error.getType());
        assertEquals("", error.getLocation());
        assertEquals(stringValue, error.getInvalidValue());
        assertEquals(enumeratedConstraint, error.getConstraint());
    }

    public void testVerifyMinimumLengthConstraint() {

        final MutableStringType stringType =
            (MutableStringType) getMutableInhibitor();

        final MutableMinimumLengthConstraint minimumLengthConstraint =
            CONSTRAINT_FACTORY.createMinimumLengthConstraint();
        minimumLengthConstraint.setLimit(5);
        stringType.setMinimumLengthConstraint(minimumLengthConstraint);

        // check constraint with right value
        final MutableStringValue stringValue = VALUE_FACTORY.createStringValue();
        stringValue.setValue("hello");
        Collection errors = stringType.verify(stringValue);
        assertEquals(0, errors.size());

        // check constraint violation
        minimumLengthConstraint.setLimit(6);
        stringType.setMinimumLengthConstraint(minimumLengthConstraint);
        errors = stringType.verify(stringValue);
        assertEquals(1, errors.size());
        final Iterator iter = errors.iterator();
        VerificationError error = (VerificationError) iter.next();
        assertEquals(VerificationError.TYPE_CONSTRAINT_VIOLATION,
            error.getType());
        assertEquals("", error.getLocation());
        assertEquals(stringValue, error.getInvalidValue());
        assertEquals(minimumLengthConstraint, error.getConstraint());
    }

    public void testVerifyMaximumLengthConstraint() {

        final MutableStringType stringType =
            (MutableStringType) getMutableInhibitor();

        final MutableMaximumLengthConstraint maximumLengthConstraint =
            CONSTRAINT_FACTORY.createMaximumLengthConstraint();
        maximumLengthConstraint.setLimit(5);
        stringType.setMaximumLengthConstraint(maximumLengthConstraint);

        // check constraint with right value
        final MutableStringValue stringValue = VALUE_FACTORY.createStringValue();
        stringValue.setValue("hello");
        Collection errors = stringType.verify(stringValue);
        assertEquals(0, errors.size());

        // check constraint violation
        maximumLengthConstraint.setLimit(4);
        stringType.setMaximumLengthConstraint(maximumLengthConstraint);
        errors = stringType.verify(stringValue);
        assertEquals(1, errors.size());
        final Iterator iter = errors.iterator();
        VerificationError error = (VerificationError) iter.next();
        assertEquals(VerificationError.TYPE_CONSTRAINT_VIOLATION,
            error.getType());
        assertEquals("", error.getLocation());
        assertEquals(stringValue, error.getInvalidValue());
        assertEquals(maximumLengthConstraint, error.getConstraint());
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
