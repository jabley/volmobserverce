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

package com.volantis.mcs.dom2theme.integration.optimizer;

import com.volantis.mcs.dom2theme.impl.optimizer.BasicShorthandAnalyzer;
import com.volantis.mcs.dom2theme.impl.optimizer.OptimizerHelper;
import com.volantis.mcs.dom2theme.impl.optimizer.PropertyStatus;
import com.volantis.mcs.dom2theme.impl.optimizer.ShorthandAnalyzer;
import com.volantis.mcs.dom2theme.impl.optimizer.TargetEntity;
import com.volantis.mcs.themes.ExtensionPriority;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleShorthands;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.BorderStyleKeywords;
import com.volantis.mcs.themes.properties.BorderWidthKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.styling.device.DeviceValues;
import com.volantis.styling.device.DeviceValuesMock;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.styling.values.PropertyValues;

/**
 * Test cases for {@link BasicShorthandAnalyzer}.
 */
public class ShorthandAnalyserTestCase
        extends OptimizerTestCaseAbstract {

    /**
     * Test an unsupported shorthand.
     *
     * <p>The shorthand is unsupported so even though all the properties are
     * set to their initial values the shorthand cannot be used.</p>
     *
     */
    public void testUnsupportedShorthand() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        shorthandSetMock.expects.contains(StyleShorthands.BORDER_WIDTH)
                .returns(false).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        PropertyValues inputValues = createEdgeProperties(
                BorderWidthKeywords.MEDIUM, BorderWidthKeywords.MEDIUM,
                BorderWidthKeywords.MEDIUM, BorderWidthKeywords.MEDIUM,
                PropertyStatus.CLEARABLE, PropertyStatus.CLEARABLE,
                PropertyStatus.CLEARABLE, PropertyStatus.CLEARABLE);

        BasicShorthandAnalyzer analyzer = new BasicShorthandAnalyzer(
                StyleShorthands.BORDER_WIDTH, checkerMock, shorthandSetMock);
        analyzer.analyze(TargetEntity.ELEMENT, inputValues, deviceValuesMock);

        assertFalse("Shouldn't be able to use shorthand",
                analyzer.canUseShorthand());
        assertTrue("Should be able to clear all", analyzer.allClearable());
        assertEquals("Should all be initial", 4, analyzer.getInitialCount());
        assertEquals("Should be normal priority",
                Priority.NORMAL, analyzer.getShorthandPriority());
    }

    /**
     * Test that ANY values that are required prevent the properties from
     * being cleared.
     */
    public void testAnyRequired() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        shorthandSetMock.expects.contains(StyleShorthands.BORDER_WIDTH)
                .returns(true).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        PropertyValues inputValues = createEdgeProperties(
                OptimizerHelper.ANY, OptimizerHelper.ANY,
                OptimizerHelper.ANY, OptimizerHelper.ANY,
                PropertyStatus.REQUIRED, PropertyStatus.REQUIRED,
                PropertyStatus.REQUIRED, PropertyStatus.REQUIRED);

        BasicShorthandAnalyzer analyzer = new BasicShorthandAnalyzer(
                StyleShorthands.BORDER_WIDTH, checkerMock, shorthandSetMock);
        analyzer.analyze(TargetEntity.ELEMENT, inputValues, deviceValuesMock);

        assertTrue("Should be able to use shorthand",
                analyzer.canUseShorthand());
        assertFalse("Should not be able to clear all", analyzer.allClearable());
        assertEquals("None should be initial", 0, analyzer.getInitialCount());
        assertEquals("Should be normal priority",
                Priority.NORMAL, analyzer.getShorthandPriority());
    }

    /**
     * Test override of border colors when set to ANY
     */
    public void testBorderColourOverrides() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        shorthandSetMock.expects.contains(StyleShorthands.BORDER_LEFT)
                .returns(true).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        PropertyValues inputValues = createBorderEdge(
                OptimizerHelper.ANY,
                StyleKeywords.NONE,
                StyleValueFactory.getDefaultInstance().getLength(
                    null, 0,LengthUnit.PX),
                PropertyStatus.REQUIRED,
                PropertyStatus.REQUIRED,
                PropertyStatus.REQUIRED);

        MutableStyleProperties outputValues =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();

        ShorthandAnalyzer analyzer = new BasicShorthandAnalyzer(
                StyleShorthands.BORDER_LEFT, checkerMock, shorthandSetMock);
        analyzer.analyze(TargetEntity.ELEMENT, inputValues, deviceValuesMock);
        analyzer.updateShorthand(outputValues);

        assertTrue("Should be able to use shorthand",
                analyzer.canUseShorthand());

        // red should be the color overide
        // none was specified
        // initial value was got for width as style is none
        assertEquals("Shorthand should match:",
                "border-left:red none 0",
                outputValues.getShorthandValue(
                StyleShorthands.BORDER_LEFT).toString());
    }
    
    private PropertyValues createEdgeProperties(
            final StyleValue bottomValue, final StyleValue leftValue,
            final StyleValue rightValue, final StyleValue topValue,
            final PropertyStatus bottomStatus, final PropertyStatus leftStatus,
            final PropertyStatus rightStatus, final PropertyStatus topStatus) {

        MutablePropertyValues inputValues =
                STYLING_FACTORY.createPropertyValues();

        setPropertyValue(inputValues, StylePropertyDetails.BORDER_BOTTOM_WIDTH,
                bottomValue, bottomStatus);

        setPropertyValue(inputValues, StylePropertyDetails.BORDER_LEFT_WIDTH,
                leftValue, leftStatus);

        setPropertyValue(inputValues, StylePropertyDetails.BORDER_RIGHT_WIDTH,
                rightValue, rightStatus);

        setPropertyValue(inputValues, StylePropertyDetails.BORDER_TOP_WIDTH,
                topValue, topStatus);

        return inputValues;
    }

    private PropertyValues createBorderEdge(
            final StyleValue colorValue,
            final StyleValue styleValue,
            final StyleValue widthValue,
            final PropertyStatus colorStatus,
            final PropertyStatus styleStatus,
            final PropertyStatus widthStatus) {

        MutablePropertyValues inputValues =
                STYLING_FACTORY.createPropertyValues();

        setPropertyValue(inputValues, StylePropertyDetails.BORDER_LEFT_COLOR,
                colorValue, colorStatus);
        setPropertyValue(inputValues, StylePropertyDetails.BORDER_LEFT_STYLE,
                styleValue, styleStatus);
        setPropertyValue(inputValues, StylePropertyDetails.BORDER_LEFT_WIDTH,
                widthValue, widthStatus);

        return inputValues;
    }

    /**
     * Ensure that if the target device has a priority of NORMAL for a value
     * that it forces the shared priority to be important.
     */
    public void testForceImportant() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final DeviceValuesMock deviceValuesMock =
                new DeviceValuesMock("deviceValuesMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        shorthandSetMock.expects.contains(StyleShorthands.BORDER_LEFT)
                .returns(true).any();

        deviceValuesMock.fuzzy.getStyleValue(mockFactory.expectsAny())
                .returns(DeviceValues.DEFAULT).any();

        deviceValuesMock.expects
                .getPriority(StylePropertyDetails.BORDER_LEFT_COLOR)
                .returns(Priority.NORMAL).any();

        deviceValuesMock.expects
                .getPriority(StylePropertyDetails.BORDER_LEFT_STYLE)
                .returns(Priority.NORMAL).any();

        deviceValuesMock.expects
                .getPriority(StylePropertyDetails.BORDER_LEFT_WIDTH)
                .returns(ExtensionPriority.MEDIUM).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        PropertyValues inputValues = createBorderEdge(
                StyleColorNames.RED,
                BorderStyleKeywords.DASHED,
                BorderWidthKeywords.THICK,
                PropertyStatus.REQUIRED,
                PropertyStatus.REQUIRED,
                PropertyStatus.REQUIRED);

        MutableStyleProperties outputValues =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();

        ShorthandAnalyzer analyzer = new BasicShorthandAnalyzer(
                StyleShorthands.BORDER_LEFT, checkerMock, shorthandSetMock);
        analyzer.analyze(TargetEntity.ELEMENT, inputValues, deviceValuesMock);

        assertTrue("Should be able to use shorthand",
                analyzer.canUseShorthand());

        analyzer.updateShorthand(outputValues);

        assertEquals("Shorthand should match:",
                "border-left:red dashed thick !important",
                outputValues.getShorthandValue(
                StyleShorthands.BORDER_LEFT).getStandardCSS());
    }
}
