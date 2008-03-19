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
import com.volantis.shared.metadata.impl.MetaDataTestCaseHelper;
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.metadata.type.NumberType;
import com.volantis.shared.metadata.type.UnitType;
import com.volantis.shared.metadata.type.MetaDataTypeFactory;
import com.volantis.shared.metadata.type.VerificationError;
import com.volantis.shared.metadata.type.constraint.mutable.MutableEnumeratedConstraint;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableNumberSubTypeConstraint;
import com.volantis.shared.metadata.type.mutable.MutableQuantityType;
import com.volantis.shared.metadata.type.mutable.MutableNumberType;
import com.volantis.shared.metadata.type.mutable.MutableUnitType;
import com.volantis.shared.metadata.MetaDataFactory;
import com.volantis.shared.metadata.value.mutable.MutableQuantityValue;
import com.volantis.shared.metadata.value.mutable.MutableNumberValue;
import com.volantis.shared.metadata.value.BooleanValue;
import com.volantis.shared.metadata.value.QuantityUnits;

import java.util.Collection;
import java.util.List;

/**
 * Test case for {@link QuantityTypeImpl}.
 */
public class QuantityTypeImplTestCase extends MetaDataTypeImplTestCaseAbstract {

    /**
     * Constructor
     * @param name The name of this test.
     */
    public QuantityTypeImplTestCase(String name) throws Exception {
        super(name);
    }

    // Javadoc inherited.
    protected MutableInhibitor getMutableInhibitor() {
        return new MutableQuantityTypeImpl();
    }

    // Javadoc inherited.
    protected ImmutableInhibitor getImmutableInhibitor() {
        return new ImmutableQuantityTypeImpl();
    }

    // Javadoc inherited.
    public void testEqualsAndHashcodeImplementedCorrectly() {
        MutableQuantityType fieldDefinition1 = createMutableUnitTypeForTestingEquals();
        MutableQuantityType fieldDefinition2 = createMutableUnitTypeForTestingEquals();

        // test that both the values are equal and they have the same hash codes
        assertEquals("Object 1 should be equal to object 2", fieldDefinition1,
                fieldDefinition2);
        MetaDataTestCaseHelper.ensureHashcodesEqual(fieldDefinition1, fieldDefinition2);

        // change the magnitude
        fieldDefinition2.setMagnitudeType(null);
        MetaDataTestCaseHelper.assertNotEquals(fieldDefinition1, fieldDefinition2);
        MetaDataTestCaseHelper.ensureHashcodesNotEqual(fieldDefinition1, fieldDefinition2);

        // now reset
        fieldDefinition2 = createMutableUnitTypeForTestingEquals();

        // change the type
        fieldDefinition2.setUnitType(null);
        MetaDataTestCaseHelper.assertNotEquals(fieldDefinition1, fieldDefinition2);
        MetaDataTestCaseHelper.ensureHashcodesNotEqual(fieldDefinition1, fieldDefinition2);
    }

    /**
     * Helper method which creates a <code>MutableUnitType</code> which can be used for
     * testing.
     * @return a mutable unit type.
     */
    private MutableQuantityType createMutableUnitTypeForTestingEquals() {
        MutableQuantityType mutableQuantityType = new MutableQuantityTypeImpl();

        MetaDataTypeFactory factory = MetaDataFactory.getDefaultInstance().
                getTypeFactory();

        NumberType numberType = factory.createNumberType();
        mutableQuantityType.setMagnitudeType( numberType );

        UnitType unitType = factory.createUnitType();
        mutableQuantityType.setUnitType(unitType);

        return mutableQuantityType;
    }


    public void testVerify() {
        // create the test type
        final MutableQuantityType quantityType =
            TYPE_FACTORY.createQuantityType();

        final MutableNumberType numberType = TYPE_FACTORY.createNumberType();
        final ImmutableNumberSubTypeConstraint subTypeConstraint =
            CONSTRAINT_FACTORY.getNumberSubTypeConstraint(Integer.class);
        numberType.setNumberSubTypeConstraint(subTypeConstraint);
        quantityType.setMagnitudeType(numberType);

        final MutableUnitType unitType = TYPE_FACTORY.createUnitType();
        final MutableEnumeratedConstraint enumeratedConstraint =
            CONSTRAINT_FACTORY.createEnumeratedConstraint();
        final List enumeratedValues =
            enumeratedConstraint.getMutableEnumeratedValues();
        enumeratedValues.add(QuantityUnits.MILLIMETERS);
        enumeratedValues.add(QuantityUnits.CENTIMETERS);
        enumeratedValues.add(QuantityUnits.INCHES);
        unitType.setEnumeratedConstraint(enumeratedConstraint);
        quantityType.setUnitType(unitType);

        // normal case
        MutableQuantityValue quantityValue = VALUE_FACTORY.createQuantityValue();
        final MutableNumberValue magnitudeValue =
            VALUE_FACTORY.createNumberValue();
        magnitudeValue.setValue(new Integer(123));
        quantityValue.setMagnitudeValue(magnitudeValue);
        quantityValue.setUnitValue(QuantityUnits.CENTIMETERS);
        Collection errors = quantityType.verify(quantityValue);
        assertEquals(0, errors.size());

        // invalid magnitude type
        magnitudeValue.setValue(new Byte((byte) 42));
        quantityValue.setMagnitudeValue(magnitudeValue);
        errors = quantityType.verify(quantityValue);
        assertEquals(1, errors.size());
        VerificationError error = (VerificationError) errors.iterator().next();
        assertEquals(VerificationError.TYPE_CONSTRAINT_VIOLATION,
            error.getType());
        assertEquals("[@magnitude]", error.getLocation());
        assertEquals(magnitudeValue, error.getInvalidValue());
        assertEquals(subTypeConstraint, error.getConstraint());

        // invalid unit type
        magnitudeValue.setValue(new Integer(42));
        quantityValue.setMagnitudeValue(magnitudeValue);
        quantityValue.setUnitValue(QuantityUnits.PIXELS);
        errors = quantityType.verify(quantityValue);
        assertEquals(1, errors.size());
        error = (VerificationError) errors.iterator().next();
        assertEquals(VerificationError.TYPE_CONSTRAINT_VIOLATION,
            error.getType());
        assertEquals("[@unit]", error.getLocation());
        assertEquals(QuantityUnits.PIXELS, error.getInvalidValue());
        assertEquals(enumeratedConstraint, error.getConstraint());

        // invalid type
        BooleanValue booleanValue = VALUE_FACTORY.createBooleanValue();
        errors = quantityType.verify(booleanValue);
        assertEquals(1, errors.size());
        error = (VerificationError) errors.iterator().next();
        assertEquals(VerificationError.TYPE_INVALID_IMPLEMENTATION,
            error.getType());
        assertEquals("", error.getLocation());
        assertEquals(booleanValue, error.getInvalidValue());
        assertNull(error.getConstraint());
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
