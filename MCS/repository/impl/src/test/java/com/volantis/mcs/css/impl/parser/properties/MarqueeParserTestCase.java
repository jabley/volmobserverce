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
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.MCSMarqueeDirectionKeywords;
import com.volantis.mcs.themes.properties.MCSMarqueeRepetitionKeywords;
import com.volantis.mcs.themes.properties.MCSMarqueeSpeedKeywords;
import com.volantis.mcs.themes.properties.MCSMarqueeStyleKeywords;

/**
 * Test {@link MarqueeParser}.
 */
public class MarqueeParserTestCase
    extends ParsingPropertiesMockTestCaseAbstract {

    private static StyleValue KEYWORD_NORMAL = MCSMarqueeSpeedKeywords.NORMAL;
    private static StyleValue KEYWORD_LEFT = MCSMarqueeDirectionKeywords.LEFT;
    private static StyleValue KEYWORD_INFINITE = MCSMarqueeRepetitionKeywords.INFINITE;
    private static StyleValue KEYWORD_SCROLL = MCSMarqueeStyleKeywords.SCROLL;

    /**
     * Test that inherit sets the values properly.
     */
    public void testInherit()
            throws Exception {

        StyleValue inherit = STYLE_VALUE_FACTORY.getInherit();
        expectProperties(new StyleValue[] {
            inherit,
            inherit,
            inherit,
            inherit,
        }, PropertyGroups.MARQUEE_PROPERTIES);

        parseDeclarations("mcs-marquee: inherit");
    }

    /**
     * Test that separate marquee speed, direction, repetition and style work.
     */
    public void testSpeedDirectionRepetitionStyle() throws Exception {

        expectProperties(new StyleValue[] {
            KEYWORD_LEFT,
            KEYWORD_INFINITE,
            KEYWORD_NORMAL,
            KEYWORD_SCROLL,
        }, PropertyGroups.MARQUEE_PROPERTIES);

        parseDeclarations("mcs-marquee: left infinite normal scroll");
    }   
}
