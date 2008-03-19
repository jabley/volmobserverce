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

import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.PropertyGroups;
import com.volantis.mcs.themes.StyleValue;

/**
 * Test background parser.
 */
public class BackgroundParserTestCase
        extends ParsingPropertiesMockTestCaseAbstract {

    /**
     * Test position specified as a single value following by a single element.
     */
    public void testPosition() throws Exception {

        expectProperties(new StyleValue[]{
            null, // attachment
            COLOR_GREEN, // color
            null, // image
            STYLE_VALUE_FACTORY.getPair(LENGTH_1PX, PERCENTAGE_50), // position
            null, // repeat
        }, PropertyGroups.BACKGROUND_PROPERTIES);

        parseDeclarations("background: 1px green");
    }

    /**
     * Test image.
     */
    public void testImageComponentURI() throws Exception {

        expectProperties(new StyleValue[]{
            null, // attachment
            null, // color
            STYLE_VALUE_FACTORY.getComponentURI(null, "/image.png"), // image
            null, // position
            null, // repeat
        }, PropertyGroups.BACKGROUND_PROPERTIES);

        parseDeclarations("background: mcs-component-url('/image.png')");
    }

    /**
     * Test image.
     */
    public void testImageTranscodableURI() throws Exception {

        expectProperties(new StyleValue[]{
            null, // attachment
            null, // color
            STYLE_VALUE_FACTORY.getTranscodableURI(null, "/image.png"), // image
            null, // position
            null, // repeat
        }, PropertyGroups.BACKGROUND_PROPERTIES);

        parseDeclarations("background: mcs-transcodable-url('/image.png')");
    }

    /**
     * Test important.
     */
    public void testImportant() throws Exception {

        expectProperties(PropertyGroups.BACKGROUND_PROPERTIES,
                new StyleValue[]{
                    null, // attachment
                    null, // color
                    STYLE_VALUE_FACTORY.getComponentURI(null, "/image.png"), // image
                    null, // position
                    null, // repeat
                },
                Priority.IMPORTANT);

        parseDeclarations(
                "background: mcs-component-url('/image.png') !important");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
