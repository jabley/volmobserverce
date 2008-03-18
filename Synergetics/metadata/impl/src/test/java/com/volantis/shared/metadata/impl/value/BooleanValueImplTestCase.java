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
import com.volantis.shared.metadata.value.MetaDataValueTestCaseAbstract;
import com.volantis.shared.metadata.value.mutable.MutableBooleanValue;

/**
 * Test case for <code>BooleanValueImpl</code>.
 */
public class BooleanValueImplTestCase extends MetaDataValueTestCaseAbstract{

    // Javadoc inherited.
    public BooleanValueImplTestCase(String name) throws Exception {
        super(name);
    }

    // Javadoc inherited.
    protected MutableInhibitor getMutableInhibitor() {
        return new MutableBooleanValueImpl();
    }

    // Javadoc inherited.
    protected ImmutableInhibitor getImmutableInhibitor() {
        return new ImmutableBooleanValueImpl();
    }

    // Javadoc inherited.
    public void testEqualsAndHashcodeImplementedCorrectly() {
        MutableBooleanValue booleanValue1 =
                createBooleanValueForTestEquals();
        MutableBooleanValue booleanValue2 =
                createBooleanValueForTestEquals();

        // test that both the values are equal and they have the same hash codes
        assertEquals("Object 1 should be equal to object 2", booleanValue1, booleanValue2);
        assertEquals("Object 2 should be equal to object 1", booleanValue2, booleanValue1);

        int booleanValue1Hashcode = booleanValue1.hashCode();
        int booleanValue2Hashcode = booleanValue2.hashCode();
        assertTrue("Objects which are equal should have the same hash codes. Were : "
                + booleanValue1Hashcode + " and " + booleanValue2Hashcode,
                booleanValue1Hashcode == booleanValue2Hashcode);

        // change a property in one of the objects and ensure they are not equal
        booleanValue2.setValue(Boolean.FALSE);

        assertNotEquals(booleanValue1, booleanValue2);
        assertNotEquals(booleanValue2, booleanValue1);

        // ensure that the hashcodes are different for these two objects
        booleanValue1Hashcode = booleanValue1.hashCode();
        booleanValue2Hashcode = booleanValue2.hashCode();
        assertFalse("Objects which are not equal should ideally have different hash codes",
                booleanValue1Hashcode == booleanValue2Hashcode);
    }

    /**
     * Helper method for {@link #testEqualsAndHashcodeImplementedCorrectly()} which creates a
     * <code>MutableBooleanValue</code> which can be used for testing that
     * <code>BooleanValueImpl</code>.equals() has been implemented correctly.
     * @return a mutable boolean value.
     */
    private MutableBooleanValue createBooleanValueForTestEquals() {
        MutableBooleanValueImpl booleanValue = new MutableBooleanValueImpl();
        booleanValue.setValue(Boolean.TRUE);
        return booleanValue;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jan-05	6560/5	tom	VBM:2004122401 Added Inhibitor base class

 10-Jan-05	6560/3	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
