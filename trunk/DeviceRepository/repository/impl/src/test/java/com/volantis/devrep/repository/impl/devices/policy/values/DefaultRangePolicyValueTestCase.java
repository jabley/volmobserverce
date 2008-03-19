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

import com.volantis.mcs.devices.DeviceRepositoryException;

/**
 * This tests the DefaultRangePolicyValue class through implementing abstract
 * methods from the test hierarchy, and also for methods implemented directly
 * in this class.
 */
public class DefaultRangePolicyValueTestCase
        extends DefaultSimplePolicyValueTestAbstract {
    /**
     * A default value used to initialise the various test instances created
     * in this class.
     */
    protected static final String DEFAULT_VALUE = "150";

    /**
     * Initialise a new instance of this test case.
     */
    public DefaultRangePolicyValueTestCase() {
    }

    /**
     * Initialise a new named instance of this test case.
     *
     * @param s The name of the test case.
     */
    public DefaultRangePolicyValueTestCase(String s) {
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
        // +ve integer
        DefaultRangePolicyValue value =
                new DefaultRangePolicyValue(DEFAULT_VALUE);
        assertEquals("Integer values should match (1)",
                     new Integer(150),
                     value.getValueAsInteger());

        // -ve integer
        value = new DefaultRangePolicyValue("-" + DEFAULT_VALUE);
        assertEquals("Integer values should match (2)",
                     new Integer(-150),
                     value.getValueAsInteger());

        // not an integer
        try {
            value = new DefaultRangePolicyValue("Hello");
            fail("Previous line should have caused an exception");
        } catch (DeviceRepositoryException dre) {
            // Test succeeded
        }
    }

    // JavaDoc inherited
    public String getAsStringValue() {
        return DEFAULT_VALUE;
    }

    // javadoc inherited
    public DefaultPolicyValue getTestInstance() {
        DefaultRangePolicyValue drpv = null;
        try {
            drpv = new DefaultRangePolicyValue(DEFAULT_VALUE);
        } catch (DeviceRepositoryException e) {
            fail("A DeviceRepositoryException should not occur.");
        }
        return drpv;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 21-Sep-04	5569/2	pcameron	VBM:2004091719 NumberFormatException handled within device repository PAPI

 20-Sep-04	5563/2	pcameron	VBM:2004091719 NumberFormatExcetion handled within device repository PAPI

 28-Jul-04	4952/1	claire	VBM:2004072301 Public API for Device Repository: Provide PolicyValue implementations

 ===========================================================================
*/
