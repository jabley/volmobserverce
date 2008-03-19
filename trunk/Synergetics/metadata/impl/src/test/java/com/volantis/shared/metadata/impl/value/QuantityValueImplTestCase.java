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

package com.volantis.shared.metadata.impl.value;

import com.volantis.shared.metadata.value.MetaDataValueTestCaseAbstract;
import com.volantis.shared.metadata.value.QuantityUnits;
import com.volantis.shared.metadata.value.UnitValue;
import com.volantis.shared.metadata.value.mutable.MutableQuantityValue;
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.metadata.impl.MetaDataTestCaseHelper;

/**
 * Test case for {@link QuantityValueImpl}.
 */
public class QuantityValueImplTestCase extends MetaDataValueTestCaseAbstract{

    /**
     * Constructor
     * @param name The name of this test case.
     */
    public QuantityValueImplTestCase(String name) throws Exception {
        super(name);
    }

    // Javadoc inherited.
    protected MutableInhibitor getMutableInhibitor() {
        return new MutableQuantityValueImpl();
    }

    // Javadoc inherited.
    protected ImmutableInhibitor getImmutableInhibitor() {
        return new ImmutableQuantityValueImpl();
    }

     // Javadoc inherited.
    public void testEqualsAndHashcodeImplementedCorrectly() {

        MutableQuantityValue quantityValue1 = createMutableUnitTypeForTestingEquals();
        MutableQuantityValue quantityValue2 = createMutableUnitTypeForTestingEquals();

        // test that both the values are equal and they have the same hash codes
        assertEquals("Object 1 should be equal to object 2", quantityValue1,
                quantityValue2);
        MetaDataTestCaseHelper.ensureHashcodesEqual(quantityValue1, quantityValue2);

        // change the magnitude
        final MutableNumberValueImpl magnitudeValue =
            new MutableNumberValueImpl();
        magnitudeValue.setValue(new Integer(123));
        quantityValue2.setMagnitudeValue(magnitudeValue);
        MetaDataTestCaseHelper.assertNotEquals(quantityValue1, quantityValue2);
        MetaDataTestCaseHelper.ensureHashcodesNotEqual(quantityValue1, quantityValue2);

        // now reset
        quantityValue2 = createMutableUnitTypeForTestingEquals();

        // change the type
        magnitudeValue.setValue(new Long(123));
        quantityValue2.setMagnitudeValue(magnitudeValue);
        MetaDataTestCaseHelper.assertNotEquals(quantityValue1, quantityValue2);
        MetaDataTestCaseHelper.ensureHashcodesNotEqual(quantityValue1, quantityValue2);

    }

    /**
     * This tests the getMagnitudeValueAsNumber method.
     */
    public void testGetMagnitudeValueAsNumber() {
        MutableQuantityValue mutableQuantityValue = new MutableQuantityValueImpl();

        NumberValueImpl numberValue = new MutableNumberValueImpl();
        numberValue.setValue(new Double(10));
        mutableQuantityValue.setMagnitudeValue( numberValue );

        UnitValue unitValue = QuantityUnits.CENTIMETERS;
        mutableQuantityValue.setUnitValue(unitValue);

        Number expectedResultInMillimeters = new Double(100);
        Number result =
                mutableQuantityValue.getMagnitudeAsNumber(QuantityUnits.MILLIMETERS);

        assertEquals("CM to MM Conversion failure", expectedResultInMillimeters, result);

        // todo add tests for other conversions
    }

    /**
     * Helper method which creates a <code>MutableUnitType</code> which can be used for
     * testing.
     * @return a mutable unit type.
     */
    private MutableQuantityValue createMutableUnitTypeForTestingEquals() {
        MutableQuantityValue mutableQuantityValue = new MutableQuantityValueImpl();

        NumberValueImpl numberValue = new MutableNumberValueImpl();
        mutableQuantityValue.setMagnitudeValue( numberValue );

        UnitValueImpl unitValue = new ImmutableUnitValueImpl();
        mutableQuantityValue.setUnitValue(unitValue);

        return mutableQuantityValue;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jan-05	6560/3	tom	VBM:2004122401 Added Inhibitor base class

 14-Jan-05	6560/1	tom	VBM:2004122401 Completed Metadata API Implementation

 ===========================================================================
*/
