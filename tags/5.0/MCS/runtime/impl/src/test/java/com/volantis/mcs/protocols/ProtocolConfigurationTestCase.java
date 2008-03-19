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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/ProtocolConfigurationTestCase.java,v 1.3 2003/03/17 11:21:44 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Feb-03    Byron           VBM:2003021306 - Created
 * 13-Mar-03    Phil W-S        VBM:2003031110 - Remove the suite() and main()
 *                              methods as required by the new unit test
 *                              conventions.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.css.version.CSSPropertyMock;
import com.volantis.mcs.css.version.CSSVersion;
import com.volantis.mcs.css.version.CSSVersionMock;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.InternalDeviceMock;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.capability.CapabilitySupportLevel;
import com.volantis.mcs.protocols.capability.DeviceCapabilityConstants;
import com.volantis.mcs.protocols.capability.DeviceCapabilityManager;
import com.volantis.mcs.protocols.capability.DeviceCapabilityManagerBuilder;
import com.volantis.mcs.protocols.capability.DeviceElementCapability;
import com.volantis.mcs.protocols.css.emulator.renderer.StyleEmulationPropertiesRenderer;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.themes.properties.MCSMarqueeDirectionKeywords;
import com.volantis.mcs.themes.properties.MCSMarqueeRepetitionKeywords;
import com.volantis.mcs.themes.properties.MCSMarqueeStyleKeywords;
import com.volantis.mcs.themes.properties.MCSTextBlinkKeywords;
import com.volantis.mcs.themes.properties.MCSTextLineThroughStyleKeywords;
import com.volantis.mcs.themes.properties.MCSTextUnderlineStyleKeywords;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.properties.StylePropertyDefinitions;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.Set;

/**
 * Test the ProtocolConfiguration. Note any subclasses of ProtocolConfiguration
 * should ensure they subclass this test case.
 *
 * @author Byron Wild
 */
public class ProtocolConfigurationTestCase extends TestCaseAbstract {
    protected ProtocolConfigurationImpl config;

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    /**
     * Mock {@link CSSVersion} which specifies which styles are supported, and
     * therefore which will require emulation.
     *
     */
    private CSSVersionMock cssVersion;

    /**
     * Mock {@link com.volantis.mcs.devices.InternalDevice} which is used to
     * specify what emulation elements (e.g. blink) are supported.
     */
    protected InternalDeviceMock device;

    /**
     * Builds the device capability manager using configuration information
     * from the device and the protocol.
     */
    protected DeviceCapabilityManagerBuilder builder;

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();
        config = createProtocolConfiguration();
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        config = null;

        super.tearDown();
    }

    /**
     * Test the getting of the candidate url in the following steps:
     * <ul>
     * <li>No asset url locations</li>
     * <li>One asset url location</li>
     * <li>Element has name</li>
     * <li>Element has attribute which isn't in the map</li>
     * <li>Element has attribute which is in the map</li>
     * <li>Two asset urls in map (with same element name</li>
     * </ul>
     *
     * @todo later this method should be updated to reflect the changes
     * made in ProtocolConfiguration.
     * @throws Exception if any exception occurs.
     */
    public void testAddCandidateElementAssetURLs() throws Exception {
        // Preconditions for this test.
        // 1. protocol configuration is created
        // 2. the element parameter must not be null
        // 3. the element must have a name
	/*
        String result = null;;
        Element element = new Element();
        config.addCandidateElementAssetURLs(element, null);
        assertNull("Result should be null (no asset URL locations)", result);

        // Setup a assetURLLocation for 'table'->'width'
        config.assetURLLocations.add("table", "width");
        result = config.getCandidateElementAssetURL(element);
        assertNull("Result should be null (element has no name)", result);

        // Now give the element a name that is in the map.
        element.setName("table");
        result = config.getCandidateElementAssetURL(element);
        assertNull("Result should be null (element has no attributes)", result);

        // Now give the element an attribute which is not in the map.
        element.setAttribute("background-color", "blue");
        result = config.getCandidateElementAssetURL(element);
        assertNull("Result should be null (element has no attributes)", result);

        // Now give the element an attribute which is in the map.
        element.setAttribute("width", "100");
        result = config.getCandidateElementAssetURL(element);
        assertEquals("Result should be equal", "100", result);

        // Now add another pair to the asset URL locations.
        config.assetURLLocations.add("table", "height");
        result = config.getCandidateElementAssetURL(element);
        assertEquals("Result should be equal", "100", result);
	*/
    }

    /**
     * Test the isElementAtomic method with:
     * <ul>
     * <li>Empty list</li>
     * <li>Only one element (should find match iff it is in the list)</li>
     * <li>Only one element (test case-sensitivity search)</li>
     * </ul>
     * @throws Exception as not expected.
     */
    public void testIsElementAtomic() throws Exception {
        String elementName = "table";

        final String ATOMIC = "This element should be atomic";
        final String NOT_ATOMIC = "This element should NOT be atomic";

        // Test if atomicElements has no elements.
        assertTrue("This element should NOT be atomic",
                   !config.isElementAtomic(elementName));

        // Test if atomicElements has one element.
        config.atomicElements.add(elementName);
        assertTrue(ATOMIC, config.isElementAtomic(elementName));
        assertTrue(NOT_ATOMIC, !config.isElementAtomic("not in list"));
        assertTrue(NOT_ATOMIC, !config.isElementAtomic(elementName.toUpperCase()));
    }

    /**
     * Similar to above test.
     * @see #testIsElementAtomic
     * @throws Exception
     */
    public void testIsElementAlwaysEmpty() throws Exception {
        String value = "table";

        final String EMPTY = "This element should be empty";
        final String NOT_EMPTY = "This element should NOT be empty";

        // Test if atomicElements has no elements.
        assertTrue("This element should NOT be empty",
                   !config.isElementAlwaysEmpty(value));

        // Test if atomicElements has one element.
        config.alwaysEmptyElements.add(value);
        assertTrue(EMPTY, config.isElementAlwaysEmpty(value));
        assertTrue(NOT_EMPTY, !config.isElementAlwaysEmpty("not in list"));
        assertTrue(NOT_EMPTY, !config.isElementAlwaysEmpty(value.toUpperCase()));
    }

    /**
     * Test the initial state of this object.
     * @throws Exception
     */
    public void testProtocolConfiguration() throws Exception {
        assertNotNull(config.alwaysEmptyElements);
        assertNotNull(config.atomicElements);
        assertNotNull(config.assetURLLocations);
    }

    /**
     * Tests that style emulation renderers are configured as expected.
     *
     * @param supportsMarquee   true if the device supports the marquee element
     * @param supportsBlink     true if the device supports the blink element
     * @param supportsUnderline
     * @param supportsStrike
     * @param supportsCss       true if the device supports CSS
     * @param elementName       name of the element on which to test
     * @param propertyValues    style information which should be applied to the
     *                          test element
     * @return Element result of rendering the element with the supplied
     * properties and protocol configuration
     */
    protected Element doTest(boolean supportsMarquee, boolean supportsBlink,
                             boolean supportsUnderline, boolean supportsStrike,
                             boolean supportsCss, String elementName,
                             MutablePropertyValues propertyValues) {

        setExpectations(supportsMarquee, supportsBlink, supportsUnderline,
                supportsStrike, supportsCss);
        ProtocolConfigurationImpl configuration = createProtocolConfiguration();
        device.fuzzy.getBooleanPolicyValue(mockFactory.expectsAny())
                .returns(false).any();

        device.fuzzy.getPolicyValue(mockFactory.expectsAny())
                .returns(null).any();

        configuration.setCssVersion(cssVersion);
        configuration.initialize(device, builder);

        // Run test with all the element emulations supported.
        StyleEmulationPropertiesRenderer renderer =
                configuration.getStyleEmulationPropertyRendererSelector();
        Element html = domFactory.createElement();
        html.setName("html");
        Element element = domFactory.createElement();
        element.setName(elementName);
        html.addHead(element);

        renderer.applyProperties(element, propertyValues);

        return element;
    }

    /**
     * Utility method to test underline emulation.
     *
     * @param supportsUnderline     true if the device supports the blink element
     * @param supportsCSS       true if the device supports CSS
     * @return Element result of rendering the element with the supplied
     * properties and protocol configuration
     */
    private Element doUnderlineTest(boolean supportsUnderline,
                                    boolean supportsCSS) {
        MutablePropertyValues properties = createPropertyValues();
        properties.setComputedAndSpecifiedValue(
                StylePropertyDetails.MCS_TEXT_UNDERLINE_STYLE,
                MCSTextUnderlineStyleKeywords.SOLID);
        return doTest(false, false, supportsUnderline, false, supportsCSS, "p",
                properties);
    }

   /**
     * Utility method to test strike emulation.
     *
     * @param supportsStrike    true if the device supports the strike element
     * @param supportsCSS       true if the device supports CSS
     * @return Element result of rendering the element with the supplied
     * properties and protocol configuration
     */
    private Element doStrikeTest(boolean supportsStrike, boolean supportsCSS) {
        MutablePropertyValues properties = createPropertyValues();
        properties.setComputedAndSpecifiedValue(
                StylePropertyDetails.MCS_TEXT_LINE_THROUGH_STYLE,
                MCSTextLineThroughStyleKeywords.SOLID);
        return doTest(false, false, false, supportsStrike, supportsCSS, "p",
                properties);
    }


    /**
     * Utility method to test blink emulation.
     *
     * @param supportsBlink     true if the device supports the blink element
     * @param supportsCSS       true if the device supports CSS
     * @return Element result of rendering the element with the supplied
     * properties and protocol configuration
     */
    private Element doBlinkTest(boolean supportsBlink, boolean supportsCSS) {
        MutablePropertyValues properties = createPropertyValues();
        properties.setComputedAndSpecifiedValue(
                StylePropertyDetails.MCS_TEXT_BLINK,
                MCSTextBlinkKeywords.BLINK);
        return doTest(false, supportsBlink, false, false, supportsCSS, "p",
                properties);
    }

    /**
     * Utility method to test marquee emulation.
     *
     * @param supportsMarquee   true if the device supports the marquee element
     * @param supportsCss       true if the device supports CSS
     * @return Element result of rendering the element with the supplied
     * properties and protocol configuration
     */
    private Element doMarqueeTest(boolean supportsMarquee,
                                  boolean supportsCss,
                                  MutablePropertyValues propertyValues,
                                  String elementName) {
        return doTest(supportsMarquee, false, false, false, supportsCss,
                elementName, propertyValues);
    }

    /**
     * Verify that the underline style is emulated using an element if the
     * device does not support CSS and does support the emulation element.
     */
    public void testUnderlineEmulatedWhenCssNotSupportedAndElementSupported() {

        Element element = doUnderlineTest(true, false);
        assertEquals("p", element.getName());
        Element underline = (Element) element.getHead();
        assertNotNull(underline);
        assertEquals("u", underline.getName());
    }

    /**
     * Verify that the underline style is not emulated using an element when
     * the device supports CSS.
     */
    public void testUnderlineNotEmulatedWhenCssSupported() {
        Element element = doUnderlineTest(true, true);
        assertEquals("p", element.getName());
        assertNull(element.getHead());
    }

    /**
     * Verify that the underline style is not emulated using an element when
     * the device does not support CSS OR the emulation element.
     */
    public void testUnderlineNotEmulatedWhenBothCssAndElementNotSupported() {
        Element element = doUnderlineTest(false, false);
        assertEquals("p", element.getName());
        assertNull(element.getHead());

        element = doUnderlineTest(false, false);
        assertEquals("p", element.getName());
        assertNull(element.getHead());
    }

    /**
     * Verify that the blink style is emulated using an element if the device
     * does not support CSS and does support the emulation element.
     */
    public void testBlinkEmulatedWhenCssNotSupportedAndElementSupported() {

        Element element = doBlinkTest(true, false);
        assertEquals("p", element.getName());
        Element blink = (Element) element.getHead();
        assertNotNull(blink);
        assertEquals("blink", blink.getName());
    }

    /**
     * Verify that the blink style is not emulated using an element when the
     * device supports CSS.
     */
    public void testBlinkNotEmulatedWhenCssSupported() {
        Element element = doBlinkTest(true, true);
        assertEquals("p", element.getName());
        assertNull(element.getHead());
    }

    /**
     * Verify that the blink style is not emulated using an element when
     * the device does not support CSS OR the emulation element.
     */
    public void testBlinkNotEmulatedWhenBothCssAndElementNotSupported() {
        Element element = doBlinkTest(false, false);
        assertEquals("p", element.getName());
        assertNull(element.getHead());
    }

    /**
     * Verify that the blink style is emulated using an element if the device
     * does not support CSS and does support the emulation element.
     */
    public void testStrikeEmulatedWhenCssNotSupportedAndElementSupported() {

        Element element = doStrikeTest(true, false);
        assertEquals("p", element.getName());
        Element strike = (Element) element.getHead();
        assertNotNull(strike);
        assertEquals("strike", strike.getName());
    }

    /**
     * Verify that the blink style is not emulated using an element when the
     * device supports CSS.
     */
    public void testStrikeNotEmulatedWhenCssSupported() {
        Element element = doStrikeTest(true, true);
        assertEquals("p", element.getName());
        assertNull(element.getHead());
    }

    /**
     * Verify that the blink style is not emulated using an element when
     * the device does not support CSS OR the emulation element.
     */
    public void testStrikeNotEmulatedWhenBothCssAndElementNotSupported() {
        Element element = doStrikeTest(false, false);
        assertEquals("p", element.getName());
        assertNull(element.getHead());
    }

    /**
     * Verify that a div element with marquee styles is only emulated using the
     * marquee element if the device DOES NOT support the css AND DOES support
     * the marquee element.
     *
     * @throws Exception if there was a problem running the test
     */
    public void testRenderMarqueeForDivWithStyleSet() throws Exception {

        MutablePropertyValues properties = createPropertyValues();
        properties.setComputedAndSpecifiedValue(
                StylePropertyDetails.MCS_MARQUEE_STYLE,
                MCSMarqueeStyleKeywords.SCROLL);

        // Test that the div element is replaced if the device doesn't support
        // the marquee css and does support the marquee element.
        Element element = doMarqueeTest(true, false, properties, "div");
        assertEquals("marquee", element.getName());
        assertEquals("scroll", element.getAttributeValue("behavior"));

        // Test that the div element is NOT replaced if the device doesn't
        // support the marquee css, but also doesn't support the marquee element.
        element = doMarqueeTest(false, false, properties, "div");
        assertEquals("div", element.getName());
        assertNull(element.getAttributeValue("behavior"));

        // Test that the div element is NOT replaced if the device does
        // support the marquee css, regardless of whether it supports the
        // marquee element.
        element = doMarqueeTest(false, true, properties, "div");
        assertEquals("div", element.getName());
        assertNull(element.getAttributeValue("behavior"));

        // Test that the div element is NOT replaced if the device does
        // support the marquee css, regardless of whether it supports the
        // marquee element.
        element = doMarqueeTest(true, true, properties, "div");
        assertEquals("div", element.getName());
        assertNull(element.getAttributeValue("behavior"));
    }

   /**
     * Verify that a div element with marquee styles is only emulated using the
     * marquee element if the device DOES NOT support the css AND DOES support
     * the marquee element.
     *
     * @throws Exception if there was a problem running the test
     */
    public void testRenderMarqueeForDivWithAllSet() throws Exception {

        MutablePropertyValues properties = createPropertyValues();
        properties.setComputedAndSpecifiedValue(
                StylePropertyDetails.MCS_MARQUEE_STYLE,
                MCSMarqueeStyleKeywords.SCROLL);
        properties.setComputedAndSpecifiedValue(
                StylePropertyDetails.MCS_MARQUEE_DIRECTION,
                MCSMarqueeDirectionKeywords.LEFT);
        properties.setComputedAndSpecifiedValue(
                StylePropertyDetails.MCS_MARQUEE_REPETITION,
                MCSMarqueeRepetitionKeywords.INFINITE);
        properties.setComputedAndSpecifiedValue(
                StylePropertyDetails.BACKGROUND_COLOR, StyleColorNames.ORANGE);

        // Test that the div element is replaced if the device doesn't support
        // the marquee css and does support the marquee element.
        Element element = doMarqueeTest(true, false, properties, "div");
        checkMarqueeElement(element, "marquee", true);

        // Test that the div element is NOT replaced if the device doesn't
        // support the marquee css, but also doesn't support the marquee element.
        element = doMarqueeTest(false, false, properties, "div");
        checkMarqueeElement(element, "div", false);

        // Test that the div element is NOT replaced if the device does
        // support the marquee css, regardless of whether it supports the
        // marquee element.
        element = doMarqueeTest(false, true, properties, "div");
        checkMarqueeElement(element, "div", false);

        // Test that the div element is NOT replaced if the device does
        // support the marquee css, regardless of whether it supports the
        // marquee element.
        element = doMarqueeTest(true, true, properties, "div");
        checkMarqueeElement(element, "div", false);
    }

    /**
     * Verify that a div element without mcs-marquee-style set is never
     * emulated using the marquee element, regardless of the value of the other
     * marquee style properties.
     *
     * @throws Exception if there was a problem running the test
     */
    public void testRenderMarqueeForDivWithStyleNotSet() throws Exception {

        MutablePropertyValues properties = createPropertyValues();
        properties.setComputedAndSpecifiedValue(
                StylePropertyDetails.MCS_MARQUEE_DIRECTION,
                MCSMarqueeDirectionKeywords.LEFT);
        properties.setComputedAndSpecifiedValue(
                StylePropertyDetails.MCS_MARQUEE_REPETITION,
                MCSMarqueeRepetitionKeywords.INFINITE);
        properties.setComputedAndSpecifiedValue(
                StylePropertyDetails.BACKGROUND_COLOR,
                StyleColorNames.ORANGE);

        // Test that the div element is NOT replaced if the device doesn't
        // support the marquee css and does support the marquee element.
        Element element = doMarqueeTest(true, false, properties, "div");
        checkMarqueeElement(element, "div", false);

        // Test that the div element is NOT replaced if the device doesn't
        // support the marquee css, but also doesn't support the marquee element.
        element = doMarqueeTest(false, false, properties, "div");
        checkMarqueeElement(element, "div", false);

        // Test that the div element is NOT replaced if the device does
        // support the marquee css, regardless of whether it supports the
        // marquee element.
        element = doMarqueeTest(false, true, properties, "div");
        checkMarqueeElement(element, "div", false);

        // Test that the div element is NOT replaced if the device does
        // support the marquee css, regardless of whether it supports the
        // marquee element.
        element = doMarqueeTest(true, true, properties, "div");
        checkMarqueeElement(element, "div", false);
    }

    public void testRenderMarqueeForNotDivWithStyleSet() throws Exception {

        MutablePropertyValues properties = createPropertyValues();
        properties.setComputedAndSpecifiedValue(
                StylePropertyDetails.MCS_MARQUEE_STYLE,
                MCSMarqueeStyleKeywords.SCROLL);

        // Test that the div element is replaced if the device doesn't support
        // the marquee css and does support the marquee element.
        Element element = doMarqueeTest(true, false, properties, "p");
        assertEquals("p", element.getName());
        Element marquee = element.getParent();
        assertNotNull(marquee);
        assertEquals("marquee", marquee.getName());
        assertEquals("scroll", marquee.getAttributeValue("behavior"));
        assertNull(marquee.getAttributeValue("direction"));
        assertNull(marquee.getAttributeValue("loop"));
        assertNull(marquee.getAttributeValue("bgcolor"));

        assertNotNull(marquee.getParent());
        assertEquals("html", marquee.getParent().getName());

        // Test that the div element is NOT replaced if the device doesn't
        // support the marquee css, but also doesn't support the marquee element.
        element = doMarqueeTest(false, false, properties, "p");
        assertEquals("p", element.getName());
        Element parent = element.getParent();
        assertNotNull(parent);
        checkMarqueeElement(parent, "html", false);

        // Test that the div element is NOT replaced if the device does
        // support the marquee css, regardless of whether it supports the
        // marquee element.
        element = doMarqueeTest(false, true, properties, "p");
        assertEquals("p", element.getName());
        parent = element.getParent();
        assertNotNull(parent);
        checkMarqueeElement(parent, "html", false);

        // Test that the div element is NOT replaced if the device does
        // support the marquee css, regardless of whether it supports the
        // marquee element.
        element = doMarqueeTest(true, true, properties, "p");
        assertEquals("p", element.getName());
        parent = element.getParent();
        assertNotNull(parent);
        checkMarqueeElement(parent, "html", false);
    }

    public void testRenderMarqueeForNotDivWithAllSet() throws Exception {

        MutablePropertyValues properties = createPropertyValues();
        properties.setComputedAndSpecifiedValue(
                StylePropertyDetails.MCS_MARQUEE_STYLE,
                MCSMarqueeStyleKeywords.SCROLL);
        properties.setComputedAndSpecifiedValue(
                StylePropertyDetails.MCS_MARQUEE_DIRECTION,
                MCSMarqueeDirectionKeywords.LEFT);
        properties.setComputedAndSpecifiedValue(
                StylePropertyDetails.MCS_MARQUEE_REPETITION,
                MCSMarqueeRepetitionKeywords.INFINITE);
        properties.setComputedAndSpecifiedValue(
                StylePropertyDetails.BACKGROUND_COLOR,
                StyleColorNames.ORANGE);

        // Test that the marquee element is inserted if the device doesn't
        // support the marquee css and does support the marquee element.
        Element element = doMarqueeTest(true, false, properties, "p");
        assertEquals("p", element.getName());
        Element marquee = element.getParent();
        assertNotNull(marquee);
        checkMarqueeElement(marquee, "marquee", true);
        assertNotNull(marquee.getParent());
        assertEquals("html", marquee.getParent().getName());

        // Test that the div element is NOT inserted if the device doesn't
        // support the marquee css, but also doesn't support the marquee element.
        element = doMarqueeTest(false, false, properties, "p");
        assertEquals("p", element.getName());
        Element parent = element.getParent();
        assertNotNull(parent);
        checkMarqueeElement(parent, "html", false);


        // Test that the div element is NOT inserted if the device does
        // support the marquee css, regardless of whether it supports the
        // marquee element.
        element = doMarqueeTest(false, true, properties, "p");
        assertEquals("p", element.getName());
        parent = element.getParent();
        assertNotNull(parent);
        checkMarqueeElement(parent, "html", false);

        // Test that the div element is NOT inserted if the device does
        // support the marquee css, regardless of whether it supports the
        // marquee element.
        element = doMarqueeTest(true, true, properties, "p");
        assertEquals("p", element.getName());
        parent = element.getParent();
        assertNotNull(parent);
        checkMarqueeElement(parent, "html", false);
    }

    /**
     * Verify that any non-div element without mcs-marquee-style set is never
     * emulated using the marquee element, regardless of the value of the other
     * marquee style properties.
     *
     * @throws Exception if there was a problem running the test
     */
    public void testRenderMarqueeForNotDivWithStyleNotSet() throws Exception {

        MutablePropertyValues properties = createPropertyValues();
        properties.setComputedAndSpecifiedValue(
                StylePropertyDetails.MCS_MARQUEE_DIRECTION,
                MCSMarqueeDirectionKeywords.LEFT);
        properties.setComputedAndSpecifiedValue(
                StylePropertyDetails.MCS_MARQUEE_REPETITION,
                MCSMarqueeRepetitionKeywords.INFINITE);
        properties.setComputedAndSpecifiedValue(
                StylePropertyDetails.BACKGROUND_COLOR,
                StyleColorNames.ORANGE);

        // Test that a marquee element is NOT inserted if the device doesn't
        // support the marquee css and does support the marquee element.
        Element element = doMarqueeTest(true, false, properties, "p");
        checkMarqueeElement(element, "p", false);

        // Test that the marquee element is NOT inserted if the device doesn't
        // support the marquee css, but also doesn't support the marquee element.
        element = doMarqueeTest(false, false, properties, "p");
        checkMarqueeElement(element, "p", false);

        // Test that the marquee element is NOT inserted if the device does
        // support the marquee css, regardless of whether it supports the
        // marquee element.
        element = doMarqueeTest(false, true, properties, "p");
        checkMarqueeElement(element, "p", false);

        // Test that the marquee element is NOT inserted if the device does
        // support the marquee css, regardless of whether it supports the
        // marquee element.
        element = doMarqueeTest(true, true, properties, "p");
        checkMarqueeElement(element, "p", false);
    }

    /**
     * Verify that the configuration specifies that, by default, there are no
     * policies which require explicit handling of the policy value "default".
     */
    public void testDefaultPolicyValueConfiguration() {
        // Initialise the protocol config, ensuring that the device does not
        // explicitly specify any device element capabilities.
        setExpectations(false, false, false, false, false);
        device.fuzzy.getBooleanPolicyValue(mockFactory.expectsAny())
                .returns(false).any();

        device.fuzzy.getPolicyValue(mockFactory.expectsAny())
                .returns(null).any();

        config.initialize(device, builder);

        // Get the default device element capabilities.
        Set expectedDefaults = config.defaultElementCapabilities;
        DeviceCapabilityManager manager =
                config.getDeviceCapabilityManager();

        final String[] supportedElements = new String[] {"td", "blink", "div",
                                                         "marquee", "hr",
                                                         "strike", "u"};
        int numSupportedElements = supportedElements.length;

        // Check that unspecified values are unspecified.
        int actualNumber = 0;

        for (int i = 0; i < numSupportedElements; i++) {
            String elementName = supportedElements[i];
            DeviceElementCapability dec =
                    manager.getDeviceElementCapability(elementName, true);
            if (!expectedDefaults.contains(dec)) {
                assertEquals(CapabilitySupportLevel.NONE,
                        dec.getElementSupportLevel());
            } else {
                actualNumber++;
            }
        }
        assertEquals(expectedDefaults.size(), actualNumber);
    }

    /**
     * Tests, whether x-client-frame.rate.max is read properly
     * from device policy value.
     */
    public void testMaxClientFrameRate() {
        setExpectations(false, false, false, false, false);
        device.fuzzy.getBooleanPolicyValue(mockFactory.expectsAny())
                .returns(false).any();

        device.fuzzy.getPolicyValue(mockFactory.expectsAny())
                .returns(null).any();

        config.initialize(device, builder);
        
        assertEquals(10.0, config.getMaxClientFrameRate(), 0.1);
    }
    
    /**
     * Create the appropriate protocol configuration with which to run
     * the test.
     *
     * @return ProtocolConfigurationImpl on which to run the test
     */
    protected ProtocolConfigurationImpl createProtocolConfiguration() {
        return new ProtocolConfigurationImpl();
    }

    /**
     * Create an {@link com.volantis.mcs.devices.InternalDevice} mock and
     * {@link CSSVersion) mock and configure them as specified.
     *
     * @param supportsMarquee   true if the device supports the marquee element
     * @param supportsBlink     true if the device supports the blink element
     * @param supportsCss       true if the device supports CSS
     */
    protected void setExpectations(boolean supportsMarquee,
                                   boolean supportsBlink,
                                   boolean supportsUnderline,
                                   boolean supportsStrike,
                                   boolean supportsCSS) {

        final String marqueeSupport = supportsMarquee? "full": "none";
        final String blinkSupport = supportsBlink? "full": "none";
        final String underlineSupport = supportsUnderline? "full": "none";
        final String strikeSupport = supportsStrike? "full": "none";
        device = new InternalDeviceMock("device", expectations);
        builder = new DeviceCapabilityManagerBuilder(device);

        // from DeviceCapabilityManagerBuilder#addHrElementCapabilities
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.HR_SUPPORTED).returns("true").any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.HR_BORDER_TOP_COLOR).returns("full").any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.HR_BORDER_TOP_STYLE).returns("full").any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.HR_BORDER_TOP_WIDTH).returns("full").any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.HR_BORDER_BOTTOM_COLOR).returns("full").any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.HR_BORDER_BOTTOM_STYLE).returns("full").any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.HR_BORDER_BOTTOM_WIDTH).returns("full").any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.HR_COLOR).returns("full").any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.HR_WIDTH).returns("full").any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.HR_HEIGHT).returns("full").any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.HR_MARGIN_TOP).returns("full").any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.HR_MARGIN_BOTTOM).returns("full").any();

        // from DeviceCapabilityManagerBuilder#addDivElementCapabilities
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.DIV_SUPPORTED).returns("true").any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.DIV_MARGIN_TOP).returns("full").any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.DIV_MARGIN_BOTTOM).returns("full").any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.DIV_BORDER_TOP_COLOR).returns("full").any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.DIV_BORDER_TOP_STYLE).returns("full").any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.DIV_BORDER_TOP_WIDTH).returns("full").any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.DIV_BORDER_BOTTOM_COLOR).returns("full").any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.DIV_BORDER_BOTTOM_STYLE).returns("full").any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.DIV_BORDER_BOTTOM_WIDTH).returns("full").any();

        // from DeviceCapabilityManagerBuilder#addTdElementCapabilities
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.TD_MARGIN_TOP).returns("full").any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.TD_MARGIN_BOTTOM).returns("full").any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.TD_MARGIN_LEFT).returns("full").any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.TD_MARGIN_RIGHT).returns("full").any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.TD_PADDING_TOP).returns("full").any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.TD_PADDING_BOTTOM).returns("full").any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.TD_PADDING_LEFT).returns("full").any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.TD_PADDING_RIGHT).returns("full").any();

        // from DeviceCapabilityManagerBuilder#addMarqueeElementCapabilities
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.MARQUEE_SUPPORTED).returns(
                        marqueeSupport).any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.MARQUEE_DIRECTION).returns(
                        marqueeSupport).any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.MARQUEE_LOOP).returns
                (marqueeSupport).any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.MARQUEE_BEHAVIOR).returns(
                        marqueeSupport).any();
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.MARQUEE_BGCOLOR).returns(
                        marqueeSupport).any();

        // from DeviceCapabilityManagerBuilder#addBlinkElementCapabilities
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.BLINK_SUPPORTED).returns(
                        blinkSupport).any();
        device.expects.getBooleanPolicyValue(
                DevicePolicyConstants.SUPPORTS_CSS).
                returns(supportsCSS).any();

        // from DeviceCapabilityManagerBuilder#addUnderlineElementCapabilities
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.U_SUPPORTED).returns(
                        underlineSupport).any();
        device.expects.getBooleanPolicyValue(
                DevicePolicyConstants.SUPPORTS_CSS).
                returns(supportsCSS).any();

        // from DeviceCapabilityManagerBuilder#addStrikeElementCapabilities
        device.expects.getPolicyValue(
                DeviceCapabilityConstants.STRIKE_SUPPORTED).returns(
                        strikeSupport).any();
        device.expects.getBooleanPolicyValue(
                DevicePolicyConstants.SUPPORTS_CSS).
                returns(supportsCSS).any();

        // from ProtocolConfigurationImpl#registerMarqueeEmulationRenderers
        device.expects.getBooleanPolicyValue(
                DevicePolicyConstants.WAP_MARQUEE_DISPLAY_SUPPORTED).
                returns(false).any();
        device.expects.getBooleanPolicyValue(
                DevicePolicyConstants.WAP_MARQUEE_STYLE_SUPPORTED).
                returns(false).any();
        device.expects.getBooleanPolicyValue(
                DevicePolicyConstants.WAP_MARQUEE_REPETITION_SUPPORTED).
                returns(false).any();
        device.expects.getBooleanPolicyValue(
                DevicePolicyConstants.WAP_MARQUEE_DIRECTION_SUPPORTED).
                returns(false).any();

        // from WMLRootConfigurationImpl#registerElementOnlyStyleEmulationPropertyRenderers
        device.expects.getPolicyValue(
                DevicePolicyConstants.WML_IMAGE_NOSAVE).returns("none").any();

        cssVersion = new CSSVersionMock("cssVersion", expectations);

        if (supportsBlink) {
            CSSPropertyMock blink = supportsCSS ?
                    new CSSPropertyMock("display", expectations): null;

            cssVersion.expects.getProperty(
                    StylePropertyDetails.MCS_TEXT_BLINK).returns(blink).any();
        }

        if (supportsUnderline) {
            CSSPropertyMock underline = supportsCSS ?
                    new CSSPropertyMock("display", expectations): null;

            cssVersion.expects.getProperty(
                    StylePropertyDetails.MCS_TEXT_UNDERLINE_COLOR).
                    returns(null).any();

            cssVersion.expects.getProperty(
                    StylePropertyDetails.MCS_TEXT_UNDERLINE_STYLE).
                    returns(underline).any();
        }

        if (supportsStrike) {
            CSSPropertyMock strike = supportsCSS ?
                    new CSSPropertyMock("display", expectations): null;

            cssVersion.expects.getProperty(
                    StylePropertyDetails.MCS_TEXT_LINE_THROUGH_COLOR).
                    returns(null).any();

            cssVersion.expects.getProperty(
                    StylePropertyDetails.MCS_TEXT_LINE_THROUGH_STYLE).
                    returns(strike).any();
        }

        if (supportsMarquee) {

            CSSPropertyMock display =
                    new CSSPropertyMock("display", expectations);
            cssVersion.expects.getProperty(
                    StylePropertyDetails.DISPLAY).
                    returns(display).any();
            boolean result = supportsCSS? true: false;
            display.expects.supportsKeyword(DisplayKeywords.MCS_MARQUEE).
                    returns(result).any();

            CSSPropertyMock property = supportsCSS?
                    new CSSPropertyMock("property", expectations): null;

            cssVersion.expects.getProperty(
                    StylePropertyDetails.MCS_MARQUEE_STYLE).
                    returns(property).any();
            cssVersion.expects.getProperty(
                    StylePropertyDetails.MCS_MARQUEE_REPETITION).
                    returns(property).any();
            cssVersion.expects.getProperty(
                    StylePropertyDetails.MCS_MARQUEE_DIRECTION).
                    returns(property).any();
        }
        
        device.expects.getPolicyValue(
                DevicePolicyConstants.CLIENT_FRAME_RATE_MAX).returns("10").any();
    }

    /**
     * Create a set of mutable property values.
     *
     * @return A newly created set of mutable property values.
     */
    protected MutablePropertyValues createPropertyValues() {
        StylePropertyDefinitions definitions =
                StylePropertyDetails.getDefinitions();
        return StylingFactory.getDefaultInstance()
                .createPropertyValues(definitions);
    }

    /**
     * Verify that the marquee style has been emulated as expected.
     *
     * @param element   the rendered element
     * @param name      name of the expected element (either marquee or div)
     * @param emulated  true if the marquee style should have been emulated,
     *                  false if it wasn't supported
     */
    protected void checkMarqueeElement(Element element, String name,
                                       boolean emulated) {
        assertEquals(name, element.getName());
        if (emulated) {
            assertEquals("scroll", element.getAttributeValue("behavior"));
            assertEquals("left", element.getAttributeValue("direction"));
            assertEquals("16", element.getAttributeValue("loop"));
            assertEquals("orange", element.getAttributeValue("bgcolor"));
        } else {
            assertNull(element.getAttributeValue("behavior"));
            assertNull(element.getAttributeValue("direction"));
            assertNull(element.getAttributeValue("loop"));
            assertNull(element.getAttributeValue("bgcolor"));
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 14-Sep-05	9472/1	ibush	VBM:2005090808 Add default styling for sub/sup elements

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
