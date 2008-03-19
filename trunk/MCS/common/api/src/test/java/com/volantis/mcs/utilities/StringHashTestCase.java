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

package com.volantis.mcs.utilities;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import junit.framework.Assert;

/**
 * This tests the StringHash class.
 */
public class StringHashTestCase extends TestCaseAbstract {
    /**
     * A test string used in more than one test method.
     */
    private final String value = "HashString";

    /**
     * An expected test result applicable in more than one test method.
     */
    private final String expected = "C515E2A719426599BE9CBFB8F2A9FA81";

    /**
     * Initialise a new instance of this test case.
     */
    public StringHashTestCase() {
    }

    /**
     * Initialise a new named instance of this test case.
     *
     * @param s The name of this test.
     */
    public StringHashTestCase(String s) {
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
     * This tests converting an arbitrary length string to a fixed length
     * string of hex digits.
     */
    public void testGetDigestAsHex() throws Exception {
        // Test a string conversion
        String hexString = StringHash.getDigestAsHex(value);
        Assert.assertEquals("Byte strings should match", expected, hexString);
        Assert.assertEquals("String length is wrong",
                     expected.length(),
                     hexString.length());

        // Test a longer string to ensure length is fine
        hexString = StringHash.getDigestAsHex(value + value);
        Assert.assertEquals("String length is wrong",
                     expected.length(),
                     hexString.length());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 08-Jul-04	4837/1	claire	VBM:2004070603 Ensure correct logging from HashString

 ===========================================================================
*/
