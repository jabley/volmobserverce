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

package com.volantis.shared.metadata.impl.value;

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.metadata.value.mutable.MutableNumberValue;

/**
 * Test case for <code>NumberValueImpl</code>.
 */
public class NumberValueImplTestCase extends SimpleValueImplTestCaseAbstract {

    /**
     * Constructor
     * @param name The name of this test case.
     */
    public NumberValueImplTestCase(String name) throws Exception {
        super(name);
    }

    // Javadoc inherited.
    protected MutableInhibitor getMutableInhibitor() {
        MutableNumberValue mnv = new MutableNumberValueImpl();
        mnv.setValue(new Double(10.0));
        return mnv;
    }

    // Javadoc inherited.
    protected ImmutableInhibitor getImmutableInhibitor() {
        return new ImmutableNumberValueImpl(new Double(10.0));
    }

    // Javadoc inherited.
    public void testEqualsAndHashcodeImplementedCorrectly() {
        MutableNumberValue numberValue1 = getMutableNumberValueForTestEquals();
        MutableNumberValue numberValue2 = getMutableNumberValueForTestEquals();

        // test that both the values are equal and they have the same hash codes
        assertEquals("Object 1 should be equal to object 2", numberValue1, numberValue2);
        assertEquals("Object 2 should be equal to object 1", numberValue2, numberValue1);

        int booleanValue1Hashcode = numberValue1.hashCode();
        int booleanValue2Hashcode = numberValue2.hashCode();
        assertTrue("Objects which are equal should have the same hash codes. Were : "
                + booleanValue1Hashcode + " and " + booleanValue2Hashcode,
                booleanValue1Hashcode == booleanValue2Hashcode);

        // change a property in one of the objects and ensure they are not equal
        numberValue2.setValue(new Double(2.0));

        assertNotEquals(numberValue1, numberValue2);
        assertNotEquals(numberValue2, numberValue1);

        // ensure that the hashcodes are different for these two objects
        booleanValue1Hashcode = numberValue1.hashCode();
        booleanValue2Hashcode = numberValue2.hashCode();
        assertFalse("Objects which are not equal should ideally have different hash codes",
                booleanValue1Hashcode == booleanValue2Hashcode);
    }

    /**
     * Helper method for {@link #testEqualsAndHashcodeImplementedCorrectly}.
     * @return a <code>MutableNumberValue</code> useful for testing.
     */
    private MutableNumberValue getMutableNumberValueForTestEquals() {
        MutableNumberValueImpl numberValue = new MutableNumberValueImpl();
        numberValue.setValue(new Double(1.0));
        return numberValue;
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
