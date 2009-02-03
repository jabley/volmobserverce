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
import com.volantis.shared.metadata.impl.MetaDataTestCaseHelper;
import com.volantis.shared.metadata.value.MetaDataValueTestCaseAbstract;
import com.volantis.shared.metadata.value.mutable.MutableChoiceValue;

/**
 * Test case for {@link ChoiceValueImpl}.
 */
public class ChoiceValueImplTestCase extends MetaDataValueTestCaseAbstract{

    /**
     * Constructor
     * @param name The name of this test case.
     */
    public ChoiceValueImplTestCase(String name) throws Exception {
        super(name);
    }

    // Javadoc inherited.
    protected MutableInhibitor getMutableInhibitor() {
        return new MutableChoiceValueImpl();
    }

    // Javadoc inherited.
    protected ImmutableInhibitor getImmutableInhibitor() {
        return new ImmutableChoiceValueImpl();
    }

     // Javadoc inherited.
    public void testEqualsAndHashcodeImplementedCorrectly() {

        MutableChoiceValue choiceValue1 = createMutableChoiceValueForTestingEquals();
        MutableChoiceValue choiceValue2 = createMutableChoiceValueForTestingEquals();

        // test that both the values are equal and they have the same hash codes
        assertEquals("Object 1 should be equal to object 2", choiceValue1,
                choiceValue2);
        MetaDataTestCaseHelper.ensureHashcodesEqual(choiceValue1, choiceValue2);

        // change the value
        final MutableNumberValueImpl value = new MutableNumberValueImpl();
        value.setValue(new Integer(123));
        choiceValue2.setValue(value);
        MetaDataTestCaseHelper.assertNotEquals(choiceValue1, choiceValue2);
        MetaDataTestCaseHelper.ensureHashcodesNotEqual(choiceValue1, choiceValue2);

        // now reset
        choiceValue2 = createMutableChoiceValueForTestingEquals();

        // change the choice name
        choiceValue2.setChoiceName("other");
        MetaDataTestCaseHelper.assertNotEquals(choiceValue1, choiceValue2);
        MetaDataTestCaseHelper.ensureHashcodesNotEqual(choiceValue1, choiceValue2);
    }

    /**
     * Helper method which creates a <code>MutableChoiceValue</code> which can 
     * be used for testing.
     * 
     * @return a mutable unit type.
     */
    private MutableChoiceValue createMutableChoiceValueForTestingEquals() {
        MutableChoiceValue mutableChoiceValue = new MutableChoiceValueImpl();

        mutableChoiceValue.setChoiceName("dummy");

        MutableNumberValueImpl value = new MutableNumberValueImpl();
        value.setValue(new Integer(1));
        mutableChoiceValue.setValue(value);

        return mutableChoiceValue;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jan-05  6560/3  tom VBM:2004122401 Added Inhibitor base class

 14-Jan-05  6560/1  tom VBM:2004122401 Completed Metadata API Implementation

 ===========================================================================
*/
