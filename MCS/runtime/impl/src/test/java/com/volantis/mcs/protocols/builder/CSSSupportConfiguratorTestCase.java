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

package com.volantis.mcs.protocols.builder;

import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.devices.InternalDeviceFactory;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.devrep.repository.api.devices.policy.values.PolicyValueFactory;
import com.volantis.mcs.devices.policy.values.PolicyValue;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.HashMap;
import java.util.Map;

public class CSSSupportConfiguratorTestCase extends TestCaseAbstract {

    private static final InternalDeviceFactory INTERNAL_DEVICE_FACTORY =
        InternalDeviceFactory.getDefaultInstance();

    /**
     * Verify that empty remappable stuff returns an empty map and not null -
     * essentially a Null Object implementation.
     */
    public void testRemappableElementsIsNonNull() {
        final String ssversion = DevicePolicyConstants.CSS_WAP;
        final DefaultDevice device = createDevice("Master", ssversion, null);

        final CSSSupportConfigurator configurator =
            new CSSSupportConfigurator(
                INTERNAL_DEVICE_FACTORY.createInternalDevice(device));
        final Map remappableElements = configurator.getFallbackAttributeExpressions();

        assertNotNull(remappableElements);
        assertTrue(remappableElements.isEmpty());
    }

    /**
     * Verify that a policy defining no CSS support for a particular element
     * and CSS property requires a policy defining a corresponding expression
     * to cause the remappableElements structure to contain anything.
     */
    public void testRemappableElementsWithNoExpressionsDefined() {
        final String ssversion = DevicePolicyConstants.CSS_WAP;
        final DefaultDevice device = createDevice("Master", ssversion, null);

        final CSSSupportConfigurator configurator =
            new CSSSupportConfigurator(
                INTERNAL_DEVICE_FACTORY.createInternalDevice(device));
        final Map remappableElements = configurator.
                getFallbackAttributeExpressions();

        assertNotNull(remappableElements);
        assertTrue(remappableElements.isEmpty());
    }

    /**
     * Verify that a policy defining no CSS support and a corresponding
     * attribute is picked up.
     */
    public void testRemappableElementsWithASingleExpressionDefined() {
        final Map policies = new HashMap();
        final String expression = "length(css('width'),'px')";
        policies.put("x-element.img.attribute.foo.expression", expression);
        final String ssversion = DevicePolicyConstants.CSS_WAP;
        policies.put("ssversion", ssversion);
        final DefaultDevice device = createDevice("Master", policies, null);

        final CSSSupportConfigurator configurator =
            new CSSSupportConfigurator(
                INTERNAL_DEVICE_FACTORY.createInternalDevice(device));
        final Map remappableElements = configurator.getFallbackAttributeExpressions();

        assertNotNull(remappableElements);
        assertFalse(remappableElements.isEmpty());
        assertEquals(1, remappableElements.size());
        assertTrue(remappableElements.containsKey("img"));

        final Map expressions = (Map) remappableElements.get("img");
        assertNotNull(expressions);
        assertFalse(expressions.isEmpty());
        assertTrue(expressions.containsKey("foo"));
        assertEquals(expression, (String) expressions.get("foo"));
    }

    /**
     * After discussion with Paul, the very existence of a policy containing a
     * valid expression should cause a remappable expression rule to be
     * created; i.e. it should not require a
     * x-element.%element-name%.supports.css.%css-property% set to "none" or
     * "partial" to cause for an expression search to happen.
     */
    public void testRemappableExpressionOnlyRequiresExpressionPolicy() {
        final Map policies = new HashMap();
        final String expression = "length(css('width'),'px')";
        policies.put("x-element.img.attribute.foo.expression", expression);
        final String ssversion = DevicePolicyConstants.CSS_WAP;
        policies.put("ssversion", ssversion);
        final DefaultDevice device = createDevice("Master", policies, null);

        final CSSSupportConfigurator configurator =
            new CSSSupportConfigurator(
                INTERNAL_DEVICE_FACTORY.createInternalDevice(device));
        final Map remappableElements = configurator.getFallbackAttributeExpressions();

        assertNotNull(remappableElements);
        assertFalse(remappableElements.isEmpty());
        assertEquals(1, remappableElements.size());
        assertTrue(remappableElements.containsKey("img"));


        final Map expressions = (Map) remappableElements.get("img");
        assertNotNull(expressions);
        assertFalse(expressions.isEmpty());
        assertEquals(1, expressions.size());
        assertTrue(expressions.containsKey("foo"));
        assertEquals(expression, expressions.get("foo"));
    }

    public void testRemappableElementsWithMultipleExpressions()  throws Exception {
        final Map policies = new HashMap();
        final String fooExpression = "length(css('width'),'px')";
        policies.put("x-element.img.attribute.foo.expression", fooExpression);
        final String barExpression = "length(css('width'),'px')";
        policies.put("x-element.img.attribute.bar.expression", barExpression);
        final String ssversion = DevicePolicyConstants.CSS_WAP;
        policies.put("ssversion", ssversion);
        final DefaultDevice device = createDevice("Master", policies, null);

        final CSSSupportConfigurator configurator =
            new CSSSupportConfigurator(
                INTERNAL_DEVICE_FACTORY.createInternalDevice(device));
        final Map remappableElements = configurator.getFallbackAttributeExpressions();

        assertNotNull(remappableElements);
        assertFalse(remappableElements.isEmpty());
        assertEquals(1, remappableElements.size());
        assertTrue(remappableElements.containsKey("img"));

        final Map expressions = (Map) remappableElements.get("img");

        assertNotNull(expressions);
        assertFalse(expressions.isEmpty());
        assertEquals("Contains only a foo and a bar mapping",
                2, expressions.size());
        assertTrue(expressions.containsKey("foo"));
        assertEquals(fooExpression, (String) expressions.get("foo"));
        assertTrue(expressions.containsKey("bar"));
        assertEquals(barExpression, (String) expressions.get("bar"));
    }

    /**
     * Verify that the inherited policy doesn't override the policy closest in
     * scope.
     */
    public void testInheritanceOfRemappableElements() {

        // Create a vaguely realistic device hierarchy subset:
        // +-+ Master (none)
        //   +-+ Mobile  (none)
        //     +-+ Handset (none)
        //       +-+ XHTML-Handset (CSS1)
        //         +-+ Samsung-XHTML (CSS1)
        //           +-+ Samsung-SprintPCS (CSS1)
        //             +-+ Samsung-SPH-A600 (CSS2)

        final DefaultDevice master = createDevice("Master", "none", null);
        final DefaultDevice mobile = createDevice("Mobile", "none", master);
        final DefaultDevice handset = createDevice("Handset", "ll ", mobile);
        final DefaultDevice xhtmlHandset = createDevice("XHTML-Handset",
                DevicePolicyConstants.CSS1, handset);
        final DefaultDevice samsungXHTML = createDevice("Samsung-XHTML",
                DevicePolicyConstants.CSS1, xhtmlHandset);
        final DefaultDevice samsungSprintPCs = createDevice("Samsung-SprintPCS",
                DevicePolicyConstants.CSS1, samsungXHTML);

        final Map samsungA600Policies = new HashMap();
        final String fooExpression = "length(css('width'),'px')";
        samsungA600Policies.put(
            "x-element.img.attribute.foo.expression", fooExpression);
        final String barExpression = "length(css('height'),'px')";
        samsungA600Policies.put(
            "x-element.img.attribute.bar.expression", barExpression);
        samsungA600Policies.put("ssversion", DevicePolicyConstants.CSS2);
        final DefaultDevice samsungA600 = createDevice("Samsung-SPH-A600",
            samsungA600Policies, samsungSprintPCs);

        final CSSSupportConfigurator configurator =
            new CSSSupportConfigurator(
                INTERNAL_DEVICE_FACTORY.createInternalDevice(samsungA600));
        final Map remappableElements = configurator.getFallbackAttributeExpressions();

        assertNotNull(remappableElements);
        assertFalse(remappableElements.isEmpty());
        assertEquals(1, remappableElements.size());
        assertTrue(remappableElements.containsKey("img"));

        final Map expressions = (Map) remappableElements.get("img");

        assertNotNull(expressions);
        assertFalse(expressions.isEmpty());
        assertEquals("Contains a width and height attribute mapping",
                2, expressions.size());
        assertTrue(expressions.containsKey("foo"));
        assertEquals(fooExpression, (String) expressions.get("foo"));
        assertTrue(expressions.containsKey("bar"));
        assertEquals(barExpression, (String) expressions.get("bar"));
    }

    /**
     * Create a device with the supplied name, style sheet version and parent.
     *
     * @param name the name of the device.
     * @param ssversion the style sheet version for the device.
     * @param parent the parent device, or null.
     * @return the device created.
     */
    DefaultDevice createDevice(String name, String ssversion,
            DefaultDevice parent) {

        HashMap policies = new HashMap();
        policies.put("ssversion", ssversion);
        return createDevice(name, policies, parent);
    }

    DefaultDevice createDevice(String name, Map policyValues,
            DefaultDevice parent) {

        HashMap policies = new HashMap();
        policies.putAll(policyValues);
        DefaultDevice device = new DefaultDevice(name, policies,
            new PolicyValueFactory() {
                public PolicyValue createPolicyValue(
                    DefaultDevice device, String policyName) {
                    return null;
                }
            });
        if (parent != null) {
            device.setFallbackDevice(parent);
        }
        return device;
    }
}
