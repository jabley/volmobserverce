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

package com.volantis.mcs.css.renderer.shorthand;

import com.volantis.mcs.css.renderer.PropertyRendererTestAbstract;
import com.volantis.mcs.css.renderer.PropertyRenderer;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.StyleShorthands;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.properties.FontStyleKeywords;
import com.volantis.mcs.themes.properties.FontVariantKeywords;
import com.volantis.mcs.themes.properties.FontWeightKeywords;
import com.volantis.mcs.themes.properties.FontSizeKeywords;
import com.volantis.mcs.themes.properties.LineHeightKeywords;
import com.volantis.mcs.themes.properties.FontFamilyKeywords;
import com.volantis.mcs.themes.values.FontShorthandValue;

/**
 * Test cases for {@link FontRenderer}.
 */
public class FontRendererTestCase
        extends PropertyRendererTestAbstract {

    /**
     * Ensure that when the font value contains line height it is separated by
     * '/' from the font-size.
     */
    public void testLineHeightSeparator() throws Exception {

        PropertyRenderer renderer = new FontRenderer();

        MutableStyleProperties properties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();

        FontShorthandValue value = new FontShorthandValue(
                StyleShorthands.FONT,
                new StyleValue[] {
                    FontStyleKeywords.ITALIC,
                    FontVariantKeywords.NORMAL,
                    FontWeightKeywords.WEIGHT__700,
                    FontSizeKeywords.LARGE,
                    LineHeightKeywords.NORMAL,
                    FontFamilyKeywords.FANTASY,
                }, Priority.NORMAL);

        properties.setShorthandValue(value);

        String actual = render(renderer, properties);
        assertEquals("font:italic normal 700 large/normal fantasy;", actual);
    }

}
