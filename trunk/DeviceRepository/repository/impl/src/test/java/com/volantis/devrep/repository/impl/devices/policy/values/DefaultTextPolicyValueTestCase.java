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

/**
 * This tests the DefaultTextPolicyValue class through implementing abstract
 * methods from the test hierarchy, and also for methods implemented directly
 * in this class.
 */
public class DefaultTextPolicyValueTestCase
        extends DefaultSimplePolicyValueTestAbstract {
    /**
     * A default value used to initialise the various test instances created
     * in this class.
     */
    protected static final String TEST_STRING = "Test Value";

    /**
     * Initialise a new instance of this test case.
     */
    public DefaultTextPolicyValueTestCase() {
    }

    /**
     * Initialise a new named instance of this test case.
     *
     * @param s The name of the test case.
     */
    public DefaultTextPolicyValueTestCase(String s) {
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
        DefaultTextPolicyValue value =
                (DefaultTextPolicyValue) getTestInstance();
        assertEquals("Strings should match (1)",
                     value.getAsString(),
                     getAsStringValue());
        assertEquals("Strings should match (2)",
                     value.getValueAsString(),
                     getAsStringValue());
    }

    // JavaDoc inherited
    public String getAsStringValue() {
        return TEST_STRING;
    }

    // JavaDoc inherited
    public DefaultPolicyValue getTestInstance() {
        return new DefaultTextPolicyValue(TEST_STRING);
    }
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
