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

import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.values.StyleColorNames;

import java.util.ArrayList;
import java.util.List;

/**
 * Test for {@link MCSChartForegroundColorsParser}.
 */
public class MCSChartForegroundColorsParserTestCase
    extends ParsingPropertiesMockTestCaseAbstract {

    /**
     * Test the multiple values can be specified.
     */
    public void testMultiple() throws Exception {

        List values = new ArrayList();
        values.add(StyleColorNames.RED);
        values.add(StyleColorNames.GREEN);
        values.add(StyleColorNames.BLUE);
        StyleList list = STYLE_VALUE_FACTORY.getList(values);

        expectSetProperty(StylePropertyDetails.MCS_CHART_FOREGROUND_COLORS,
                          list);

        parseDeclarations("mcs-chart-foreground-colors: red, green, blue");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10610/1	ianw	VBM:2005120206 Added browse actions for font-family and mcs-chart-forground-color

 06-Dec-05	10608/1	ianw	VBM:2005120206 Added browse actions for font-family and mcs-chart-forground-color

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
