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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.builder;

import com.volantis.mcs.css.version.CSSProperty;
import com.volantis.mcs.css.version.CSSVersion;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.InternalDeviceFactory;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.devrep.repository.api.devices.policy.values.PolicyValueFactory;
import com.volantis.mcs.devices.policy.values.PolicyValue;
import com.volantis.mcs.protocols.ProtocolConfigurationImpl;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleShorthands;
import com.volantis.mcs.themes.StyleSyntaxes;
import com.volantis.mcs.themes.properties.VerticalAlignKeywords;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.Level;

import java.util.HashMap;
import java.util.Map;

/**
 * A test case for {@link CSSConfigurator}.
 */
public class CSSConfiguratorTestCase extends TestCaseAbstract {

    // Declare a Category for the packages you want to log during test.
    private static final Category category = Category.getInstance(
            "com.volantis.mcs.protocols");

    // The name of the color triplets device policy.
    private static final String COLOR_TRIPLETS =
            "x-css.syntax.supports.color.triplets";

    private static final InternalDeviceFactory INTERNAL_DEVICE_FACTORY =
        InternalDeviceFactory.getDefaultInstance();

    private CSSConfigurator cssConfigurator = new CSSConfigurator();
    private ProtocolConfigurationImpl config = new ProtocolConfigurationImpl();

    protected void setUp() throws Exception {
        Category.getRoot().removeAllAppenders();
        BasicConfigurator.configure();
        // Tests by default have logging turned off
        Category.getRoot().setLevel(Level.OFF);
    }

    protected void tearDown() throws Exception {
        Category.shutdown();
    }

    /**
     * Test that we can add a property from the config per the device.
     */
    public void testPropertyAddition() {
        // (Use this in the test you want to debug)
        // Tell log4j to log everything for your test packages.
        category.setLevel(Level.ALL);

        // Test adding border-spacing to CSS Mobile Profile.
        // This is what XHTMLBasic MIB2.1 and Netfront3 must do.
        final String ssversion = DevicePolicyConstants.CSS_MOBILE_PROFILE1;
        final StyleProperty property = StylePropertyDetails.BORDER_SPACING;
        final String policy = createCssPropertyPolicyName(property);
        DefaultDevice device = createDevice("Master", ssversion, null);

        // Test the default setup, it shouldn't have border-spacing.
        cssConfigurator.initialise(config,
            INTERNAL_DEVICE_FACTORY.createInternalDevice(device));
        assertNull(config.getCssVersion().getProperty(property));

        // Add border-spacing and test again, it should have it.
        final Map policies = new HashMap();
        policies.put("ssversion", ssversion);
        policies.put(policy, "full");
        device = createDevice("Master", policies, null);
        cssConfigurator.initialise(config,
            INTERNAL_DEVICE_FACTORY.createInternalDevice(device));
        assertNotNull(config.getCssVersion().getProperty(property));
    }

    /**
     * Test that we can remove a property from the config per the device.
     */
    public void testPropertyRemoval() {
        // (Use this in the test you want to debug)
        // Tell log4j to log everything for your test packages.
        category.setLevel(Level.ALL);

        // Test removing background-attachment from CSS Mobile Profile.
        final String ssversion = DevicePolicyConstants.CSS_MOBILE_PROFILE1;
        final StyleProperty property = StylePropertyDetails.BACKGROUND_ATTACHMENT;
        final String policy = createCssPropertyPolicyName(property);
        DefaultDevice device = createDevice("Master", ssversion, null);

        // Test the default setup, it should have background-attachment.
        cssConfigurator.initialise(config,
            INTERNAL_DEVICE_FACTORY.createInternalDevice(device));
        assertNotNull(config.getCssVersion().getProperty(
                property));

        // Remove background-attachment and test again, it should be gone.
        final Map policies = new HashMap();
        policies.put("ssversion", ssversion);
        policies.put(policy, "none");
        device = createDevice("Master", policies, null);

        cssConfigurator.initialise(config,
            INTERNAL_DEVICE_FACTORY.createInternalDevice(device));
        assertNull(config.getCssVersion().getProperty(
                property));
    }

    /**
     */
    public void testShorthands() {
        // (Use this in the test you want to debug)
        // Tell log4j to log everything for your test packages.
        category.setLevel(Level.ALL);

        final String ssversion = DevicePolicyConstants.CSS_WAP;
        DefaultDevice device = createDevice("Master", ssversion, null);
        CSSVersion cssVersion;

        // Test the default setup for CSSWAP.
        cssConfigurator.initialise(config,
            INTERNAL_DEVICE_FACTORY.createInternalDevice(device));
        cssVersion = config.getCssVersion();
        // It should not have margin.
        assertFalse(cssVersion.supportsShorthand(StyleShorthands.MARGIN));
        // It should have font.
        assertTrue(cssVersion.supportsShorthand(StyleShorthands.FONT));

        // Add margin and test again
        final Map policies = new HashMap();
        policies.put("ssversion", ssversion);
        policies.put("x-css.shorthands.margin.support", "full");
        device = createDevice("Master", policies, null);
        cssConfigurator.initialise(config,
            INTERNAL_DEVICE_FACTORY.createInternalDevice(device));
        cssVersion = config.getCssVersion();
        // It should have margin now.
        assertTrue(cssVersion.supportsShorthand(StyleShorthands.MARGIN));
        // It should still have font.
        assertTrue(cssVersion.supportsShorthand(StyleShorthands.FONT));
    }

    /**
     * test modifying CSS MP vertical-align property, adding top, bottom and
     * removing super, sub
     */
    public void testKeywords() {
        // (Use this in the test you want to debug)
        // Tell log4j to log everything for your test packages.
        category.setLevel(Level.ALL);

        final String ssversion = DevicePolicyConstants.CSS_MOBILE_PROFILE1;
        DefaultDevice device = createDevice("Master", ssversion, null);
        CSSProperty cssProperty;

        // Test the default setup for CSSWAP.
        cssConfigurator.initialise(config,
            INTERNAL_DEVICE_FACTORY.createInternalDevice(device));
        cssProperty = config.getCssVersion().getProperty(
                StylePropertyDetails.VERTICAL_ALIGN);

        // It should not have top or bottom.
        assertFalse(cssProperty.supportsKeyword(VerticalAlignKeywords.TOP));
        assertFalse(cssProperty.supportsKeyword(VerticalAlignKeywords.BOTTOM));
        // It should have super and sub.
        assertTrue(cssProperty.supportsKeyword(VerticalAlignKeywords.SUPER));
        assertTrue(cssProperty.supportsKeyword(VerticalAlignKeywords.SUB));

        final Map policies = new HashMap();
        policies.put("ssversion", ssversion);
        // Add top and bottom.
        policies.put(
            "x-css.properties.vertical-align.keyword.top.support", "full");
        policies.put(
            "x-css.properties.vertical-align.keyword.bottom.support", "full");
        // Remove super and sub.
        policies.put(
            "x-css.properties.vertical-align.keyword.super.support", "none");
        policies.put(
            "x-css.properties.vertical-align.keyword.sub.support", "none");
        device = createDevice("Master", policies, null);

        cssConfigurator.initialise(config,
            INTERNAL_DEVICE_FACTORY.createInternalDevice(device));
        cssProperty = config.getCssVersion().getProperty(
                StylePropertyDetails.VERTICAL_ALIGN);
        // It should have top and bottom.
        assertTrue(cssProperty.supportsKeyword(VerticalAlignKeywords.TOP));
        assertTrue(cssProperty.supportsKeyword(VerticalAlignKeywords.BOTTOM));
        // It should not have super or sub.
        assertFalse(cssProperty.supportsKeyword(VerticalAlignKeywords.SUPER));
        assertFalse(cssProperty.supportsKeyword(VerticalAlignKeywords.SUB));
    }

    /**
     * Test that we only inherit property definitions from those ancestor
     * devices which share the same style sheet version.
     */
    public void testPropertyInheritance() {
        // (Use this in the test you want to debug)
        // Tell log4j to log everything for your test packages.
        category.setLevel(Level.ALL);

        // Use mcs-line-gap as that is not enabled in any CSS version
        // as it is internal.
        final StyleProperty property = StylePropertyDetails.MCS_LINE_GAP;
        final String policy = createCssPropertyPolicyName(property);

        // Create a vaguely realistic device hierarchy subset:
        // +-+ Master (none)
        //   +-+ PC  (CSS1)
        //     +-+ PC-Win32-IE5 (CSS2)
        //       +-+ PC-Win32IE5.5 (CSS2)

        final Map masterPolicies = new HashMap();
        masterPolicies.put("ssversion", "none");
        // Enable mcs-line-gap for Master, this *should not* be inherited.
        masterPolicies.put(policy, "full");
        DefaultDevice master = createDevice("Master", masterPolicies, null);

        final Map pcPolicies = new HashMap();
        pcPolicies.put("ssversion", DevicePolicyConstants.CSS1);
        DefaultDevice pc = createDevice("PC", pcPolicies, master);

        final Map pcWin32IE5Policies = new HashMap();
        pcWin32IE5Policies.put("ssversion", DevicePolicyConstants.CSS2);
        DefaultDevice pcWin32IE5 =
            createDevice("PC-Win32-IE5", pcWin32IE5Policies, pc);

        final Map pcWin32IE55Policies = new HashMap();
        pcWin32IE55Policies.put("ssversion", DevicePolicyConstants.CSS2);
        DefaultDevice pcWin32IE55 =
            createDevice("PC-Win32-IE5.5", pcWin32IE55Policies, pcWin32IE5);

        cssConfigurator.initialise(config,
            INTERNAL_DEVICE_FACTORY.createInternalDevice(pcWin32IE55));
        assertNull("Master property should not be inherited",
                config.getCssVersion().getProperty(property));

        // Enable mcs-line-gap for PC, this *should not* be inherited.
        pcPolicies.put(policy, "full");
        pc = createDevice("PC", pcPolicies, master);
        pcWin32IE5 = createDevice("PC-Win32-IE5", pcWin32IE5Policies, pc);
        pcWin32IE55 = createDevice("PC-Win32-IE5.5", pcWin32IE55Policies, pcWin32IE5);
        cssConfigurator.initialise(config,
            INTERNAL_DEVICE_FACTORY.createInternalDevice(pcWin32IE55));
        assertNull("PC property should not be inherited",
                config.getCssVersion().getProperty(property));

        // Enable mcs-line-gap for PC-Win32-IE5, this *should* be inherited
        // as it shares CSS2 with PC-Win32-IE5.5.
        pcWin32IE5Policies.put(policy, "full");
        pcWin32IE5 = createDevice("PC-Win32-IE5", pcWin32IE5Policies, pc);
        pcWin32IE55 = createDevice("PC-Win32-IE5.5", pcWin32IE55Policies, pcWin32IE5);
        cssConfigurator.initialise(config,
            INTERNAL_DEVICE_FACTORY.createInternalDevice(pcWin32IE55));
        assertNotNull("PC-Win32-IE5 property should be inherited",
                config.getCssVersion().getProperty(property));
    }

    /**
     * Verify that device specific syntax values cause the CSSVersion to be
     * updated.
     */
    public void testSyntaxes() {
        // (Use this in the test you want to debug)
        // Tell log4j to log everything for your test packages.
        category.setLevel(Level.ALL);

        final String ssversion = DevicePolicyConstants.CSS_WAP;
        DefaultDevice device = createDevice("Master", ssversion, null);
        CSSVersion cssVersion;

        // Test the default setup for CSSWAP.
        cssConfigurator.initialise(config,
            INTERNAL_DEVICE_FACTORY.createInternalDevice(device));
        cssVersion = config.getCssVersion();

        // It should support the color.triplets syntax by default (explicitly
        // added in the CSS*VersionFactory).
        assertTrue(cssVersion.supportsSyntax(StyleSyntaxes.COLOR_TRIPLETS));

        // Explicitly add full support for the color.triplets syntax and
        // test again.
        final Map policies = new HashMap();
        policies.put("ssversion", ssversion);
        policies.put(COLOR_TRIPLETS, "full");
        device = createDevice("Master", policies, null);
        cssConfigurator.initialise(config,
            INTERNAL_DEVICE_FACTORY.createInternalDevice(device));
        cssVersion = config.getCssVersion();

        // It should have syntax values now.
        assertTrue(cssVersion.supportsSyntax(StyleSyntaxes.COLOR_TRIPLETS));

        // Explicitly set color.triplets syntax support to none and test again
        policies.put(COLOR_TRIPLETS, "none");
        device = createDevice("Master", policies, null);
        cssConfigurator.initialise(config,
            INTERNAL_DEVICE_FACTORY.createInternalDevice(device));
        cssVersion = config.getCssVersion();

        // It should have syntax values now.
        assertFalse(cssVersion.supportsSyntax(StyleSyntaxes.COLOR_TRIPLETS));
    }

    public void testInitialiseStylePseudoSelectors() {
        // (Use this in the test you want to debug)
        // Tell log4j to log everything for your test packages.
        category.setLevel(Level.ALL);

        final String ssversion = DevicePolicyConstants.CSS_WAP;
        DefaultDevice device = createDevice("Master", ssversion, null);
        CSSVersion cssVersion;

        // Test the default setup for CSSWAP.
        cssConfigurator.initialise(config,
               INTERNAL_DEVICE_FACTORY.createInternalDevice(device));
        cssVersion = config.getCssVersion();
        // It should not have hover.
        assertFalse(cssVersion.supportsPseudoSelectorId("hover"));
        // It should have link.
        assertTrue(cssVersion.supportsPseudoSelectorId("link"));

        // Add support for hover and test again
        final Map policies = new HashMap();
        policies.put("ssversion", DevicePolicyConstants.CSS2);
        policies.put("x-css.selectors.hover.support", "full");
        device = createDevice("Master", policies, null);
        cssConfigurator.initialise(config,
               INTERNAL_DEVICE_FACTORY.createInternalDevice(device));
        cssVersion = config.getCssVersion();
        // It should have hover now.
        assertTrue(cssVersion.supportsPseudoSelectorId("hover"));
        // It should still have link.
        assertTrue(cssVersion.supportsPseudoSelectorId("link"));
    }

    /**
     * Create the device policy name for individual css property support.
     *
     * @param property the property to create the policy name for.
     * @return the device policy name created.
     */
    String createCssPropertyPolicyName(final StyleProperty property) {

        return "x-css.properties." + property.getName() + ".support";
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

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10835/1	geoff	VBM:2005121405 P900; superscript does not work with CssMobleProfile

 14-Dec-05	10829/2	geoff	VBM:2005121405 P900; superscript does not work with CssMobleProfile

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 ===========================================================================
*/
