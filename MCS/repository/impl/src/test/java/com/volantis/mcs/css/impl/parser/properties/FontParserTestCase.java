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
import com.volantis.mcs.themes.font.UnknownFontValue;
import com.volantis.mcs.themes.properties.FontStyleKeywords;
import com.volantis.mcs.themes.properties.FontVariantKeywords;
import com.volantis.mcs.themes.properties.FontWeightKeywords;
import com.volantis.mcs.themes.properties.MCSSystemFontKeywords;
import com.volantis.styling.properties.StyleProperty;

/**
 * Test for {@link FontParser}.
 */
public class FontParserTestCase
    extends ParsingPropertiesMockTestCaseAbstract {

    /**
     * The font properties in order.
     */
    private static final StyleProperty[] PROPERTIES =
            new StyleProperty[]{
                StylePropertyDetails.FONT_STYLE,
                StylePropertyDetails.FONT_VARIANT,
                StylePropertyDetails.FONT_WEIGHT,
                StylePropertyDetails.FONT_SIZE,
                StylePropertyDetails.LINE_HEIGHT,
                StylePropertyDetails.FONT_FAMILY,
                StylePropertyDetails.MCS_SYSTEM_FONT,
            };

    /**
     * Test system font.
     */
    public void testSystemFont() throws Exception {

        expectProperties(new StyleValue[] {
            UnknownFontValue.INSTANCE, // style
            UnknownFontValue.INSTANCE, // variant
            UnknownFontValue.INSTANCE, // weight
            UnknownFontValue.INSTANCE, // size
            UnknownFontValue.INSTANCE, // line height
            UnknownFontValue.INSTANCE, // family
            MCSSystemFontKeywords.CAPTION, // mcs system
        }, PROPERTIES);

        parseDeclarations("font: caption");
    }

    /**
     * Test font style.
     */
    public void testFontStyle() throws Exception {

        expectProperties(new StyleValue[] {
            FontStyleKeywords.ITALIC, // style
            null, // variant
            null, // weight
            null, // size
            null, // line height
            FONT_SERIF, // family
            null, // mcs system
        }, PROPERTIES);

        parseDeclarations("font: italic serif");
    }

    /**
     * Test font variant.
     */
    public void testFontVariant() throws Exception {

        expectProperties(new StyleValue[] {
            null, // style
            FontVariantKeywords.SMALL_CAPS, // variant
            null, // weight
            null, // size
            null, // line height
            FONT_SERIF, // family
            null, // mcs system
        }, PROPERTIES);

        parseDeclarations("font: small-caps serif");
    }

    /**
     * Test font weight.
     */
    public void testFontWeight() throws Exception {

        expectProperties(new StyleValue[] {
            null, // style
            null, // variant
            FontWeightKeywords.WEIGHT__100, // weight
            null, // size
            null, // line height
            FONT_SERIF, // family
            null, // mcs system
        }, PROPERTIES);

        parseDeclarations("font: 100 serif");
    }

    /**
     * Test font style and weight.
     */
    public void testFontStyleAndWeight() throws Exception {

        expectProperties(new StyleValue[] {
            FontStyleKeywords.ITALIC, // style
            null, // variant
            FontWeightKeywords.WEIGHT__100, // weight
            null, // size
            null, // line height
            FONT_SERIF, // family
            null, // mcs system
        }, PROPERTIES);

        parseDeclarations("font: italic 100 serif");
    }

    /**
     * Test font size.
     */
    public void testFontSize() throws Exception {

        expectProperties(new StyleValue[] {
            null, // style
            null, // variant
            null, // weight
            LENGTH_2CM, // size
            null, // line height
            FONT_SERIF, // family
            null, // mcs system
        }, PROPERTIES);

        parseDeclarations("font: 2cm serif");
    }

    /**
     * Test font size with line height
     */
    public void testFontSizeWithLineHeight() throws Exception {

        expectProperties(new StyleValue[] {
            null, // style
            null, // variant
            null, // weight
            LENGTH_2CM, // size
            PERCENTAGE_50, // line height
            FONT_SERIF, // family
            null, // mcs system
        }, PROPERTIES);

        parseDeclarations("font: 2cm/50% serif");
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
