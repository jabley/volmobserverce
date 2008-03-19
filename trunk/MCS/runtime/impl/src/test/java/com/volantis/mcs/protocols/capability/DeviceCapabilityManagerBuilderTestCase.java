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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.capability;

import com.volantis.mcs.devices.InternalDevice;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.devices.InternalDeviceFactory;
import com.volantis.mcs.devices.policy.values.PolicyValue;
import com.volantis.devrep.repository.api.devices.policy.values.PolicyValueFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Verifies that {@link DeviceCapabilityManagerBuilder} works as expected.
 */
public class DeviceCapabilityManagerBuilderTestCase
        extends TestCaseAbstract {

    private static final InternalDeviceFactory INTERNAL_DEVICE_FACTORY =
        InternalDeviceFactory.getDefaultInstance();

    private DeviceCapabilityManagerBuilder builder;
    private InternalDevice device;
    private HashMap policies;
    private static final String POLICY_NAME = "policyName";
    private static final String FULL = "full";
    private static final String NONE = "none";
    private static final String PARTIAL = "partial";
    private static final String ELEMENT_NAME = "marquee";
    private static final Set ELEMENT_CAPABILITIES = new HashSet();
    private static DeviceElementCapability TEST_CAPABILITY =
            new DeviceElementCapability(ELEMENT_NAME,
                    CapabilitySupportLevel.PARTIAL);

    static {
        TEST_CAPABILITY.addSupportedAttribute(
                DeviceCapabilityConstants.MARQUEE_BGCOLOR_ATT,
                CapabilitySupportLevel.FULL);
        TEST_CAPABILITY.addSupportedAttribute(
                DeviceCapabilityConstants.MARQUEE_BEHAVIOR_ATT,
                CapabilitySupportLevel.FULL);
        TEST_CAPABILITY.addSupportedAttribute(
                DeviceCapabilityConstants.MARQUEE_DIRECTION_ATT,
                CapabilitySupportLevel.NONE);
        ELEMENT_CAPABILITIES.add(TEST_CAPABILITY);
    }

    // Javadoc inherited. 
    protected void setUp() throws Exception {
        super.setUp();
        policies = new HashMap();
        DefaultDevice masterDevice =
            new DefaultDevice(
                "Master", policies,
                new PolicyValueFactory() {
                    public PolicyValue createPolicyValue(
                        DefaultDevice device, String policyName) {
                        return null;
                    }
                });
        device = INTERNAL_DEVICE_FACTORY.createInternalDevice(masterDevice);
        builder = new DeviceCapabilityManagerBuilder(device);
    }

    /**
     * Utility helper method to avoid code duplication.
     *
     * @param expected capability support level
     */
    private DeviceCapabilityManager doTest(CapabilitySupportLevel expected) {
        DeviceCapabilityManager manager = builder.build();
        CapabilitySupportLevel level = builder.getSupportLevel(POLICY_NAME);
        assertEquals(expected, level);
        return manager;
    }

    /**
     * Verify that the string "full" is interpreted as
     * {@link CapabilitySupportLevel.FULL}.
     */
    public void testGetFullSupportLevel() {

        policies.put(POLICY_NAME, FULL);
        doTest(CapabilitySupportLevel.FULL);
    }

    /**
     * Verify that the string "none" is interpreted as
     * {@link CapabilitySupportLevel.NONE}.
     */
    public void testGetNoneSupportLevel() {

        policies.put(POLICY_NAME, NONE);
        doTest(CapabilitySupportLevel.NONE);
    }

    /**
     * Verify that the string "partial" is interpreted as
     * {@link CapabilitySupportLevel.PARTIAL}.
     */
    public void testGetPartialSupportLevel() {

        policies.put(POLICY_NAME, PARTIAL);
        doTest(CapabilitySupportLevel.PARTIAL);
    }

    /**
     * Verify that the default device element capability information is used
     * if nothing more specific is specified in the device repository.
     */
    public void testDefaultValuesUsed() {
        builder.addDefaultValues(ELEMENT_CAPABILITIES);
        DeviceCapabilityManager manager = builder.build();
        DeviceElementCapability dec =
                manager.getDeviceElementCapability(ELEMENT_NAME, true);
        assertEquals(CapabilitySupportLevel.PARTIAL,
                dec.getElementSupportLevel());
        assertEquals(CapabilitySupportLevel.FULL, dec.getSupportType(
                DeviceCapabilityConstants.MARQUEE_BEHAVIOR_ATT));
        assertEquals(CapabilitySupportLevel.FULL, dec.getSupportType(
                DeviceCapabilityConstants.MARQUEE_BGCOLOR_ATT));
        assertEquals(CapabilitySupportLevel.NONE, dec.getSupportType(
                DeviceCapabilityConstants.MARQUEE_DIRECTION_ATT));
        assertNull(dec.getSupportType(
                DeviceCapabilityConstants.MARQUEE_LOOP_ATT));
    }

    /**
     * Verify that the default device element capability information is
     * overwritten if more specific values are specified in the device repository.
     */
    public void testDefaultValuesOverwritten() {
        policies.put(DeviceCapabilityConstants.MARQUEE_SUPPORTED, FULL);
        policies.put(DeviceCapabilityConstants.MARQUEE_BEHAVIOR, FULL);
        policies.put(DeviceCapabilityConstants.MARQUEE_BGCOLOR, FULL);
        policies.put(DeviceCapabilityConstants.MARQUEE_DIRECTION, FULL);
        policies.put(DeviceCapabilityConstants.MARQUEE_LOOP, FULL);

        builder.addDefaultValues(ELEMENT_CAPABILITIES);
        DeviceCapabilityManager manager = builder.build();
        DeviceElementCapability dec =
                manager.getDeviceElementCapability(ELEMENT_NAME, true);
        assertEquals(CapabilitySupportLevel.FULL,
                dec.getElementSupportLevel());
        assertEquals(CapabilitySupportLevel.FULL, dec.getSupportType(
                DeviceCapabilityConstants.MARQUEE_BEHAVIOR_ATT));
        assertEquals(CapabilitySupportLevel.FULL, dec.getSupportType(
                DeviceCapabilityConstants.MARQUEE_BGCOLOR_ATT));
        assertEquals(CapabilitySupportLevel.FULL, dec.getSupportType(
                DeviceCapabilityConstants.MARQUEE_DIRECTION_ATT));
        assertEquals(CapabilitySupportLevel.FULL, dec.getSupportType(
                DeviceCapabilityConstants.MARQUEE_LOOP_ATT));
    }
}
