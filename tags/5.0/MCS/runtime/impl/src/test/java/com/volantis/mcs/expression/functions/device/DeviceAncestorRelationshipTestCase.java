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
package com.volantis.mcs.expression.functions.device;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.DeviceAncestorRelationship;

/**
 * Unit test for the {@link DeviceAncestorRelationship} class
 */
public class DeviceAncestorRelationshipTestCase extends TestCaseAbstract {

    /**
     * Test that ensures the {@link DeviceAncestorRelationship#get} method
     * returns the {@link DeviceAncestorRelationship#ANCESTOR} instance
     * when passed the
     * {@link com.volantis.mcs.context.MarinerRequestContext#IS_ANCESTOR}
     * value as the int argument
     * @throws Exception if an error occurs
     */
    public void testGetWithAncestorArg() throws Exception {
        doTestGet(MarinerRequestContext.IS_ANCESTOR,
                  DeviceAncestorRelationship.ANCESTOR);
    }

    /**
     * Test that ensures the {@link DeviceAncestorRelationship#get} method
     * returns the {@link com.volantis.mcs.context.DeviceAncestorRelationship#UNKNOWN} instance
     * when passed the
     * {@link com.volantis.mcs.context.MarinerRequestContext#UNKNOWN}
     * value as the int argument
     * @throws Exception if an error occurs
     */
    public void testGetWithUnknownArg() throws Exception {
        doTestGet(MarinerRequestContext.UNKNOWN,
                  DeviceAncestorRelationship.UNKNOWN);
    }

    /**
     * Test that ensures the {@link DeviceAncestorRelationship#get} method
     * returns the {@link DeviceAncestorRelationship#DEVICE} instance
     * when passed the
     * {@link com.volantis.mcs.context.MarinerRequestContext#IS_DEVICE}
     * value as the int argument
     * @throws Exception if an error occurs
     */
    public void testGetWithDeviceArg() throws Exception {
        doTestGet(MarinerRequestContext.IS_DEVICE,
                  DeviceAncestorRelationship.DEVICE);
    }

    /**
     * Test that ensures the {@link DeviceAncestorRelationship#get} method
     * returns the {@link DeviceAncestorRelationship#UNRELATED} instance
     * when passed the
     * {@link com.volantis.mcs.context.MarinerRequestContext#IS_UNRELATED}
     * value as the int argument
     * @throws Exception if an error occurs
     */
    public void testGetWithUnrelatedArg() throws Exception {
        doTestGet(MarinerRequestContext.IS_UNRELATED,
                  DeviceAncestorRelationship.UNRELATED);
    }

    /**
     * Test that ensures the {@link DeviceAncestorRelationship#get} method
     * returns the {@link DeviceAncestorRelationship#DEVICE} instance
     * when passed the
     * {@link com.volantis.mcs.context.MarinerRequestContext#IS_DEVICE}
     * value as the int argument
     * @throws Exception if an error occurs
     */
    public void testGetWithInvalidArg() throws Exception {
        doTestGet(1000000,
                  DeviceAncestorRelationship.UNKNOWN);
    }

    /**
     * Helper method that executes the {@link DeviceAncestorRelationship#get}
     * method at tests that the correct value is returned
     * @param argValue the int value that is passed in to the method that
     * is being tested
     * @param expected the expected return object
     */
    protected void doTestGet(int argValue,
                             DeviceAncestorRelationship expected) {

        // execute the get method
        DeviceAncestorRelationship actual
                = DeviceAncestorRelationship.get(argValue);

        // check that the correct value is returned
        assertSame("Wrong DeviceAncestorRelationship value", expected, actual);
    }

    /**
     * Test to ensure that the
     * {@link DeviceAncestorRelationship#getRelationshipName} method returns
     * the correct value for the UNKNOWN relationship
     * @throws Exception if an error occurs
     */
    public void testGetRelationshipNameForUnknown() throws Exception {
        doTestGetRelationshipName(DeviceAncestorRelationship.UNKNOWN,
                                  "unknown");
    }

    /**
     * Test to ensure that the
     * {@link DeviceAncestorRelationship#getRelationshipName} method returns
     * the correct value for the UNRELATED relationship
     * @throws Exception if an error occurs
     */
    public void testGetRelationshipNameForUnrelated() throws Exception {
        doTestGetRelationshipName(DeviceAncestorRelationship.UNRELATED,
                                  "unrelated");
    }

    /**
     * Test to ensure that the
     * {@link DeviceAncestorRelationship#getRelationshipName} method returns
     * the correct value for the ANCESTOR relationship
     * @throws Exception if an error occurs
     */
    public void testGetRelationshipNameForAncestor() throws Exception {
        doTestGetRelationshipName(DeviceAncestorRelationship.ANCESTOR,
                                  "ancestor");
    }

    /**
     * Test to ensure that the
     * {@link DeviceAncestorRelationship#getRelationshipName} method returns
     * the correct value for the DEVICE relationship
     * @throws Exception if an error occurs
     */
    public void testGetRelationshipNameForDevice() throws Exception {
        doTestGetRelationshipName(DeviceAncestorRelationship.DEVICE,
                                  "device");
    }

    /**
     * Test the {@link DeviceAncestorRelationship#getRelationshipName} method
     * to ensure that it returns the correct String
     * @param relationship the DeviceAncestorRelationship instance to test
     * @param expected the expected String
     * @throws Exception if an error occurs
     */
    protected void doTestGetRelationshipName(
            DeviceAncestorRelationship relationship,
            String expected) throws Exception {
        assertEquals("The getRelationshipName method for the " + expected +
                     " relationship returned unexpected string",
                     expected,
                     relationship.getRelationshipName());
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
