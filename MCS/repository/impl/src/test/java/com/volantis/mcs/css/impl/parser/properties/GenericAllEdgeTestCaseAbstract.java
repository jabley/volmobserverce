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

package com.volantis.mcs.css.impl.parser.properties;

import com.volantis.mcs.themes.PropertyGroups;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.styling.properties.StyleProperty;

/**
 * Base for all classes that test classes derived from
 * {@link GenericAllEdgeParser}.
 */
public abstract class GenericAllEdgeTestCaseAbstract
        extends ParsingPropertiesMockTestCaseAbstract {

    private final String[] cssValues;

    private final StyleValue[] styleValues;

    private final StyleProperty[] styleProperties;

    private final String propertyName;

    protected GenericAllEdgeTestCaseAbstract(
            String propertyName, String[] values,
            StyleValue[] styleValues,
            StyleProperty[] styleProperties) {

        this.propertyName = propertyName;
        this.cssValues = values;
        this.styleValues = styleValues;
        this.styleProperties = styleProperties;
    }

    /**
     * Test that one value sets all the properties.
     */
    public void testOneValue() throws Exception {

        StyleValue value = styleValues[0];
        String cssValue = cssValues[0];

        // They should all be set to the same value.
        expectSetProperty(PropertyGroups.TOP, value);
        expectSetProperty(PropertyGroups.BOTTOM, value);
        expectSetProperty(PropertyGroups.LEFT, value);
        expectSetProperty(PropertyGroups.RIGHT, value);

        parseDeclarations(propertyName + ": " + cssValue);
    }

    /**
     * Test that two value sets top/bottom left/right.
     */
    public void testTwoValue() throws Exception {

        StyleValue value1 = styleValues[0];
        StyleValue value2 = styleValues[1];
        String cssValue1 = cssValues[0];
        String cssValue2 = cssValues[1];

        // Top/Bottom set to first value, Left/Right set to second.
        expectSetProperty(PropertyGroups.TOP, value1);
        expectSetProperty(PropertyGroups.BOTTOM, value1);
        expectSetProperty(PropertyGroups.LEFT, value2);
        expectSetProperty(PropertyGroups.RIGHT, value2);

        parseDeclarations(propertyName + ": " + cssValue1 + " " + cssValue2);
    }

    /**
     * Test that three values sets top left/right bottom
     */
    public void testThreeValue() throws Exception {

        StyleValue value1 = styleValues[0];
        StyleValue value2 = styleValues[1];
        StyleValue value3 = styleValues[2];
        String cssValue1 = cssValues[0];
        String cssValue2 = cssValues[1];
        String cssValue3 = cssValues[2];

        // Top set to first value, Left/Right set to second, Bottom set to
        // third.
        expectSetProperty(PropertyGroups.TOP, value1);

        expectSetProperty(PropertyGroups.LEFT, value2);
        expectSetProperty(PropertyGroups.RIGHT, value2);

        expectSetProperty(PropertyGroups.BOTTOM, value3);

        parseDeclarations(propertyName + ": " +
                          cssValue1 + " " +
                          cssValue2 + " " +
                          cssValue3);
    }

    /**
     * Test that four values sets top right bottom left
     */
    public void testFourValue() throws Exception {

        StyleValue value1 = styleValues[0];
        StyleValue value2 = styleValues[1];
        StyleValue value3 = styleValues[2];
        StyleValue value4 = styleValues[3];
        String cssValue1 = cssValues[0];
        String cssValue2 = cssValues[1];
        String cssValue3 = cssValues[2];
        String cssValue4 = cssValues[3];

        // Top set to first value, Right set to second, Bottom set to
        // third, Left set to fourth
        expectSetProperty(PropertyGroups.TOP, value1);

        expectSetProperty(PropertyGroups.RIGHT, value2);

        expectSetProperty(PropertyGroups.BOTTOM, value3);

        expectSetProperty(PropertyGroups.LEFT, value4);

        parseDeclarations(propertyName + ": " +
                          cssValue1 + " " +
                          cssValue2 + " " +
                          cssValue3 + " " +
                          cssValue4);
    }

    private void expectSetProperty(int i, final StyleValue value) {
        PropertyValue propertyValue =
            ThemeFactory.getDefaultInstance().createPropertyValue(
                styleProperties[i], value);
        mutableStylePropertiesMock.expects.setPropertyValue(propertyValue);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
