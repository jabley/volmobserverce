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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.policies.theme;

import com.volantis.mcs.devices.InternalDevice;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.devices.InternalDeviceFactory;
import com.volantis.mcs.policies.variants.VariantMock;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.runtime.policies.ActivatedVariablePolicyMock;
import com.volantis.mcs.themes.StyleSheetMock;
import com.volantis.testtools.mock.test.MockTestCaseAbstract;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Collections;

/**
 * Tests for the RuntimeThemeCreator class.
 */
public class RuntimeThemeCreatorTestCase extends MockTestCaseAbstract {

    private static final InternalDeviceFactory INTERNAL_DEVICE_FACTORY =
        InternalDeviceFactory.getDefaultInstance();

    public void testAddParentRules() {

        final ActivatedVariablePolicyMock policyMock =
            new ActivatedVariablePolicyMock("policyMock", expectations);

        final VariantMock deviceVariantMock =
            new VariantMock("deviceVariantMock", expectations);
        deviceVariantMock.expects.getVariantType().returns(
            VariantType.THEME).any();
        final StyleSheetMock deviceStyleSheetMock =
            new StyleSheetMock("deviceStyleSheetMock", expectations);
        deviceStyleSheetMock.expects.getRules().returns(
            Collections.singletonList("device rule")).any();
        final ActivatedThemeContent deviceContent =
            new ActivatedThemeContent(deviceStyleSheetMock, true);
        deviceVariantMock.expects.getContent().returns(deviceContent).any();

        final VariantMock fallbackDeviceVariantMock =
            new VariantMock("fallbackDeviceVariantMock", expectations);
        fallbackDeviceVariantMock.expects.getVariantType().returns(
            VariantType.THEME).any();
        final StyleSheetMock fallbackDeviceStyleSheetMock =
            new StyleSheetMock("fallbackDeviceStyleSheetMock", expectations);
        fallbackDeviceStyleSheetMock.expects.getRules().returns(
            Collections.singletonList("fallback device rule")).any();
        final ActivatedThemeContent fallbackDeviceContent =
            new ActivatedThemeContent(fallbackDeviceStyleSheetMock, true);
        fallbackDeviceVariantMock.expects.getContent().returns(
            fallbackDeviceContent).any();

        policyMock.expects.getDeviceTargetedVariant("device").returns(
            deviceVariantMock).any();
        policyMock.expects.getDeviceTargetedVariant("fallback device").returns(
            fallbackDeviceVariantMock).any();

        final DefaultDevice fallbackDevice =
            new DefaultDevice("fallback device", new HashMap(), null);
        final DefaultDevice device =
            new DefaultDevice("device", new HashMap(), null);
        device.setFallbackDevice(fallbackDevice);

        final DefaultDevice testDefaultDevice =
            new DefaultDevice("test device", new HashMap(), null);
        testDefaultDevice.setFallbackDevice(device);
        final InternalDevice testDevice =
            INTERNAL_DEVICE_FACTORY.createInternalDevice(testDefaultDevice);
        final LinkedList styles = new LinkedList();
        RuntimeThemeCreator.addParentRules(policyMock, testDevice, styles);
        assertEquals(2, styles.size());
        assertEquals("fallback device rule", styles.get(0));
        assertEquals("device rule", styles.get(1));
    }
}
