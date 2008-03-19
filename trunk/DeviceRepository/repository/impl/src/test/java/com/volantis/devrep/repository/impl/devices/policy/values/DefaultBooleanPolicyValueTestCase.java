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
 * This tests the DefaultBooleanPolicyValue class through implementing abstract
 * methods from the test hierarchy, and also for methods implemented directly
 * in this class.
 */
public class DefaultBooleanPolicyValueTestCase
        extends DefaultSimplePolicyValueTestAbstract {
    /**
     * A string representation of a true value used in these tests.
     */
    protected static final String TRUE_STRING = "true";

    /**
     * A string representation of a false value used in these tests.
     */
    protected static final String FALSE_STRING = "false";

    /**
     * Initialise a new instance of this test case.
     */
    public DefaultBooleanPolicyValueTestCase() {
    }

    /**
     * Initialise a new named instance of this test case.
     *
     * @param s The name of the test case.
     */
    public DefaultBooleanPolicyValueTestCase(String s) {
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
     * This tests the boolean policy value with true.
     */
    public void testTrue() throws Exception {
        testBooleanValue(TRUE_STRING, true);
    }

    /**
     * This tests the boolean policy value with false.
     */
    public void testFalse() throws Exception {
        // This also test all other strings - anything other than "true" gets
        // converted to false by the Boolean constructor.
        testBooleanValue(FALSE_STRING, false);
    }

    /**
     * Tests arbitrary string values.
     */
    public void testArbitraryStrings() throws Exception {
        testBooleanValue("FALSE", false);
        testBooleanValue("TRUE", true);
        testBooleanValue("tRuE", true);
        testBooleanValue("blah", false);
        testBooleanValue("", false);
        testBooleanValue(null, false);
    }

    /**
     * Given a string and boolean value, this constructs a boolean policy value
     * instance from them as appropriate and also ensures the values returned
     * are as expected.
     *
     * @param stringValue  The string value which should be used to initialise
     *                     the instance and match any returned strings.
     * @param booleanValue The boolean value expected from initialising the
     *                     test instance with the <code>stringValue</code>.
     */
    private void testBooleanValue(String stringValue,
                                  boolean booleanValue) throws Exception {
        DefaultBooleanPolicyValue value =
                new DefaultBooleanPolicyValue(stringValue);
        assertEquals("Value strings should match",
                     value.getAsString(),
                     new Boolean(stringValue).toString());
        Boolean asBoolean = value.getValueAsBoolean();
        assertEquals("Values should match",
                     asBoolean.booleanValue(),
                     booleanValue);
    }

    // JavaDoc inherited
    public String getAsStringValue() {
        return TRUE_STRING;
    }

    // JavaDoc inherited
    public DefaultPolicyValue getTestInstance() {
        return new DefaultBooleanPolicyValue(TRUE_STRING);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 21-Sep-04	5569/1	pcameron	VBM:2004091719 NumberFormatException handled within device repository PAPI

 20-Sep-04	5563/1	pcameron	VBM:2004091719 NumberFormatExcetion handled within device repository PAPI

 28-Jul-04	4952/1	claire	VBM:2004072301 Public API for Device Repository: Provide PolicyValue implementations

 ===========================================================================
*/
