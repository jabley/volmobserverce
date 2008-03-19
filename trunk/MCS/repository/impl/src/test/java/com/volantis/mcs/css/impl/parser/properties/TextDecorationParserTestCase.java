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

import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.MCSTextBlinkKeywords;
import com.volantis.mcs.themes.properties.MCSTextLineThroughStyleKeywords;
import com.volantis.mcs.themes.properties.MCSTextOverlineStyleKeywords;
import com.volantis.mcs.themes.properties.MCSTextUnderlineStyleKeywords;
import com.volantis.styling.properties.StyleProperty;

/**
 * Test for {@link TextDecorationParser}.
 */
public class TextDecorationParserTestCase
        extends ParsingPropertiesMockTestCaseAbstract {

    private static final StyleProperty[] PROPERTIES = new StyleProperty[]{
                StylePropertyDetails.MCS_TEXT_BLINK,
                StylePropertyDetails.MCS_TEXT_LINE_THROUGH_STYLE,
                StylePropertyDetails.MCS_TEXT_OVERLINE_STYLE,
                StylePropertyDetails.MCS_TEXT_UNDERLINE_STYLE,
            };

    /**
     * Test that parsing none works properly.
     *
     * @throws Exception
     */
    public void testNone() throws Exception {

        doTestParser(new String[]{
            "none"
        }, PROPERTIES, new StyleKeyword[] {
            MCSTextBlinkKeywords.NONE,
            MCSTextLineThroughStyleKeywords.NONE,
            MCSTextOverlineStyleKeywords.NONE,
            MCSTextUnderlineStyleKeywords.NONE,
        });
    }

    private void doTestParser(
            String[] cssValues, StyleProperty[] properties,
            StyleKeyword[] keywords) {

        StringBuffer buffer = new StringBuffer("text-decoration:");
        for (int i = 0; i < cssValues.length; i++) {
            String cssValue = cssValues[i];

            buffer.append(" ").append(cssValue);
        }

        for (int i = 0; i < properties.length; i++) {
            StyleProperty property = properties[i];

            expectSetProperty(property, keywords[i]);
        }

        parseDeclarations(buffer.toString());
    }

    /**
     * Test that parsing a single blink keyword works properly.
     *
     * @throws Exception
     */
    public void testSingleBlinkKeyword() throws Exception {

        doTestParser(new String[]{
            "blink"
        }, PROPERTIES, new StyleKeyword[]{
            MCSTextBlinkKeywords.BLINK,
            MCSTextLineThroughStyleKeywords.NONE,
            MCSTextOverlineStyleKeywords.NONE,
            MCSTextUnderlineStyleKeywords.NONE,
        });
    }

    /**
     * Test that parsing a single line-through keyword works properly.
     *
     * @throws Exception
     */
    public void testSingleLineThroughKeyword() throws Exception {

        doTestParser(new String[]{
            "line-through"
        }, PROPERTIES, new StyleKeyword[]{
            MCSTextBlinkKeywords.NONE,
            MCSTextLineThroughStyleKeywords.SOLID,
            MCSTextOverlineStyleKeywords.NONE,
            MCSTextUnderlineStyleKeywords.NONE,
        });
    }

    /**
     * Test that parsing a single overline keyword works properly.
     *
     * @throws Exception
     */
    public void testSingleOverlineKeyword() throws Exception {

        doTestParser(new String[]{
            "overline"
        }, PROPERTIES, new StyleKeyword[]{
            MCSTextBlinkKeywords.NONE,
            MCSTextLineThroughStyleKeywords.NONE,
            MCSTextOverlineStyleKeywords.SOLID,
            MCSTextUnderlineStyleKeywords.NONE,
        });
    }

    /**
     * Test that parsing a single underline keyword works properly.
     *
     * @throws Exception
     */
    public void testSingleUnderlineKeyword() throws Exception {

        doTestParser(new String[]{
            "underline"
        }, PROPERTIES, new StyleKeyword[]{
            MCSTextBlinkKeywords.NONE,
            MCSTextLineThroughStyleKeywords.NONE,
            MCSTextOverlineStyleKeywords.NONE,
            MCSTextUnderlineStyleKeywords.SOLID,
        });
    }

    /**
     * Test that parsing a number of keywords works properly.
     *
     * @throws Exception
     */
    public void testMultipleKeywords() throws Exception {

        doTestParser(new String[]{
            "line-through",
            "underline"
        }, PROPERTIES, new StyleKeyword[]{
            MCSTextBlinkKeywords.NONE,
            MCSTextLineThroughStyleKeywords.SOLID,
            MCSTextOverlineStyleKeywords.NONE,
            MCSTextUnderlineStyleKeywords.SOLID,
        });
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
