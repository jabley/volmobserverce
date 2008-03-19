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

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * This is the root of the test hierarhcy for the default policy value classes.
 * It provides some tests that apply from this level in the hierarchy all the
 * way down.  Subclasses in this hierarchy can add any extra tests as
 * required, or override those where the test makes no sense.
 */
public abstract class DefaultPolicyValueTestAbstract extends TestCaseAbstract {

    /**
     * Initialise a new instance of this test case.
     */
    public DefaultPolicyValueTestAbstract() {
    }

    /**
     * Initialise a new named instance of this test case.
     *
     * @param s The name of the test case.
     */
    public DefaultPolicyValueTestAbstract(String s) {
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
     * This tests the functionality of the {@link DefaultPolicyValue#getAsString}
     * method using subclass specific test instances
     * ({@link #getTestInstance}) and expected values ({@link #getAsStringValue}).
     */
    public void testGetAsString() throws Exception {
        DefaultPolicyValue value = getTestInstance();
        String asString = value.getAsString();
        assertEquals("Values as strings should match",
                     asString,
                     getAsStringValue());
    }

    /**
     * A means of providing all classes in the test hierarchy with an
     * initialised instance of a DefaultPolicyValue for use in the various
     * test methods throuhout the hierarchy.
     *
     * @return An instance of a default policy value that can be used in tests
     */
    public abstract DefaultPolicyValue getTestInstance();

    /**
     * A means of providing a test specific value for the desired result for
     * the operation of {@link DefaultPolicyValue#getAsString}.
     *
     * @return A string that matches the result of applying getAsString to the
     *         test instance provided by {@link #getTestInstance}.
     */
    public abstract String getAsStringValue();
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
