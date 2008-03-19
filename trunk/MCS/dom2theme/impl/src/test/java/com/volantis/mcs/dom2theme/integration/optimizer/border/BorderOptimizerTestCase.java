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
package com.volantis.mcs.dom2theme.integration.optimizer.border;

import com.volantis.mcs.dom2theme.impl.optimizer.OptimizerHelper;
import com.volantis.mcs.dom2theme.impl.optimizer.PropertyStatus;
import com.volantis.mcs.dom2theme.impl.optimizer.TargetEntity;
import com.volantis.mcs.dom2theme.impl.optimizer.border.BorderOptimizer;
import com.volantis.mcs.dom2theme.integration.optimizer.OptimizerTestCaseAbstract;
import com.volantis.mcs.themes.ExtensionPriority;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.PropertyGroups;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleShorthands;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.BorderStyleKeywords;
import com.volantis.mcs.themes.properties.BorderWidthKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.styling.device.DeviceStylingTestHelper;
import com.volantis.styling.device.DeviceValues;
import com.volantis.styling.device.DeviceValuesMock;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;


public class BorderOptimizerTestCase extends OptimizerTestCaseAbstract {

    /**
     * Test that ANY required values are rendered properly.
     */
    public void testRequiredAnyValues() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        supportsNonBorderEdgeShorthands();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        BorderOptimizer optimizer = new BorderOptimizer(checkerMock,
                shorthandSetMock);

        setBoxProperties(inputValues,
                StylePropertyDetails.BORDER_BOTTOM_COLOR,
                OptimizerHelper.ANY, PropertyStatus.REQUIRED,
                StylePropertyDetails.BORDER_LEFT_COLOR,
                OptimizerHelper.ANY, PropertyStatus.REQUIRED,
                StylePropertyDetails.BORDER_RIGHT_COLOR,
                OptimizerHelper.ANY, PropertyStatus.REQUIRED,
                StylePropertyDetails.BORDER_TOP_COLOR,
                StyleColorNames.BLACK,
                PropertyStatus.REQUIRED);
        setBoxProperties(inputValues,
                StylePropertyDetails.BORDER_BOTTOM_STYLE,
                        OptimizerHelper.ANY, PropertyStatus.REQUIRED,
                StylePropertyDetails.BORDER_LEFT_STYLE,
                        OptimizerHelper.ANY, PropertyStatus.REQUIRED,
                StylePropertyDetails.BORDER_RIGHT_STYLE,
                        OptimizerHelper.ANY, PropertyStatus.REQUIRED,
                StylePropertyDetails.BORDER_TOP_STYLE,
                        BorderStyleKeywords.SOLID, PropertyStatus.REQUIRED);
        setBoxProperties(inputValues,
                StylePropertyDetails.BORDER_BOTTOM_WIDTH,
                StyleValueFactory.getDefaultInstance().getLength(
                    null, 0.0, LengthUnit.PX),
                PropertyStatus.REQUIRED,
                StylePropertyDetails.BORDER_LEFT_WIDTH,
                StyleValueFactory.getDefaultInstance().getLength(
                    null, 0.0, LengthUnit.PX),
                PropertyStatus.REQUIRED,
                StylePropertyDetails.BORDER_RIGHT_WIDTH,
                StyleValueFactory.getDefaultInstance().getLength(
                    null, 0.0, LengthUnit.PX),
                PropertyStatus.REQUIRED,
                StylePropertyDetails.BORDER_TOP_WIDTH,
                StyleValueFactory.getDefaultInstance().getLength(
                    null, 1.0, LengthUnit.PX),
                PropertyStatus.REQUIRED);

        MutableStyleProperties outputValues =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();

        optimizer.optimize(TargetEntity.ELEMENT, inputValues, outputValues,
                deviceValuesMock);

        assertEquals("border-color:black;border-style:solid;border-width:1px 0 0",
                outputValues.getStandardCSS());
    }

    private void supportsNonBorderEdgeShorthands() {
        shorthandSetMock.expects.contains(StyleShorthands.BORDER).
                returns(true).any();
        shorthandSetMock.expects.contains(StyleShorthands.BORDER_COLOR)
                .returns(true).any();
        shorthandSetMock.expects.contains(StyleShorthands.BORDER_STYLE)
                .returns(true).any();
        shorthandSetMock.expects.contains(StyleShorthands.BORDER_WIDTH)
                .returns(true).any();
        shorthandSetMock.expects.contains(StyleShorthands.BORDER_BOTTOM)
                .returns(false).any();
        shorthandSetMock.expects.contains(StyleShorthands.BORDER_LEFT)
                .returns(false).any();
        shorthandSetMock.expects.contains(StyleShorthands.BORDER_RIGHT)
                .returns(false).any();
        shorthandSetMock.expects.contains(StyleShorthands.BORDER_TOP)
                .returns(false).any();
    }

    private void supportsAllBorderShorthands() {
        shorthandSetMock.expects.contains(StyleShorthands.BORDER).
                returns(true).any();
        shorthandSetMock.expects.contains(StyleShorthands.BORDER_COLOR)
                .returns(true).any();
        shorthandSetMock.expects.contains(StyleShorthands.BORDER_STYLE)
                .returns(true).any();
        shorthandSetMock.expects.contains(StyleShorthands.BORDER_WIDTH)
                .returns(true).any();
        shorthandSetMock.expects.contains(StyleShorthands.BORDER_BOTTOM)
                .returns(true).any();
        shorthandSetMock.expects.contains(StyleShorthands.BORDER_LEFT)
                .returns(true).any();
        shorthandSetMock.expects.contains(StyleShorthands.BORDER_RIGHT)
                .returns(true).any();
        shorthandSetMock.expects.contains(StyleShorthands.BORDER_TOP)
                .returns(true).any();
    }

    private void setBoxProperties(MutablePropertyValues inputValues,
            final StyleProperty bottomProperty, final StyleValue bottomValue,
            final PropertyStatus bottomStatus,
            final StyleProperty leftProperty, final StyleValue leftValue,
            final PropertyStatus leftStatus,
            final StyleProperty rightProperty, final StyleValue rightValue,
            final PropertyStatus rightStatus,
            final StyleProperty topProperty, final StyleValue topValue,
            final PropertyStatus topStatus) {


        setPropertyValue(inputValues, bottomProperty, bottomValue, bottomStatus);
        setPropertyValue(inputValues, leftProperty, leftValue, leftStatus);
        setPropertyValue(inputValues, rightProperty, rightValue, rightStatus);
        setPropertyValue(inputValues, topProperty, topValue, topStatus);
    }

    private void setBoxProperties(
            MutablePropertyValues inputValues,
            StyleProperty[] boxProperties,
            StyleValue value,
            PropertyStatus status) {

        for (int i = 0; i < boxProperties.length; i++) {
            StyleProperty property = boxProperties[i];
            setPropertyValue(inputValues, property, value, status);
        }
    }

    /**
     * Ensure that a single property treated as medium priority by the device
     * makes the whole border shorthand important.
     */
    public void testSingleMediumMakesBorderImportant() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final DeviceValuesMock deviceValuesMock =
                DeviceStylingTestHelper.createDeviceValuesMock(mockFactory,
                        expectations, DeviceValues.DEFAULT,
                        ExtensionPriority.MEDIUM,
                        StylePropertyDetails.BORDER_TOP_COLOR);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        supportsAllBorderShorthands();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        BorderOptimizer optimizer = new BorderOptimizer(checkerMock,
                shorthandSetMock);

        setBoxProperties(inputValues, PropertyGroups.BORDER_COLOR_PROPERTIES,
                StyleColorNames.BLACK, PropertyStatus.REQUIRED);
        setBoxProperties(inputValues, PropertyGroups.BORDER_STYLE_PROPERTIES,
                BorderStyleKeywords.SOLID, PropertyStatus.REQUIRED);
        setBoxProperties(inputValues, PropertyGroups.BORDER_WIDTH_PROPERTIES,
                BorderWidthKeywords.THICK, PropertyStatus.REQUIRED);

        MutableStyleProperties outputValues =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();

        optimizer.optimize(TargetEntity.ELEMENT, inputValues, outputValues,
                deviceValuesMock);

        assertEquals("border:black solid thick !important",
                outputValues.getStandardCSS());
    }

    /**
     * Ensure that a single property treated as medium priority by the device
     * makes the whole border-color shorthand important.
     */
    public void testSingleMediumMakesBorderColorImportant() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final DeviceValuesMock deviceValuesMock =
                DeviceStylingTestHelper.createDeviceValuesMock(mockFactory,
                        expectations, DeviceValues.DEFAULT,
                        ExtensionPriority.MEDIUM,
                        StylePropertyDetails.BORDER_TOP_COLOR);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        supportsAllBorderShorthands();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        BorderOptimizer optimizer = new BorderOptimizer(checkerMock,
                shorthandSetMock);

        setBoxProperties(inputValues, PropertyGroups.BORDER_COLOR_PROPERTIES,
                StyleColorNames.BLACK, PropertyStatus.REQUIRED);

        MutableStyleProperties outputValues =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();

        optimizer.optimize(TargetEntity.ELEMENT, inputValues, outputValues,
                deviceValuesMock);

        assertEquals("border-color:black !important",
                outputValues.getStandardCSS());
    }

    /**
     * Ensure that a single property treated as medium priority by the device
     * makes the whole border-top shorthand important.
     */
    public void testSingleMediumMakesBorderTopImportant() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final DeviceValuesMock deviceValuesMock =
                DeviceStylingTestHelper.createDeviceValuesMock(mockFactory,
                        expectations, DeviceValues.DEFAULT,
                        ExtensionPriority.MEDIUM,
                        StylePropertyDetails.BORDER_TOP_COLOR);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        supportsAllBorderShorthands();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        BorderOptimizer optimizer = new BorderOptimizer(checkerMock,
                shorthandSetMock);

        setPropertyValue(inputValues, StylePropertyDetails.BORDER_TOP_COLOR,
                StyleColorNames.BLACK, PropertyStatus.REQUIRED);
        setPropertyValue(inputValues, StylePropertyDetails.BORDER_TOP_STYLE,
                BorderStyleKeywords.SOLID, PropertyStatus.REQUIRED);
        setPropertyValue(inputValues, StylePropertyDetails.BORDER_TOP_WIDTH,
                BorderWidthKeywords.THICK, PropertyStatus.REQUIRED);

        MutableStyleProperties outputValues =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();

        optimizer.optimize(TargetEntity.ELEMENT, inputValues, outputValues,
                deviceValuesMock);

        assertEquals("border-top:black solid thick !important",
                outputValues.getStandardCSS());
    }

    /**
     * Ensure that if the border-top-color matches color that it does
     * not become part of the shorthand.
     */
    public void testBorderColorMatchingColorIsCleared() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        supportsAllBorderShorthands();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        BorderOptimizer optimizer = new BorderOptimizer(checkerMock,
                shorthandSetMock);

        setPropertyValue(inputValues, StylePropertyDetails.BORDER_TOP_COLOR,
                StyleColorNames.BLACK, PropertyStatus.CLEARABLE);
        setPropertyValue(inputValues, StylePropertyDetails.BORDER_TOP_STYLE,
                BorderStyleKeywords.SOLID, PropertyStatus.REQUIRED);
        setPropertyValue(inputValues, StylePropertyDetails.BORDER_TOP_WIDTH,
                BorderWidthKeywords.THICK, PropertyStatus.REQUIRED);

        MutableStyleProperties outputValues =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();

        optimizer.optimize(TargetEntity.ELEMENT, inputValues, outputValues,
                deviceValuesMock);

        assertEquals("border-top:solid thick",
                outputValues.getStandardCSS());
    }
}
