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

import com.volantis.mcs.css.impl.parser.ParsingMockTestCaseAbstract;
import com.volantis.mcs.themes.MutableStylePropertiesMock;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.styling.properties.StyleProperty;

/**
 * Base for classes that test setting a single property using mocks.
 */
public abstract class ParsingPropertiesMockTestCaseAbstract
        extends ParsingMockTestCaseAbstract {

    protected MutableStylePropertiesMock mutableStylePropertiesMock;

    protected void setUp() throws Exception {
        super.setUp();

        mutableStylePropertiesMock = new MutableStylePropertiesMock(
                "mutableStylePropertiesMock", expectations);

        // Create a mutable style properties mock.
        styleSheetFactoryMock.expects.createStyleProperties()
                .returns(mutableStylePropertiesMock);
    }

    protected void expectSetProperty(
            final StyleProperty property, StyleValue value) {
        expectSetProperty(ThemeFactory.getDefaultInstance().createPropertyValue(
            property, value));
    }

    protected void expectSetProperty(final PropertyValue propertyValue) {
        mutableStylePropertiesMock.expects.setPropertyValue(propertyValue);
    }

    protected void expectProperties(
            StyleValue[] values, final StyleProperty[] properties) {

        expectProperties(properties, values, Priority.NORMAL);
    }

    protected void expectProperties(
            final StyleProperty[] properties, StyleValue[] values,
            Priority priority) {

        if (values.length != properties.length) {
            throw new IllegalArgumentException("Must have " +
                    properties.length +
                    " entries");
        }

        for (int i = 0; i < values.length; i++) {
            StyleValue value = values[i];
            StyleProperty property = properties[i];
            if (value == null) {
                value = property.getStandardDetails().getInitialValue();
            }
            if (value != null) {
                PropertyValue propertyValue =
                    ThemeFactory.getDefaultInstance().createPropertyValue(
                        property, value, priority);
                expectSetProperty(propertyValue);
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/2	pduffin	VBM:2005111405 Massive changes for performance

 28-Sep-05	9487/4	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
