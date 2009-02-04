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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.mcs.themes.properties.MCSMenuHorizontalSeparatorKeywords;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * This is a test case for {@link DefaultHorizontalSeparatorRenderer},  It
 * tests the various possible outcomes from rendering using this separator
 * renderer.
 */
public class DefaultHorizontalSeparatorRendererTestCase
        extends TestCaseAbstract {

    /**
     * Creates a new instance of this test class.
     */
    public DefaultHorizontalSeparatorRendererTestCase() {
    }

    /**
     * Creates a new named instance of this test class.
     *
     * @param s The name of the test class.
     */
    public DefaultHorizontalSeparatorRendererTestCase(String s) {
        super(s);
    }

    // JavaDoc inherited
    protected void setUp() throws Exception {
        // compiled code
    }

    // JavaDoc inherited
    protected void tearDown() throws Exception {
        // compiled code
    }

    /**
     * This tests the functionality of the render method with no separator.
     */
    public void testRenderWithNoSeparator() throws Exception {
        SeparatorRenderer renderer = new DefaultHorizontalSeparatorRenderer(
                MCSMenuHorizontalSeparatorKeywords.NONE);

        // Create expected
        String expected = "";

        // Test
        testRenderer(renderer, expected);
    }

    /**
     * This tests the functionality of the render method with a space separator.
     */
    public void testRenderWithSpaceSeparator() throws Exception {
        SeparatorRenderer renderer = new DefaultHorizontalSeparatorRenderer(
                MCSMenuHorizontalSeparatorKeywords.SPACE);

        // Create expected
        String expected = " ";

        // Test
        testRenderer(renderer, expected);
    }

    /**
     * This tests the functionality of the render method with a non breaking
     * space separator.
     */
    public void testRenderWithNonBreakingSpaceSeparator() throws Exception {
        SeparatorRenderer renderer = new DefaultHorizontalSeparatorRenderer(
                MCSMenuHorizontalSeparatorKeywords.NON_BREAKING_SPACE);

        // Create expected
        String expected = VolantisProtocol.NBSP;

        // Test
        testRenderer(renderer, expected);

    }

    /**
     * Utility method that given a separator renderer and an expected string
     * will perform rendering using that renderer and then compare the output
     * with the expected string.  Varios asserts are made to ensure the code
     * is operating as expected.
     *
     * @param renderer   The separator renderer to use to get output
     * @param expected   The expected output string
     * @throws Exception If there is a problem rendering or with any test assert
     */
    private void testRenderer(SeparatorRenderer renderer,
                              String expected) throws Exception {
        // Create the test buffer
        TestDOMOutputBuffer testBuffer = new TestDOMOutputBuffer();

        // Start span element
        testBuffer.openElement("span");

        // Render using the test buffer
        renderer.render(testBuffer);

        // End span element
        testBuffer.closeElement("span");

        // Extract the output from the rendering as a string.
        String actual = DOMUtilities.toString(testBuffer.getRoot());
        assertNotNull("The actual string should exist", actual);

        // Normalise expected - this requires that all input strings contain
        // some markup otherwise the test will fail on this string conversion!
        expected = DOMUtilities.provideDOMNormalizedString("<span>" +
                                                           expected +
                                                           "</span>");

        // Compare state and expected
        assertEquals("Strings should match: expected = \"" + expected +
                     "\" and actual = \"" + actual + "\"", expected, actual);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 21-Apr-04	3956/3	claire	VBM:2004042008 Provision of Default Horizontal Separator Renderer

 ===========================================================================
*/
