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
 * This tests the DefaultCompositePolicyValue class. 
 */
public abstract class DefaultCompositePolicyValueTestAbstract
        extends DefaultPolicyValueTestAbstract {
    /**
     * Initialise a instance of this test case.
     */
    public DefaultCompositePolicyValueTestAbstract() {
    }

    /**
     * Initialise a new named instance of this test case.
     *
     * @param s The name of the test case.
     */
    public DefaultCompositePolicyValueTestAbstract(String s) {
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

    public void testEnsureCompleteness() throws Exception {
        DefaultCompositePolicyValue value =
                (DefaultCompositePolicyValue) getTestInstance();

        value.complete = false;
        // Test incompleteness
        value.ensureIncomplete();
        try {
            value.ensureComplete();
            fail("Expected an exception to be thrown here (1)");
        } catch (IllegalStateException ise) {
            // Test succeeded
        }

        // Test completeness
        value.complete = true;
        value.ensureComplete();
        try {
            value.ensureIncomplete();
            fail("Expected an exception to be thrown here (2)");
        } catch (IllegalStateException ise) {
            // Test succeeded
        }
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
