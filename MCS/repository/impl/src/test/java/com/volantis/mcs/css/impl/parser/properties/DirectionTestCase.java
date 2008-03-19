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
import com.volantis.mcs.themes.properties.DirectionStyleKeywords;

/**
 * Test the parsing of direction properties.
 */
public class DirectionTestCase extends ParsingPropertiesMockTestCaseAbstract {

    /**
     * Make sure that direction left to right works properly.
     */
    public void testLeftToRightValue() throws Exception {

        expectSetProperty(StylePropertyDetails.DIRECTION,
            DirectionStyleKeywords.LTR);

        parseDeclarations("direction: ltr");
    }

    /**
     * Make sure that direction right to left works properly.
     */
    public void testRightToLeftValue() throws Exception {

        expectSetProperty(StylePropertyDetails.DIRECTION,
            DirectionStyleKeywords.RTL);

        parseDeclarations("direction: rtl");
    }

}
