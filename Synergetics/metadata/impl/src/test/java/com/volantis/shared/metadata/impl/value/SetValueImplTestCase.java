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

import java.util.Set;

/**
 * Test case for <code>SetValueImpl</code>.
 */
public class SetValueImplTestCase extends CollectionValueImplTestCaseAbstract {

    /**
     * Constructor
     * @param name The name of this test.
     */
    public SetValueImplTestCase(String name) throws Exception {
        super(name);
    }

    // Javadoc inherited.
    protected MutableInhibitor getMutableInhibitor() {
        return new MutableSetValueImpl();
    }

    // Javadoc inherited.
    protected ImmutableInhibitor getImmutableInhibitor() {
        return new ImmutableSetValueImpl();
    }


    // Javadoc inherited.
    public void testEqualsAndHashcodeImplementedCorrectly() {
        MutableBooleanValueImpl booleanValue = new MutableBooleanValueImpl();
        booleanValue.setValue(Boolean.TRUE);
        MutableSetValueImpl setValue1 = createSetValueForTests(booleanValue);
        MutableSetValueImpl setValue2 = createSetValueForTests(booleanValue);

        // ensure that the two objects are equal
        assertEquals("Object 1 should  be equal to object 2", setValue1, setValue2);
        assertEquals("Object 2 should  be equal to object 1", setValue1, setValue2);

        // ensure that they have the same hash code
        int listValue1Hashcode = setValue1.hashCode();
        int listValue2Hashcode = setValue2.hashCode();
        assertTrue("Objects which are equal should have the same hash codes. Were : "
                + listValue1Hashcode + " and " + listValue2Hashcode,
                listValue1Hashcode == listValue2Hashcode);

        // now change a single item in each list and ensure that they are different
        setValue2.getContentsAsMutableSet().remove(booleanValue);
        assertNotEquals(setValue1, setValue2);

        // see if the hashcodes are different
        listValue1Hashcode = setValue1.hashCode();
        listValue2Hashcode = setValue2.hashCode();
        assertFalse("Objects which are not equal should ideally not have the same hash " +
                "codes. Were : " + listValue1Hashcode + " and " + listValue2Hashcode,
                listValue1Hashcode == listValue2Hashcode);
    }

    public void testPersistence() throws Exception {
        super.testPersistence();    // @todo implement this
    }

    /**
     * Helper method for which creates a MutableSetValueImpl and populates it with the
     * provided data.
     * @return a <code>MutableListValueImpl</code> with a single item in its list.
     */
    private MutableSetValueImpl createSetValueForTests(BooleanValueImpl booleanValueImpl) {
        MutableSetValueImpl mutableSetValue = new MutableSetValueImpl();
        Set set = mutableSetValue.getContentsAsMutableSet();
        set.add(booleanValueImpl);
        return mutableSetValue;
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
