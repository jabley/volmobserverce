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

package com.volantis.styling.properties;

import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.BackgroundColorKeywords;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link StylePropertyImpl}.
 */
public class StylePropertyImplTestCase
        extends TestCaseAbstract {

    /**
     * Test that a keyword will be correctly rendered as a string.
     *
     * @throws Exception
     */
    public void testKeywordAsString() throws Exception {

        StyleValue value = BackgroundColorKeywords.TRANSPARENT;
        String string = value.getStandardCSS();
        assertEquals("Background color value", "transparent", string);
    }

    /**
     * Test that a color will be correctly rendered as a string.
     *
     * @throws Exception
     */
    public void testColorAsString() throws Exception {

        StyleValue value =
            StyleValueFactory.getDefaultInstance().getColorByRGB(null, 0x123456);
        String string = value.getStandardCSS();
        assertEquals("Background color", "#123456", string);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 ===========================================================================
*/
