/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.dom2theme.impl.emulator;

import com.volantis.mcs.dom.ElementMock;
import com.volantis.mcs.dom2theme.impl.model.OutputStylesMock;
import com.volantis.mcs.dom2theme.impl.model.PseudoStylePath;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.styling.StatefulPseudoClassImpl;
import com.volantis.styling.StylesMock;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.testtools.mock.test.MockTestCaseAbstract;

/**
 * Verify that {@link HoverEmulator} behaves as expected
 */
public class HoverEmulatorTestCase extends MockTestCaseAbstract {

    HoverEmulator emulator;
    ElementMock element;
    OutputStylesMock outputStyles;
    OutputStylesMock styles;
    MutableStyleProperties hoverProperties;
    MutablePropertyValues nonNestedProperties;


    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();
        // Create test objects.
        emulator = new HoverEmulator();
        element = new ElementMock("element", expectations);
        outputStyles = new OutputStylesMock("outputStyles", expectations);
        styles = new OutputStylesMock("styles", expectations);
        hoverProperties = ThemeFactory.getDefaultInstance().
               createMutableStyleProperties();
        // need to create a fully populated set of property values.
        nonNestedProperties = StylingFactory.getDefaultInstance().
                createPropertyValues(StylePropertyDetails.getDefinitions());
    }

    /**
     * Verify that nothing is rendered if no :hover styles are specified on
     * this element.
     */
    public void testEmulateForNoHover() {
        // Set expectations.
        styles.expects.getPathProperties(
                PseudoStylePath.EMPTY_PATH.addPseudoClassSet(
                        StatefulPseudoClassImpl.HOVER.getSet())).
                returns(hoverProperties);

        // Run test.
        emulator.emulate(element, styles);
    }

    /**
     * Verify that emulated styles are rendered out if :hover styles are
     * specified on this element.
     */
    public void testEmulateForSimpleStyles() {

        // Configure test objects.
        hoverProperties.setStyleValue(StylePropertyDetails.COLOR,
                StyleColorNames.RED);
        hoverProperties.setStyleValue(StylePropertyDetails.FONT_STYLE,
                StyleKeywords.ITALIC);
        nonNestedProperties.setSpecifiedValue(
                StylePropertyDetails.COLOR, StyleColorNames.GREEN);
        nonNestedProperties.setSpecifiedValue(StylePropertyDetails.FONT_STYLE,
                StyleKeywords.NORMAL);
        nonNestedProperties.setSpecifiedValue(
                StylePropertyDetails.BACKGROUND_COLOR,
                StyleColorNames.FUCHSIA);

        // Set expectations.
        styles.fuzzy.getPathProperties(
                mockFactory.expectsInstanceOf(PseudoStylePath.class)).
                returns(hoverProperties);

        element.expects.getAttributeValue(HoverEmulator.ON_MOUSE_OVER).
                returns(null);
        element.expects.getAttributeValue(HoverEmulator.ON_MOUSE_OUT).
                returns(null);
        element.expects.setAttribute(HoverEmulator.ON_MOUSE_OVER,
                "style.color='red';style.fontStyle='italic'");
        element.expects.setAttribute(HoverEmulator.ON_MOUSE_OUT,
                "style.color='';style.fontStyle=''");

        // Run test.
        emulator.emulate(element, styles);
    }

    /**
     * Verify that the styles are correctly emulated even if the element
     * already has onmouseXXX events.
     */
    public void testEmulateWhenElementAlreadyHasOnMouseXXXEvents() {
        // Configure test objects.
        hoverProperties.setStyleValue(StylePropertyDetails.COLOR,
                StyleColorNames.RED);
        hoverProperties.setStyleValue(StylePropertyDetails.FONT_STYLE,
                StyleKeywords.ITALIC);
        nonNestedProperties.setSpecifiedValue(
                StylePropertyDetails.COLOR, StyleColorNames.GREEN);
        nonNestedProperties.setSpecifiedValue(StylePropertyDetails.FONT_STYLE,
                StyleKeywords.NORMAL);
        nonNestedProperties.setSpecifiedValue(
                StylePropertyDetails.BACKGROUND_COLOR,
                StyleColorNames.FUCHSIA);

        // Set expectations.
        styles.fuzzy.getPathProperties(
                mockFactory.expectsInstanceOf(PseudoStylePath.class)).
                returns(hoverProperties);

        element.expects.getAttributeValue(HoverEmulator.ON_MOUSE_OVER).
                returns("style.fontWeight='bold'");
        element.expects.getAttributeValue(HoverEmulator.ON_MOUSE_OUT).
                returns("style.fontWeight='normal'");
        element.expects.setAttribute(HoverEmulator.ON_MOUSE_OVER,
                "style.fontWeight='bold';style.color='red';style.fontStyle='italic'");
        element.expects.setAttribute(HoverEmulator.ON_MOUSE_OUT,
                "style.fontWeight='normal';style.color='';style.fontStyle=''");

        // Run test.
        emulator.emulate(element, styles);
    }
}

