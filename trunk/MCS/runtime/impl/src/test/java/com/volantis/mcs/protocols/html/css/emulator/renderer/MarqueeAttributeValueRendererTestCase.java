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
package com.volantis.mcs.protocols.html.css.emulator.renderer;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.themes.properties.MCSMarqueeRepetitionKeywords;
import com.volantis.mcs.themes.properties.MCSMarqueeStyleKeywords;
import com.volantis.mcs.themes.StyleInteger;
import com.volantis.mcs.themes.StyleValueFactory;

/**
 * Verify that {@link MarqueeAttributeValueRenderer} behaves as
 * expected.
 */
public class MarqueeAttributeValueRendererTestCase
        extends TestCaseAbstract {

    /**
     * Verify that the keyword {@link MCSMarqueeRepetitionKeywords.INFINITE} is
     * converted to a numerical value on rendering if the keyword is not
     * supported.
     */
    public void testRenderInfiniteRepetition() {
        MarqueeAttributeValueRenderer renderer =
                new MarqueeAttributeValueRenderer(false);
        final String renderedValue = renderer.render(
                MCSMarqueeRepetitionKeywords.INFINITE);
        assertEquals("16", renderedValue);
    }

    /**
     * Verify that the keyword {@link MCSMarqueeRepetitionKeywords.INFINITE} is
     * not converted to a numerical value on rendering if the keyword is
     * supported.
     */
    public void testRenderInfiniteRepetitionWhenKeywordSupported() {
        MarqueeAttributeValueRenderer renderer =
                new MarqueeAttributeValueRenderer(true);
        final String renderedValue = renderer.render(
                MCSMarqueeRepetitionKeywords.INFINITE);
        assertEquals("infinite", renderedValue);
    }

    /**
     * Verify that rendering a negative value for the
     * {@link com.volantis.mcs.themes.StylePropertyDetails.MCS_MARQUEE_REPETITION}
     * results in the initial value being used.
     */
    public void testRenderNegativeRepetition() {
        MarqueeAttributeValueRenderer renderer =
                new MarqueeAttributeValueRenderer(false);
        final StyleInteger integer =
            StyleValueFactory.getDefaultInstance().getInteger(null, -16);
        final String renderedValue = renderer.render(integer);
        assertEquals("1", renderedValue);
    }

    /**
     * Verify that rendering a value for the
     * {@link com.volantis.mcs.themes.StylePropertyDetails.MCS_MARQUEE_REPETITION}
     * which is too large results in the initial value being used.
     */
    public void testRenderTooMuchRepetition() {
        MarqueeAttributeValueRenderer renderer =
                new MarqueeAttributeValueRenderer(false);
        final StyleInteger integer =
            StyleValueFactory.getDefaultInstance().getInteger(null, 17);
        final String renderedValue = renderer.render(integer);
        assertEquals("1", renderedValue);
    }

    /**
     * Verify that a normal keyword and a normal numerical value for a marquee
     * style are rendered correctly.
     */
    public void testRender() {
        MarqueeAttributeValueRenderer renderer =
                new MarqueeAttributeValueRenderer(false);

        // Test a normal keyword.
        String renderedValue = renderer.render(
                MCSMarqueeStyleKeywords.SCROLL);
        assertEquals(MCSMarqueeStyleKeywords.SCROLL.getStandardCSS(),
                renderedValue);

        // Test a normal integer repetition.
        final StyleInteger integer =
            StyleValueFactory.getDefaultInstance().getInteger(null, 7);
        renderedValue = renderer.render(integer);
        assertEquals("7", renderedValue);
    }
}
