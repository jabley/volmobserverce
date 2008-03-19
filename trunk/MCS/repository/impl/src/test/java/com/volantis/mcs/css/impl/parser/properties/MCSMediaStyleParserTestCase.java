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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.css.impl.parser.properties;

import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.MCSMediaStyleKeywords;

import java.util.ArrayList;
import java.util.List;

/**
 * Test for {@link MCSMediaStyleParser}.
 */
public class MCSMediaStyleParserTestCase
    extends ParsingPropertiesMockTestCaseAbstract {

    /**
     * Test the multiple values can be specified.
     */
    public void testMultiple() throws Exception {

        List values = new ArrayList();
        values.add(MCSMediaStyleKeywords.INTEGRAL);
        values.add(MCSMediaStyleKeywords.EXTERNAL);
        StyleList list = STYLE_VALUE_FACTORY.getList(values);

        expectSetProperty(StylePropertyDetails.MCS_MEDIA_STYLE,
                          list);

        parseDeclarations("mcs-media-style: integral external");
    }
}
