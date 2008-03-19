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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.core;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.eclipse.core.DeviceHeaderPattern;

/**
 * Tests the {@link DeviceHeaderPattern} class
 */
public class DeviceHeaderPatternTestCase extends TestCaseAbstract {

    /**
     * Ensure that an IllegalArgumentExcpetion is thrown when the constructor
     * is passed null for the name argument
     */
    public void testNameCannotBeNullOnConstruction() {
        try {
            new DeviceHeaderPattern(null, "regularExpression", "baseDevice");
            fail("Expected an IllegalArgumentException as the name " +
                 "arg is null");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Ensure that an IllegalArgumentExcpetion is thrown when the constructor
     * is passed null for the regularExpression argument
     */
    public void testRegularExpressionCannotBeNullOnConstruction() {
        try {
            new DeviceHeaderPattern("name", null, "baseDevice");
            fail("Expected an IllegalArgumentException as the " +
                 "regularExpression arg is null");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Ensure no excpetion is thrown when the baseDevice param is null
     */
    public void testBaseDeviceCanBeNullOnConstruction() {
        try {
            new DeviceHeaderPattern("name", "regularExpression", null);
        } catch (Exception e) {
            fail("Base device is allowed to be null");
        }
    }

    /**
     * Tests the {@link DeviceHeaderPattern#getName} method
     * @throws Exception if an error occurs.
     */
    public void testGetName() throws Exception {
        DeviceHeaderPattern dhp = new DeviceHeaderPattern("name",
                                                          "regularExpression",
                                                          "baseDevice");
        assertEquals("Unexpected name value", "name", dhp.getName());
    }

    /**
     * Tests the {@link DeviceHeaderPattern#getRegularExpression} method
     * @throws Exception if an error occurs.
     */
    public void testGetRegularExpression() throws Exception {
        DeviceHeaderPattern dhp = new DeviceHeaderPattern("name",
                                                          "regularExpression",
                                                          "baseDevice");
        assertEquals("Unexpected regular expression value",
                     "regularExpression",
                     dhp.getRegularExpression());
    }

    /**
     * Tests the {@link DeviceHeaderPattern#getBaseDevice} method
     * @throws Exception if an error occurs.
     */
    public void testGetBaseDevice() throws Exception {
        DeviceHeaderPattern dhp = new DeviceHeaderPattern("name",
                                                          "regularExpression",
                                                          "baseDevice");
        assertEquals("Unexpected base device value",
                     "baseDevice",
                     dhp.getBaseDevice());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 04-May-04	4007/1	doug	VBM:2004032304 Added a PrimaryPatterns form section

 ===========================================================================
*/
