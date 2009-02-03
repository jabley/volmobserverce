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

import com.volantis.shared.metadata.MetaDataFactory;
import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.metadata.impl.MetaDataTestCaseHelper;
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.metadata.type.constraint.ConstraintFactory;
import com.volantis.shared.metadata.type.constraint.NumberSubTypeConstraint;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableNumberSubTypeConstraint;
import com.volantis.shared.metadata.type.constraint.mutable.MutableEnumeratedConstraint;
import com.volantis.shared.metadata.type.constraint.mutable.MutableMaximumValueConstraint;
import com.volantis.shared.metadata.type.constraint.mutable.MutableMinimumValueConstraint;
import com.volantis.shared.metadata.type.mutable.MutableNumberType;
import com.volantis.shared.metadata.type.VerificationError;
import com.volantis.shared.metadata.value.mutable.MutableBooleanValue;
import com.volantis.shared.metadata.value.mutable.MutableNumberValue;
import com.volantis.shared.metadata.value.BooleanValue;

import java.util.Collection;
import java.util.List;

/**
 * Test case for {@link NumberTypeImpl}.
 */
public class NumberTypeImplTestCase extends SimpleTypeImplTestCaseAbstract {

    /**
     * Constructor
     * @param name The name of this test.
     */
    public NumberTypeImplTestCase(String name) throws Exception {
        super(name);
    }

    // Javadoc inherited.
    protected MutableInhibitor getMutableInhibitor() {
        return new MutableNumberTypeImpl();
    }

    // Javadoc inherited.
    protected ImmutableInhibitor getImmutableInhibitor() {
        return new ImmutableNumberTypeImpl();
    }

    // Javadoc inherited.
    public void testEqualsAndHashcodeImplementedCorrectly() {
        MutableNumberType numberType1 = createNumberTypeForTestingEquals();
        MutableNumberType numberType2 = createNumberTypeForTestingEquals();

        // test that both the values are equal and they have the same hash codes
        assertEquals("Object 1 should be equal to object 2", numberType1, numberType2);
        MetaDataTestCaseHelper.ensureHashcodesEqual(numberType1, numberType2);

        // change a property in one of the objects and ensure they are not equal and
        // ideally have different hashcodes
        // todo add test for enumerated constraint - should actually go into superclass

        numberType2.setNumberSubTypeConstraint(null);
        assertNotEquals(numberType1, numberType2);

        MetaDataTestCaseHelper.ensureHashcodesNotEqual(numberType1, numberType2);

        // Reset the object
        numberType2 = createNumberTypeForTestingEquals();

        // Now test changing the number sub type constraint make the objects and their
        // hashcodes not equal
        NumberSubTypeConstraint numberSubTypeConstraint = MetaDataFactory.
                getDefaultInstance().
                getTypeFactory().getConstraintFactory().
                getNumberSubTypeConstraint(Integer.class);

        numberType2.setNumberSubTypeConstraint(numberSubTypeConstraint);

        assertNotEquals(numberType1, numberType2);
        MetaDataTestCaseHelper.ensureHashcodesNotEqual(numberType1, numberType2);

        // Reset the object
        numberType2 = createNumberTypeForTestingEquals();

        // Now test changing the minimum value constraint makes the objects and their
        // hashcodes not equal
        numberType2.setMinimumValueConstraint(null);

        assertNotEquals(numberType1, numberType2);
        MetaDataTestCaseHelper.ensureHashcodesNotEqual(numberType1, numberType2);

        // Reset the object
        numberType2 = createNumberTypeForTestingEquals();

        // Now test changing the maximum value constraint makes the objects and their
        // hashcodes not equal
        numberType2.setMaximumValueConstraint(null);

        assertNotEquals(numberType1, numberType2);
        MetaDataTestCaseHelper.ensureHashcodesNotEqual(numberType1, numberType2);
    }

    /**
     * Helper method which creates a <code>MutableBooleanType</code> which can be used for
     * testing.
     * @return a mutable number type.
     */
    private MutableNumberType createNumberTypeForTestingEquals() {
        MutableNumberTypeImpl mutableNumberType = new MutableNumberTypeImpl();

        // Set an enumerated constraint on this object
        MutableEnumeratedConstraint enumeratedConstraint =
                MetaDataFactory.getDefaultInstance().getTypeFactory().
                getConstraintFactory().createEnumeratedConstraint();

        MutableBooleanValue booleanValue = MetaDataFactory.getDefaultInstance().
                getValueFactory().createBooleanValue();

        booleanValue.setValue(Boolean.TRUE);
        enumeratedConstraint.getMutableEnumeratedValues().add(booleanValue);
        mutableNumberType.setEnumeratedConstraint(enumeratedConstraint);

        ConstraintFactory constraintFactory = MetaDataFactory.getDefaultInstance().
                getTypeFactory().getConstraintFactory();

        // Set the number sub type on this object to Double
        NumberSubTypeConstraint numberSubTypeConstraint = constraintFactory.
                getNumberSubTypeConstraint(Double.class);
        mutableNumberType.setNumberSubTypeConstraint(numberSubTypeConstraint);

        // Set the minimum value to 1.0
        MutableMinimumValueConstraint minimumValue =
                constraintFactory.createMinimumValueConstraint();
        minimumValue.setLimit(new Double(1.0));
        mutableNumberType.setMinimumValueConstraint(minimumValue);

        // Set the maximum value to 2.0
        MutableMaximumValueConstraint maximumValue =
                constraintFactory.createMaximumValueConstraint();
        maximumValue.setLimit(new Double(2.0));
        mutableNumberType.setMaximumValueConstraint(maximumValue);

        return mutableNumberType;
    }

    public void testVerify() {
        final MutableNumberType numberType = TYPE_FACTORY.createNumberType();

        // normal case
        MutableNumberValue numberValue = VALUE_FACTORY.createNumberValue();
        numberValue.setValue(new Integer(5));
        Collection errors = numberType.verify(numberValue);
        assertEquals(0, errors.size());

        // null value
        numberValue = VALUE_FACTORY.createNumberValue();
        errors = numberType.verify(numberValue);
        assertEquals(1, errors.size());
        VerificationError error =
            (VerificationError) errors.iterator().next();
        assertEquals(VerificationError.TYPE_NULL_VALUE,
            error.getType());
        assertEquals("", error.getLocation());
        assertEquals(numberValue, error.getInvalidValue());
        assertNull(error.getConstraint());

        // invalid type
        BooleanValue booleanValue = VALUE_FACTORY.createBooleanValue();
        errors = numberType.verify(booleanValue);
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
        final MutableNumberValue constraintValue =
            VALUE_FACTORY.createNumberValue();
        constraintValue.setValue(new Integer(6));
        enumeratedValues.add(constraintValue.createImmutable());
        constraintValue.setValue(new Integer(7));
        enumeratedValues.add(constraintValue.createImmutable());
        numberType.setEnumeratedConstraint(enumeratedConstraint);
        // check constraint with right value
        numberValue.setValue(new Integer(6));
        errors = numberType.verify(numberValue);
        assertEquals(0, errors.size());
        // check constraint violation
        numberValue.setValue(new Integer(8));
        errors = numberType.verify(numberValue);
        assertEquals(1, errors.size());
        error = (VerificationError) errors.iterator().next();
        assertEquals(VerificationError.TYPE_CONSTRAINT_VIOLATION,
            error.getType());
        assertEquals("", error.getLocation());
        assertEquals(numberValue, error.getInvalidValue());
        assertEquals(enumeratedConstraint, error.getConstraint());
        numberType.setEnumeratedConstraint(null);

        // sub type constraint
        final ImmutableNumberSubTypeConstraint subTypeConstraint =
            CONSTRAINT_FACTORY.getNumberSubTypeConstraint(Integer.class);
        numberType.setNumberSubTypeConstraint(subTypeConstraint);
        // check constraint with right value
        numberValue.setValue(new Integer(6));
        errors = numberType.verify(numberValue);
        assertEquals(0, errors.size());
        // check constraint violation
        numberValue.setValue(new Long(8));
        errors = numberType.verify(numberValue);
        assertEquals(1, errors.size());
        error = (VerificationError) errors.iterator().next();
        assertEquals(VerificationError.TYPE_CONSTRAINT_VIOLATION,
            error.getType());
        assertEquals("", error.getLocation());
        assertEquals(numberValue, error.getInvalidValue());
        assertEquals(subTypeConstraint, error.getConstraint());
        numberType.setNumberSubTypeConstraint(null);

        // minimum value constraint with inclusive set to true
        final MutableMinimumValueConstraint minValueConstraint =
            CONSTRAINT_FACTORY.createMinimumValueConstraint();
        minValueConstraint.setLimit(new Integer(6));
        minValueConstraint.setInclusive(true);
        numberType.setMinimumValueConstraint(minValueConstraint);
        // check constraint with right value
        numberValue.setValue(new Integer(6));
        errors = numberType.verify(numberValue);
        assertEquals(0, errors.size());
        // check constraint violation
        numberValue.setValue(new Integer(5));
        errors = numberType.verify(numberValue);
        assertEquals(1, errors.size());
        error = (VerificationError) errors.iterator().next();
        assertEquals(VerificationError.TYPE_CONSTRAINT_VIOLATION,
            error.getType());
        assertEquals("", error.getLocation());
        assertEquals(numberValue, error.getInvalidValue());
        assertEquals(minValueConstraint, error.getConstraint());
        numberType.setEnumeratedConstraint(null);

        // minimum value constraint with inclusive set to false
        minValueConstraint.setInclusive(false);
        numberType.setMinimumValueConstraint(minValueConstraint);
        // check constraint with right value
        numberValue.setValue(new Integer(7));
        errors = numberType.verify(numberValue);
        assertEquals(0, errors.size());
        // check constraint violation
        numberValue.setValue(new Integer(6));
        errors = numberType.verify(numberValue);
        assertEquals(1, errors.size());
        error = (VerificationError) errors.iterator().next();
        assertEquals(VerificationError.TYPE_CONSTRAINT_VIOLATION,
            error.getType());
        assertEquals("", error.getLocation());
        assertEquals(numberValue, error.getInvalidValue());
        assertEquals(minValueConstraint, error.getConstraint());
        numberType.setMinimumValueConstraint(null);

        // maximum value constraint with inclusive set to true
        final MutableMaximumValueConstraint maxValueConstraint =
            CONSTRAINT_FACTORY.createMaximumValueConstraint();
        maxValueConstraint.setLimit(new Integer(6));
        maxValueConstraint.setInclusive(true);
        numberType.setMaximumValueConstraint(maxValueConstraint);
        // check constraint with right value
        numberValue.setValue(new Integer(6));
        errors = numberType.verify(numberValue);
        assertEquals(0, errors.size());
        // check constraint violation
        numberValue.setValue(new Integer(7));
        errors = numberType.verify(numberValue);
        assertEquals(1, errors.size());
        error = (VerificationError) errors.iterator().next();
        assertEquals(VerificationError.TYPE_CONSTRAINT_VIOLATION,
            error.getType());
        assertEquals("", error.getLocation());
        assertEquals(numberValue, error.getInvalidValue());
        assertEquals(maxValueConstraint, error.getConstraint());
        numberType.setEnumeratedConstraint(null);

        // maximum value constraint with inclusive set to false
        maxValueConstraint.setInclusive(false);
        numberType.setMaximumValueConstraint(maxValueConstraint);
        // check constraint with right value
        numberValue.setValue(new Integer(5));
        errors = numberType.verify(numberValue);
        assertEquals(0, errors.size());
        // check constraint violation
        numberValue.setValue(new Integer(6));
        errors = numberType.verify(numberValue);
        assertEquals(1, errors.size());
        error = (VerificationError) errors.iterator().next();
        assertEquals(VerificationError.TYPE_CONSTRAINT_VIOLATION,
            error.getType());
        assertEquals("", error.getLocation());
        assertEquals(numberValue, error.getInvalidValue());
        assertEquals(maxValueConstraint, error.getConstraint());
        numberType.setEnumeratedConstraint(null);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jan-05	6560/5	tom	VBM:2004122401 Added Inhibitor base class

 14-Jan-05	6560/3	tom	VBM:2004122401 Completed Metadata API Implementation

 13-Jan-05	6560/1	tom	VBM:2004122401 More Metadata API implementation

 ===========================================================================
*/
