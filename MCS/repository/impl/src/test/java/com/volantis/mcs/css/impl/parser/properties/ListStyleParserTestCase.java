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

import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleURI;
import com.volantis.mcs.themes.properties.ListStylePositionKeywords;
import com.volantis.mcs.themes.properties.ListStyleTypeKeywords;
import com.volantis.styling.properties.StyleProperty;

/**
 * Test for {@link ListStyleParser}.
 */ 
public class ListStyleParserTestCase
    extends ParsingPropertiesMockTestCaseAbstract {

    /**
     * Test that inherit sets the values properly.
     *
     * <p>This is commented out as there is a problem with the renderer.</p>
     */
    public void testInherit()
            throws Exception {

        StyleValue inherit = STYLE_VALUE_FACTORY.getInherit();

        expectSetProperty(StylePropertyDetails.LIST_STYLE_IMAGE, inherit);
        expectSetProperty(StylePropertyDetails.LIST_STYLE_TYPE, inherit);
        expectSetProperty(StylePropertyDetails.LIST_STYLE_POSITION, inherit);

        parseDeclarations("list-style: inherit");
    }

    public void testAllCombinations() throws Exception {

        String cssValues[] = {
            "url(/image.png)",
            "disc",
            "inside"
        };

        int count = cssValues.length;

        StyleValue[] styleValues = {
            STYLE_VALUE_FACTORY.getURI(null, "/image.png"),
            ListStyleTypeKeywords.DISC,
            ListStylePositionKeywords.INSIDE
        };

        StyleProperty[] styleProperties = {
            StylePropertyDetails.LIST_STYLE_IMAGE,
            StylePropertyDetails.LIST_STYLE_TYPE,
            StylePropertyDetails.LIST_STYLE_POSITION
        };

        StyleValue[] initialValues = new StyleValue[count];
        for (int i = 0; i < styleProperties.length; i++) {
            StyleProperty property = styleProperties[i];
            initialValues[i] = property.getStandardDetails().getInitialValue();
        }

        String propertyName = "list-style";
        StringBuffer buffer = new StringBuffer();
        for (int i = 1; i < (1 << count); i += 1) {

            if (i > 1) {
                styleSheetFactoryMock.expects.createStyleProperties()
                        .returns(mutableStylePropertiesMock);
            }

            buffer.setLength(0);
            buffer.append(propertyName +
                          ":");
            for (int j = 0; j < count; j += 1) {
                if ((i & (1 << j)) != 0) {
                    buffer.append(" ").append(cssValues[j]);

                    expectSetProperty(styleProperties[j], styleValues[j]);
                } else {
                    expectSetProperty(styleProperties[j], initialValues[j]);
                }
            }

            String css = buffer.toString();
            System.out.println("Testing - " + css);
            parseDeclarations(css);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/2	pduffin	VBM:2005111405 Massive changes for performance

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
