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

import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.devrep.repository.api.devices.policy.values.PolicyValueFactory;
import com.volantis.devrep.repository.impl.accessors.TestPolicyDescriptorAccessor;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultBooleanPolicyType;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultIntPolicyType;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultOrderedSetPolicyType;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultStructurePolicyType;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultTextPolicyType;
import com.volantis.mcs.devices.policy.values.BooleanPolicyValue;
import com.volantis.mcs.devices.policy.values.IntPolicyValue;
import com.volantis.mcs.devices.policy.values.OrderedSetPolicyValue;
import com.volantis.mcs.devices.policy.values.PolicyValue;
import com.volantis.mcs.devices.policy.values.StructurePolicyValue;
import com.volantis.mcs.devices.policy.values.TextPolicyValue;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This tests the <code>PolicyValueFactory</code> class and creates various
 * policy values in order to exercise the various aspects of the factory.
 */
public class PolicyValueFactoryTestCase extends TestCaseAbstract {

    /**
     * This tests the creation of a simple policy value.  This uses the
     * public method {@link PolicyValueFactory#createPolicyValue} but will
     * exercise the simple value code.
     */
    public void testCreateSimplePolicyValue() throws Exception {

        // Create simple accessor for use in the tests
        TestPolicyDescriptorAccessor accessor =
                new TestPolicyDescriptorAccessor();
        accessor.addPolicyDescriptor(null, "policyname",
                new DefaultTextPolicyType());

        // Create a device to pass into the factory
        DefaultDevice device = new DefaultDevice(null, null, null) {
            public String getPolicyValue(String policy) {
                return "Policy Value";
            }
        };

        // Create the factory
        PolicyValueFactory factory = new DefaultPolicyValueFactory(accessor);

        // Call the creation method on the factory
        PolicyValue value = factory.createPolicyValue(device, "policyname");

        // Check test outcome
        assertNotNull("Policy value should exist", value);
        assertTrue("Based on test setup expected a TextPolicyValue",
                   value instanceof TextPolicyValue);
        assertEquals("text value incorrect", "Policy Value",
                value.getAsString());
    }

    /**
     * This tests the creation of a simple policy value where there is no value
     * in the device for that policy.  This uses the public method
     * {@link PolicyValueFactory#createPolicyValue} but will exercise the
     * simple value code.
     */
    public void testCreateSimplePolicyValueNull() throws Exception {

        // Create simple accessor for use in the tests
        TestPolicyDescriptorAccessor accessor =
                new TestPolicyDescriptorAccessor();
        accessor.addPolicyDescriptor(null, "policyname",
                new DefaultTextPolicyType());

        // Create a device to pass into the factory
        DefaultDevice device = new DefaultDevice(null, null, null) {
            public String getPolicyValue(String policy) {
                return null;
            }
        };

        // Create the factory
        PolicyValueFactory factory = new DefaultPolicyValueFactory(accessor);

        // Call the creation method on the factory
        PolicyValue value = factory.createPolicyValue(device, "policyname");

        // Check test outcome
        assertNull("Policy value should not exist", value);
    }


    /**
     * This tests the creation of a composite policy value.  This uses the
     * public method {@link PolicyValueFactory#createPolicyValue} but will
     * exercise the composite value code.
     */
    public void testCreateCompositePolicyValue() throws Exception {

        // Create simple accessor for use in the tests
        TestPolicyDescriptorAccessor accessor =
                new TestPolicyDescriptorAccessor();
        accessor.addPolicyDescriptor(null, "policyname",
                new DefaultOrderedSetPolicyType(new DefaultTextPolicyType()));

        // Create a device to pass into the factory
        DefaultDevice device = new DefaultDevice(null, null, null) {
            public String getPolicyValue(String policy) {
                return "item 1, item 2, item 3, item 4";
            }
        };

        // Create the factory
        PolicyValueFactory factory = new DefaultPolicyValueFactory(accessor);

        // Call the creation method on the factory
        PolicyValue value = factory.createPolicyValue(device, "policyname");

        // Check test outcome
        assertNotNull("Policy value should exist", value);
        assertTrue("Based on test setup expected a OrderedSetPolicyValue",
                   value instanceof OrderedSetPolicyValue);
        OrderedSetPolicyValue orderedValue = (OrderedSetPolicyValue) value;
        List valueList = orderedValue.getValueAsList();
        assertNotNull("Should be a list of values", valueList);
        assertEquals("Should be 4 items in the list", valueList.size(), 4);

        for (int i=0; i< 4; i++) {
            String item = "item " + (i + 1);
            Object o = valueList.get(i);
            assertNotNull(item + " should not be null", o);
            assertTrue(item + " is not TextPolicyValue",
                    o instanceof TextPolicyValue);
            TextPolicyValue text = (TextPolicyValue) o;
            assertEquals(item + " incorrect", item, text.getValueAsString());
        }
    }

    /**
     * This tests the creation of a structure policy value.  This uses the
     * public method {@link PolicyValueFactory#createPolicyValue} but will
     * exercise the structure value code.
     */
    public void testCreateStructurePolicyValue() throws Exception {

        // Create the structure policy type for use in the tests
        DefaultTextPolicyType textType = new DefaultTextPolicyType();
        DefaultBooleanPolicyType booleanType = new DefaultBooleanPolicyType();
        DefaultIntPolicyType intType = new DefaultIntPolicyType();
        DefaultStructurePolicyType type = new DefaultStructurePolicyType();
        type.addFieldType("field1", textType);
        type.addFieldType("field2", booleanType);
        type.addFieldType("field3", intType);
        type.addFieldType("field4", intType);
        type.complete();

        // Create simple accessor for use in the tests
        TestPolicyDescriptorAccessor accessor =
                new TestPolicyDescriptorAccessor();
        accessor.addPolicyDescriptor(null, "policyname.field1", textType);
        accessor.addPolicyDescriptor(null, "policyname.field2", booleanType);
        accessor.addPolicyDescriptor(null, "policyname.field3", intType);
        accessor.addPolicyDescriptor(null, "policyname.field4", intType);
        accessor.addPolicyDescriptor(null, "policyname", type);

        // Create a device to pass into the factory
        final Map policies = new HashMap();
        policies.put("policyname.field1", "Some text");
        policies.put("policyname.field2", "true");
        policies.put("policyname.field3", "100");
        DefaultDevice device = new DefaultDevice(null, policies, null);

        // Create the factory
        PolicyValueFactory factory = new DefaultPolicyValueFactory(accessor);

        // Call the creation method on the factory
        PolicyValue value = factory.createPolicyValue(device, "policyname");

        // Check test outcome
        assertNotNull("Policy value should exist", value);
        assertTrue("Based on test setup expected a StructurePolicyValue",
                   value instanceof StructurePolicyValue);
        StructurePolicyValue structureValue = (StructurePolicyValue) value;
        Map fields = structureValue.getFieldValuesAsMap();
        assertNotNull("Should be a map of values", fields);
        assertEquals("Should be 4 items in the map", fields.size(), 4);

        PolicyValue value1 = (PolicyValue) fields.get("field1");
        assertNotNull("field1 should not be null", value1);
        PolicyValue value2 = (PolicyValue) fields.get("field2");
        assertNotNull("field2 should not be null", value2);
        PolicyValue value3 = (PolicyValue) fields.get("field3");
        assertNotNull("field3 should not be null", value3);
        PolicyValue value4 = (PolicyValue) fields.get("field4");
        assertNull("field4 should be null", value4);

        assertTrue("field1 should be text value",
                value1 instanceof TextPolicyValue);
        assertTrue("field2 should be boolean value",
                value2 instanceof BooleanPolicyValue);
        assertTrue("field3 should be int value",
                value3 instanceof IntPolicyValue);

        assertEquals("field1 incorrect", "Some text", value1.getAsString());
        assertEquals("field2 incorrect", "true", value2.getAsString());
        assertEquals("field3 incorrect", "100", value3.getAsString());
    }

    /**
     * Test the constructor.
     */
    public void testPolicyValueFactory() throws Exception {
        try {
            new DefaultPolicyValueFactory(null);
            fail("Expected an IllegalArgument Exception.");
        } catch (IllegalArgumentException e) {
            // ignored
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6707/1	pduffin	VBM:2005011710 Refactored device repository API to fix couple of performance and code duplication issues. Added support for retrieving device policy values as meta data

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 08-Oct-04	5755/1	geoff	VBM:2004092209 NullPointerException thrown when Accessing DeviceRepository API

 03-Sep-04	5408/1	byron	VBM:2004090109 ClassCastException when calling getRealPolicyValue()

 03-Sep-04	5387/1	byron	VBM:2004090109 ClassCastException when calling getRealPolicyValue()

 30-Jul-04	4993/1	geoff	VBM:2004072804 Public API for Device Repository: Final cleanup and javadoc

 28-Jul-04	4940/1	geoff	VBM:2004072103 Public API for Device Repository (umbrella)

 28-Jul-04	4952/3	claire	VBM:2004072301 Public API for Device Repository: Provide PolicyValue implementations

 ===========================================================================
*/
