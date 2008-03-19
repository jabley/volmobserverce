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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 18-May-03    Geoff           VBM:2003042904 - Created; a test case for 
 *                              VersionCode.
 * 03-Jun-03    Allan           VBM:2003060301 - TestCaseAbstract moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * A test case for {@link VersionCode}.
 */ 
public class VersionTestCase extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd ${YEAR}.";

    public VersionTestCase(String s) {
        super(s);
    }

    // Test some expected results from the spec.
    
    public void testCreate1_3() {
        checkCreateBoth(1, 3, 0x03);
    }

    public void testCreate2_7() {
        checkCreateBoth(2, 7, 0x17);
    }

    // Bounds check the major version.
    
    public void testMajorNegative() {
        checkCreateFailure(-2, 0);
    }
    
    public void testMajorUnderflow() {
        checkCreateFailure(0, 0);
    }

    public void testMajorMin() {
        checkCreateSuccess(1, 0);
    }
    
    public void testMajorMax() {
        checkCreateSuccess(0xF, 0);
    }

    public void testMajorOverflow() {
        checkCreateFailure(0x10, 0);
    }

    // Bounds check the minor version.

    public void testMinorUnderflow() {
        checkCreateFailure(2, -1);
    }

    public void testMinorMin() {
        checkCreateSuccess(2, 0);
    }
    
    public void testMinorMax() {
        checkCreateSuccess(2, 0xF);
    }

    public void testMinorOverflow() {
        checkCreateFailure(2, 0x10);
    }

    // Bounds check the code.
    
    public void testCodeUnderflow() {
        checkCreateFailure(-1);
    }

    public void testCodeMin() {
        checkCreateSuccess(0x1);
    }
    
    public void testCodeMax() {
        checkCreateSuccess(0xFF);
    }

    public void testCodeOverflow() {
        checkCreateFailure(0x100);
    }

    // Utility methods.
    
    private void checkCreateBoth(int major, int minor, int code) {
        VersionCode numericVersion = new VersionCode(major, minor);
        checkMatch(major, minor, code, numericVersion);
        VersionCode codeVersion = new VersionCode(code);
        checkMatch(major, minor, code, codeVersion);
    }

    private void checkMatch(int major, int minor, int code, VersionCode version) {
        assertEquals(major, version.getMajor());
        assertEquals(minor, version.getMinor());
        assertEquals(code, version.getInteger());
    }
    
    private void checkCreateSuccess(int major, int minor) {
        new VersionCode(major, minor);
    }

    private void checkCreateFailure(int major, int minor) {
        try {
            new VersionCode(major, minor);
            fail("numeric version should be invalid (" + major + "," + 
                    minor + ")" );
        } catch (IllegalArgumentException e) {
            // We expect this, continue.
        }
    }

    private void checkCreateSuccess(int code) {
        new VersionCode(code);
    }
    
    private void checkCreateFailure(int code) {
        try {
            new VersionCode(code);
            fail("coded version should be invalid (" + code + ")" );
        } catch (IllegalArgumentException e) {
            // We expect this, continue.
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

 ===========================================================================
*/
