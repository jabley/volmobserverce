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

import com.volantis.mcs.dom2theme.impl.optimizer.EdgeShorthandOptimizer;
import com.volantis.mcs.dom2theme.impl.optimizer.ShorthandOptimizer;
import com.volantis.mcs.dom2theme.impl.optimizer.TargetEntity;
import com.volantis.mcs.themes.ExtensionPriority;
import com.volantis.mcs.themes.MutableShorthandSet;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.PropertyGroups;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleShorthands;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.values.ShorthandValue;
import com.volantis.styling.device.DeviceStylingTestHelper;
import com.volantis.styling.device.DeviceValues;
import com.volantis.styling.device.DeviceValuesMock;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.PropertyValues;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public class EdgeShorthandOptimizerTestCase
        extends TestCaseAbstract {

    private OptimizerTestHelper helper;
    private ShorthandOptimizer optimizer;
    private StyleValueFactory valuefactory;
    private DeviceValuesMock deviceValuesMock;

    protected void setUp() throws Exception {
        super.setUp();

        valuefactory = StyleValueFactory.getDefaultInstance();

        helper = new OptimizerTestHelper();

        MutableShorthandSet supportedShorthands = new MutableShorthandSet();
        supportedShorthands.add(StyleShorthands.MARGIN);
        optimizer = new EdgeShorthandOptimizer(StyleShorthands.MARGIN,
                new TestPropertyClearerChecker(),
                supportedShorthands);


        deviceValuesMock = DeviceStylingTestHelper.createDeviceValuesMock(
                mockFactory, expectations, DeviceValues.DEFAULT);
    }

    /**
     * Test that optimising values that are all equal works properly.
     */
    public void testOptimiseAllEqual() {


        StyleValue[] expected = new StyleValue[] {
            valuefactory.getLength(null, 10, LengthUnit.PX)
        };

        doCompositeTest("margin: 10px", expected, Priority.NORMAL,
                deviceValuesMock);
    }

    private void doCompositeTest(
            String css,
            StyleValue[] expectedValues,
            Priority expectedPriority,
            final DeviceValuesMock deviceValuesMock) {

        MutableStyleProperties outputValues =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();
        PropertyValues inputValues = helper.parseDeclarations(css);

        optimizer.optimize(TargetEntity.ELEMENT, inputValues, outputValues,
                deviceValuesMock);

        ShorthandValue shorthandValue =
                outputValues.getShorthandValue(StyleShorthands.MARGIN);

        assertNotNull("Shorthand value should be set", shorthandValue);

        Priority priority = shorthandValue.getPriority();
        assertEquals("Priority mismatch", expectedPriority, priority);

        assertEquals("Count mismatch", expectedValues.length,
                shorthandValue.getCount());

        for (int i = 0; i < expectedValues.length; i++) {
            StyleValue expectedValue = expectedValues[i];
            StyleValue value = shorthandValue.getValue(i);
            assertEquals("Value " + i + " mismatch", expectedValue, value);
        }

        // todo check that the other properties are cleared.
    }

    private void doIndividualTest(
            String css,
            PropertyValue[] expectedValues,
            final DeviceValuesMock deviceValuesMock) {

        MutableStyleProperties outputValues =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();
        PropertyValues inputValues = helper.parseDeclarations(css);
        
        optimizer.optimize(TargetEntity.ELEMENT, inputValues, outputValues,
                deviceValuesMock);

        for (int i = 0; i < expectedValues.length; i++) {
            PropertyValue expectedValue = expectedValues[i];
            StyleProperty property = expectedValue.getProperty();

            PropertyValue propertyValue = outputValues.getPropertyValue(
                    property);

            assertEquals("Value " + i + " mismatch", expectedValue,
                    propertyValue);
        }
    }

    /**
     * Test that optimising values that are all different works properly.
     */
    public void testOptimiseAllDifferent() {

        StyleValue[] expected = new StyleValue[] {
            valuefactory.getLength(null, 0, LengthUnit.PX),
            valuefactory.getLength(null, 1, LengthUnit.PX),
            valuefactory.getLength(null, 2, LengthUnit.PX),
            valuefactory.getLength(null, 3, LengthUnit.PX),
        };

        doCompositeTest("margin: 0px 1px 2px 3px", expected,
                Priority.NORMAL, deviceValuesMock);
    }

    /**
     * Test that optimising two values works properly.
     */
    public void testOptimiseTwoDifferent() {

        StyleValue[] expected = new StyleValue[] {
            valuefactory.getLength(null, 0, LengthUnit.PX),
            valuefactory.getLength(null, 1, LengthUnit.PX),
        };

        doCompositeTest("margin: 0px 1px", expected,
                Priority.NORMAL, deviceValuesMock);
    }

    /**
     * Test that optimising three values works properly.
     */
    public void testOptimiseThreeDifferent() {

        StyleValue[] expected = new StyleValue[] {
            valuefactory.getLength(null, 0, LengthUnit.PX),
            valuefactory.getLength(null, 1, LengthUnit.PX),
            valuefactory.getLength(null, 2, LengthUnit.PX),
        };

        doCompositeTest("margin: 0px 1px 2px", expected,
                Priority.NORMAL, deviceValuesMock);
    }

    /**
     * Test that optimising four important values works properly when one is
     * treated by the device as medium priority.
     */
    public void testOptimiseAllDifferentMediumPriority() {

        StyleValue[] expected = new StyleValue[] {
            valuefactory.getLength(null, 0, LengthUnit.PX),
            valuefactory.getLength(null, 1, LengthUnit.PX),
            valuefactory.getLength(null, 2, LengthUnit.PX),
            valuefactory.getLength(null, 3, LengthUnit.PX),
        };

        deviceValuesMock = DeviceStylingTestHelper.createDeviceValuesMock(
                mockFactory, expectations, DeviceValues.DEFAULT,
                ExtensionPriority.MEDIUM, StylePropertyDetails.MARGIN_TOP);

        doCompositeTest("margin: 0px 1px 2px 3px", expected,
                Priority.IMPORTANT, deviceValuesMock);
    }

    /**
     * Test that when one is not set then it cannot be optimised.
     */
    public void testOptimiseDoesNothingForUnset() {

        PropertyValue[] expected = new PropertyValue[] {
            ThemeFactory.getDefaultInstance().createPropertyValue(
                    PropertyGroups.MARGIN_PROPERTIES[0],
                    valuefactory.getLength(null, 1, LengthUnit.PX),
                    Priority.IMPORTANT),

            ThemeFactory.getDefaultInstance().createPropertyValue(
                    PropertyGroups.MARGIN_PROPERTIES[1],
                    valuefactory.getLength(null, 2, LengthUnit.PX)),

            ThemeFactory.getDefaultInstance().createPropertyValue(
                    PropertyGroups.MARGIN_PROPERTIES[2],
                    valuefactory.getLength(null, 3, LengthUnit.PX)),
        };

        deviceValuesMock = new DeviceValuesMock("deviceValuesMock",
                expectations);
        deviceValuesMock = DeviceStylingTestHelper.createDeviceValuesMock(
                mockFactory, expectations, DeviceValues.DEFAULT,
                ExtensionPriority.MEDIUM, StylePropertyDetails.MARGIN_TOP);

        doIndividualTest("margin-top: 1px; margin-right: 2px; " +
                "margin-bottom: 3px", expected, deviceValuesMock);
    }
}
