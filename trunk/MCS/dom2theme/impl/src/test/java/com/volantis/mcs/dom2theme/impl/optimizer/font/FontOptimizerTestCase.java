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

package com.volantis.mcs.dom2theme.impl.optimizer.font;

import com.volantis.mcs.dom2theme.impl.optimizer.PropertyClearerCheckerImpl;
import com.volantis.mcs.dom2theme.impl.optimizer.ShorthandOptimizer;
import com.volantis.mcs.dom2theme.impl.optimizer.TargetEntity;
import com.volantis.mcs.dom2theme.integration.optimizer.OptimizerTestHelper;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.ShorthandSetMock;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleShorthands;
import com.volantis.mcs.themes.StyleValues;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.styling.device.DeviceStylingTestHelper;
import com.volantis.styling.device.DeviceValues;
import com.volantis.styling.device.DeviceValuesMock;
import com.volantis.styling.properties.PropertyDetailsSet;
import com.volantis.styling.values.PropertyValues;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link FontOptimizer}.
 */
public class FontOptimizerTestCase
        extends TestCaseAbstract {
    private OptimizerTestHelper helper;
    private PropertyDetailsSet standardDetailsSet;
    private PropertyClearerCheckerImpl checker;
    private ShorthandSetMock shorthandsMock;
    private StyleValues rootStyleProperties;
    private MutableStyleProperties outputValues;
    private DeviceValuesMock deviceValuesMock;

    protected void setUp() throws Exception {
        super.setUp();

        helper = new OptimizerTestHelper();
        standardDetailsSet = StylePropertyDetails.getDefinitions()
                .getStandardDetailsSet();

        checker = new PropertyClearerCheckerImpl(standardDetailsSet);

        shorthandsMock = new ShorthandSetMock("shorthandsMock", expectations);

        rootStyleProperties = standardDetailsSet.getRootStyleValues();

        deviceValuesMock = DeviceStylingTestHelper.createDeviceValuesMock(
                mockFactory, expectations, DeviceValues.NOT_SET);
    }

    /**
     * Ensure that when the system font is specified that the properties are
     * preserved, even if they match the initial, or inherited values, or are
     * unknown.
     */
    public void testSystemFontPreserves() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        PropertyValues inputValues = helper.parseDeclarations(
                "font: caption; font-size: medium");

        shorthandsMock.expects.contains(StyleShorthands.FONT)
                .returns(true).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================


        MutableStyleProperties outputValues =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();

        ShorthandOptimizer optimizer = new FontOptimizer(checker,
                shorthandsMock);

        checker.prepare(rootStyleProperties, TargetEntity.ELEMENT);

        optimizer.optimize(TargetEntity.ELEMENT, inputValues, outputValues,
                deviceValuesMock);

        assertEquals("font:caption;font-size:medium",
                outputValues.getStandardCSS());
    }

    /**
     * Ensure that when the system font is not specified and all the other
     * properties are that the shorthand can be used.
     */
    public void testUseShorthand() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        PropertyValues inputValues = helper.parseDeclarations(
                "font: medium/90% Foo");

        shorthandsMock.expects.contains(StyleShorthands.FONT)
                .returns(true).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================


        MutableStyleProperties outputValues =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();

        ShorthandOptimizer optimizer = new FontOptimizer(checker,
                shorthandsMock);

        checker.prepare(rootStyleProperties, TargetEntity.ELEMENT);

        optimizer.optimize(TargetEntity.ELEMENT, inputValues, outputValues,
                deviceValuesMock);

        assertEquals("font:medium/90% Foo", outputValues.getStandardCSS());
    }

    /**
     * Ensure that when font size is required for use by individual properties
     * but not for shorthand that the individual property is used because it is
     * shorter.
     */
    public void testUseFontSize() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        StyleValues parentValues = helper.parseDeclarations(
                "font: large FooBar");

        PropertyValues inputValues = helper.parseDeclarations(
                "font: medium FooBar");

        shorthandsMock.expects.contains(StyleShorthands.FONT)
                .returns(true).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================


        outputValues =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();

        ShorthandOptimizer optimizer = new FontOptimizer(checker,
                shorthandsMock);

        // When only the font-family property is needed for individual
        // properties then use that instead of the font shorthand.
        checker.prepare(parentValues, TargetEntity.ELEMENT);

        optimizer.optimize(TargetEntity.ELEMENT, inputValues, outputValues,
                deviceValuesMock);

        assertEquals("font-size:medium", outputValues.getStandardCSS());
    }

    /**
     * Ensure that all the properties are set when the target is for a pseudo
     * class.
     */
    public void testForPseudoClass() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        StyleValues parentValues = helper.parseDeclarations(
                "font: large FooBar");

        PropertyValues inputValues = helper.parseDeclarations(
                "font: medium FooBar");

        shorthandsMock.expects.contains(StyleShorthands.FONT)
                .returns(true).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================


        outputValues =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();

        ShorthandOptimizer optimizer = new FontOptimizer(checker,
                shorthandsMock);

        // A pseudo class must set all the properties that are set so in this
        // case it should use the shorthand.
        checker.prepare(parentValues, TargetEntity.PSEUDO_CLASS);

        optimizer.optimize(TargetEntity.PSEUDO_CLASS, inputValues, outputValues,
                deviceValuesMock);

        assertEquals("font:medium FooBar", outputValues.getStandardCSS());
    }
}
