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

package com.volantis.mcs.css.renderer;

import com.volantis.mcs.css.renderer.shorthand.ShorthandPropertyRenderer;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.StyleShorthand;
import com.volantis.mcs.themes.StyleShorthands;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.MCSMarqueeDirectionKeywords;
import com.volantis.mcs.themes.properties.MCSMarqueeRepetitionKeywords;
import com.volantis.mcs.themes.properties.MCSMarqueeSpeedKeywords;
import com.volantis.mcs.themes.properties.MCSMarqueeStyleKeywords;
import com.volantis.mcs.themes.values.ShorthandValue;
import com.volantis.mcs.utilities.ReusableStringWriter;

/**
 * Test the shorthand property renderer.
 */
public class ShorthandPropertyRendererTestCase
        extends PropertyRendererTestAbstract {

    /**
     * Verify that a shorthand, which has a mapping defined from its internal
     * MCS name to the external CSS name, is rendered using the correct
     * external representation.
     *
     * @throws Exception if there was a problem running the test
     */
    public void testRenderShorthandWithExternalMapping() throws Exception {
        
        // These values must be in the order they appear in the style
        // properties, or the test will fail. In normal operation, they are
        // created by ShorthandAnalyser.createShorthandValue, which guarantees
        // this.
        StyleValue[] styleValues = new StyleValue[] {
            MCSMarqueeDirectionKeywords.LEFT,
            MCSMarqueeRepetitionKeywords.INFINITE,
            MCSMarqueeSpeedKeywords.NORMAL,
            MCSMarqueeStyleKeywords.SCROLL,
        };

        final String expected = "-wap-marquee" + ":" +
                MCSMarqueeDirectionKeywords.LEFT.getStandardCSS() + " " +
                MCSMarqueeRepetitionKeywords.INFINITE.getStandardCSS() + " " +
                MCSMarqueeSpeedKeywords.NORMAL.getStandardCSS() + " " +
                MCSMarqueeStyleKeywords.SCROLL.getStandardCSS() + ";";

        doTest(StyleShorthands.MARQUEE, styleValues, expected);
    }

    /**
     * Test that rendering using the supplied style shorthand and style values
     * results in the expected output.
     *
     * @param styleShorthand    whose rendering to test
     * @param styleValues       of the properties encapsulated by this shorthand
     * @param expected          expected result of rendering
     */
    private void doTest(StyleShorthand styleShorthand,
                        StyleValue[] styleValues,
                        String expected) throws Exception {

        ShorthandPropertyRenderer renderer = new ShorthandPropertyRenderer(
                styleShorthand);

        ShorthandValue shorthandValue = new ShorthandValue(
                StyleShorthands.MARQUEE, styleValues, Priority.NORMAL);
        properties.setShorthandValue(shorthandValue);

        renderer.render(properties, context);

        final String result = ((ReusableStringWriter) context.getWriter()).
                getBuffer().toString();
        assertEquals("Rendered value is incorrect", expected, result);
    }
}
