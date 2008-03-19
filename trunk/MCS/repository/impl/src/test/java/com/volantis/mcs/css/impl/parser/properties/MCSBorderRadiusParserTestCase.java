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

package com.volantis.mcs.css.impl.parser.properties;

import com.volantis.mcs.themes.PropertyGroups;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.values.LengthUnit;

/**
 * Test the parser for border radius.
 */
public class MCSBorderRadiusParserTestCase
        extends ParsingPropertiesMockTestCaseAbstract {

    /**
     * Make sure that a single value works properly.
     */
    public void testSinglePropertySingleValue() {

        StyleValue expected 
            = STYLE_VALUE_FACTORY.getPair(LENGTH_2CM, LENGTH_2CM);

        expectSetProperty(StylePropertyDetails.MCS_BORDER_BOTTOM_LEFT_RADIUS, expected);

        parseDeclarations("mcs-border-bottom-left-radius: 2cm");
    }
    
    /**
     * Make sure that two values works properly.
     */
    public void testSinglePropertyTwoValues() throws Exception {

        StyleValue expected = STYLE_VALUE_FACTORY.getPair(
                LENGTH_1PX, LENGTH_2CM);

        expectSetProperty(StylePropertyDetails.MCS_BORDER_BOTTOM_LEFT_RADIUS, expected);

        parseDeclarations("mcs-border-bottom-left-radius: 1px 2cm");
    }

    /**
     * Make sure that single value works properly for shorthand.
     */
    public void testShorthandSingleValue() throws Exception {

        StyleValue expected 
            = STYLE_VALUE_FACTORY.getPair(LENGTH_2CM, LENGTH_2CM);

        expectSetProperty(StylePropertyDetails.MCS_BORDER_TOP_LEFT_RADIUS, expected);
        expectSetProperty(StylePropertyDetails.MCS_BORDER_TOP_RIGHT_RADIUS, expected);
        expectSetProperty(StylePropertyDetails.MCS_BORDER_BOTTOM_RIGHT_RADIUS, expected);
        expectSetProperty(StylePropertyDetails.MCS_BORDER_BOTTOM_LEFT_RADIUS, expected);

        parseDeclarations("mcs-border-radius: 2cm");
    }

    /**
     * Make sure that two values works properly for shorthand.
     */
    public void testShorthandTwoValues() throws Exception {

        StyleValue expected = STYLE_VALUE_FACTORY.getPair(
                LENGTH_1PX, LENGTH_2CM);

        expectSetProperty(StylePropertyDetails.MCS_BORDER_TOP_LEFT_RADIUS, expected);
        expectSetProperty(StylePropertyDetails.MCS_BORDER_TOP_RIGHT_RADIUS, expected);
        expectSetProperty(StylePropertyDetails.MCS_BORDER_BOTTOM_RIGHT_RADIUS, expected);
        expectSetProperty(StylePropertyDetails.MCS_BORDER_BOTTOM_LEFT_RADIUS, expected);

        parseDeclarations("mcs-border-radius: 1px 2cm");
    }
}
