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
package com.volantis.mcs.devices;

import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.utilities.BooleanObject;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import junitx.util.PrivateAccessor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Test case for InternalDevice.
 */
public class InternalDeviceTestCase extends TestCaseAbstract {

    private static final InternalDeviceFactory INTERNAL_DEVICE_FACTORY =
        InternalDeviceFactory.getDefaultInstance();

    /**
     * Create an InternalDevice with an initial set of policies for
     * use by test cases.
     * @return an InternalDevice.
     */
    private InternalDevice createInternalDevice() {
        HashMap policies = new HashMap();
        policies.put("one", "one");
        policies.put("two", "two");
        policies.put("multi1", "value1, value2, value3");
        policies.put("three", "three, three");
        policies.put("multi2", "");
        policies.put("four", "four");
        policies.put("five", "five");
        policies.put("ssversion", "hello, CSS1, CSS2");

        final DefaultDevice defaultDevice =
            new DefaultDevice("device", policies, null);
        InternalDevice device =
            INTERNAL_DEVICE_FACTORY.createInternalDevice(defaultDevice);

        return device;
    }

    /**
     * Test that getStyleSheetVersion works as expected with a multi
     * valued ssversion device policy.
     */
    public void testGetStyleSheetVersion() {
        InternalDevice device = createInternalDevice();
        String ssversion = device.getStyleSheetVersion();

        assertEquals("CSS1", ssversion);
    }

    /**
     * Test that selectSinglePolicyValue works with a multi valued
     * policy.
     */
    public void testSelectSingleValueWithMultiValuedPolicy() throws Exception {
        InternalDevice device = createInternalDevice();

        String [] array1 = { "hello", "value2", "bye"};

        final BooleanObject retrievedFromCache = new BooleanObject();
        retrievedFromCache.setValue(false);

        final BooleanObject cached = new BooleanObject();
        cached.setValue(false);

        // Set up listPolicies so we can test access.
        PrivateAccessor.setField(device, "listPolicies", new HashMap() {
            public Object get(Object key) {
                Object value = super.get(key);
                if(value!=null) {
                    retrievedFromCache.setValue(true);
                }
                return value;
            }
            public Object put(Object key, Object value) {
                cached.setValue(true);
                return super.put(key, value);
            }
        });

        List selection = Arrays.asList(array1);
        String policy = device.selectSingleKnownPolicyValue("multi1", selection);
        assertEquals("value2", policy);

        // Test that the policy retrieved multi-valued policy has been
        // cached in listPolicies.
        assertTrue("Expected retrieved policy to have been cached.",
                cached.getValue());

        assertFalse("The value should not have been retrieved from " +
                "listPolicies", retrievedFromCache.getValue());

        // Try to get the policy again and this time the cache should be
        // used.
        policy = device.selectSingleKnownPolicyValue("multi1", selection);
        assertEquals("value2", policy);

        assertTrue("Expected the policy to be retrieved from listPolicies",
                retrievedFromCache.getValue());

        // Check that a value not in the selection does not get returned.
        policy = device.selectSingleKnownPolicyValue("multi2", selection);
        assertNull(policy);
    }

    /**
     * Test that selectSinglePolicyValue works with a single valued
     * policy.
     */
    public void testSelectSingleValueWithStructuredPolicy() {
        InternalDevice device = createInternalDevice();

        String [] array1 = { "four" };

        List selection = Arrays.asList(array1);
        String policy = device.selectSingleKnownPolicyValue("four", selection);
        assertEquals("four", policy);

        String [] array2 = { "hello" };
        selection = Arrays.asList(array2);

        policy = device.selectSingleKnownPolicyValue("four", selection);
        assertNull(policy);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 21-Sep-04	5567/1	allan	VBM:2004092010 Handle multi-valued device policy selection.

 ===========================================================================
*/
