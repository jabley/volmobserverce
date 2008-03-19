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

package com.volantis.devrep.repository.impl.devices.policy.values;

import com.volantis.mcs.devices.policy.values.PolicyValue;

import java.util.List;

/**
 * This tests all DefaultSetPolicyValue classes.
 */
public abstract class DefaultSetPolicyValueTestAbstract
        extends DefaultCompositePolicyValueTestAbstract {
    /**
     * The test values used for the set tests
     */
    protected static final String[] keywords = new String[] {
        "option 1",
        "option 2",
        "option 3",
        "option 4",
        "option 5"
    };

    /**
     * Initialise a new instance of this test case.
     */
    public DefaultSetPolicyValueTestAbstract() {
    }

    /**
     * Initialise a new named instance of this test case.
     *
     * @param s The name of the test case.
     */
    public DefaultSetPolicyValueTestAbstract(String s) {
        super(s);
    }

    // JavaDoc inherited
    protected void setUp() throws Exception {
        super.setUp();
    }

    // JavaDoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test the various object creation and retrieval possibilities
     */
    public void testCreationAndRetrieval() throws Exception {
        DefaultSetPolicyValue value = (DefaultSetPolicyValue) getTestInstance();
        List valueList = value.getValueAsList();
        assertNotNull("Value list should exist", valueList);
        List expectedList = getValueList();
        assertEquals("List sizes should match",
                     expectedList.size(),
                     valueList.size());
        int size = valueList.size();
        for (int i = 0; i < size; i++) {
            // This test assumes the lists are never reordered which is fine
            // with the current implementation.
            assertEquals("List item " + i + " should match",
                         ((PolicyValue)valueList.get(i)).getAsString(),
                         ((PolicyValue)expectedList.get(i)).getAsString());
        }
    }

    /**
     * A means of providing a test specific list for the desired result for
     * the operation of {@link DefaultSetPolicyValue#getValueAsList}.
     *
     * @return A list that matches the result of applying getValueAsList to the
     *         test instance provided by {@link #getTestInstance}.
     */
    protected abstract List getValueList();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Jul-04	4952/1	claire	VBM:2004072301 Public API for Device Repository: Provide PolicyValue implementations

 ===========================================================================
*/
