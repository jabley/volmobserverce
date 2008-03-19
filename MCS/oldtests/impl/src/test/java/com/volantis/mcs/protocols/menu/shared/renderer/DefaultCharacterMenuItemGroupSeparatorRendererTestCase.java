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

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.dom.debug.DOMUtilities;

/**
 * This is a test case for {@link DefaultCharacterMenuItemGroupSeparatorRenderer},
 * It tests various possible outcomes from rendering using this separator
 * renderer.
 */
public class DefaultCharacterMenuItemGroupSeparatorRendererTestCase
        extends TestCaseAbstract {

    /**
     * Creates a new instance of this test class.
     */
    public DefaultCharacterMenuItemGroupSeparatorRendererTestCase() {
    }

    /**
     * Creates a new named instance of this test class.
     *
     * @param s The name of the test class.
     */
    public DefaultCharacterMenuItemGroupSeparatorRendererTestCase(String s) {
        super(s);
    }

    // JavaDoc inherited
    protected void setUp() throws Exception {
        super.setUp();
    }

    // JavaDoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    /**
     * This tests illegal separator renderer construction
     */
    public void testRenderIllegal() throws Exception {
        // Test null character string
        try {
            SeparatorRenderer renderer =
                    new DefaultCharacterMenuItemGroupSeparatorRenderer(null,
                                                                       1);
            fail("Construction of renderer should have been illegal (1)");
        } catch (IllegalArgumentException iae) {
            // Test was successfully if got here as the exception was thrown ;-)
        }

        // Test empty character string
        try {
            SeparatorRenderer renderer =
                    new DefaultCharacterMenuItemGroupSeparatorRenderer("",
                                                                       1);
            fail("Construction of renderer should have been illegal (1)");
        } catch (IllegalArgumentException iae) {
            // Test was successfully if got here as the exception was thrown ;-)
        }

        // Test negative repeats
        try {
            SeparatorRenderer renderer =
                    new DefaultCharacterMenuItemGroupSeparatorRenderer("*",
                                                                       -1);
            fail("Construction of renderer should have been illegal (2)");
        } catch (IllegalArgumentException iae) {
            // Test was successfully if got here as the exception was thrown ;-)
        }

        // Test zero repeats
        try {
            SeparatorRenderer renderer =
                    new DefaultCharacterMenuItemGroupSeparatorRenderer("*",
                                                                       0);
            fail("Construction of renderer should have been illegal (3)");
        } catch (IllegalArgumentException iae) {
            // Test was successfully if got here as the exception was thrown ;-)
        }

    }

    /**
     * This tests legal separator renderer construction
     */
    public void testRenderLegal() throws Exception {
        // Test one character, one repeat
        String charString = "*";
        SeparatorRenderer renderer =
                new DefaultCharacterMenuItemGroupSeparatorRenderer(charString,
                                                                   1);
        String expected = charString;
        testRenderer(renderer, expected);
    }

    /**
     * This tests legal separator rendering of various character repeats.
     */
    public void testVaryingLengths() throws Exception {

        // Test one character, one repeat - done in {@link #testRenderLegal}

        // Test one character, two repeats
        String charString = "*";
        SeparatorRenderer renderer =
                new DefaultCharacterMenuItemGroupSeparatorRenderer(charString,
                                                                   2);
        String expected = charString + charString;
        testRenderer(renderer, expected);

        // Test one character, x repeats (x = 5)
        int repeat = 5;
        renderer =
                new DefaultCharacterMenuItemGroupSeparatorRenderer(charString,
                                                                   repeat);
        expected = "";
        for (int i = 0; i < repeat; i++) {
            // This is inefficient and should use a string buffer but this
            // is a test case so it's left like this
            expected += charString;
        }
        testRenderer(renderer, expected);

        // Test several characters (5), one repeat
        charString = "--/\\--";
        renderer =
                new DefaultCharacterMenuItemGroupSeparatorRenderer(charString,
                                                                   1);
        expected = charString;
        testRenderer(renderer, expected);

        // Test several characters (5), two repeats
        renderer =
                new DefaultCharacterMenuItemGroupSeparatorRenderer(charString,
                                                                   2);
        expected = charString + charString;
        testRenderer(renderer, expected);

        // Test several characters (5), x repeats (x = 5)
        repeat = 5;
        renderer =
                new DefaultCharacterMenuItemGroupSeparatorRenderer(charString,
                                                                   repeat);
        expected = "";
        for (int i = 0; i < repeat; i++) {
            // This is inefficient and should use a string buffer but this
            // is a test case so it's left like this
            expected += charString;
        }
        testRenderer(renderer, expected);
    }

    /**
     * Utility method that given a separator renderer and an expected string
     * will perform rendering using that renderer and then compare the output
     * with the expected string.  Various asserts are made to ensure the code
     * is operating as expected.
     *
     * @param renderer   The separator renderer to use to get output
     * @param expected   The expected output string
     * @throws Exception If there is a problem rendering or with any test assert
     */
    private void testRenderer(SeparatorRenderer renderer,
                              String expected) throws Exception {
        // Surrounding element to allow successful normalization
        String element = "div";

        // Create the test buffer
        TestDOMOutputBuffer testBuffer = new TestDOMOutputBuffer();

        // Start element
        testBuffer.openElement(element);

        // Render using the test buffer
        renderer.render(testBuffer);

        // End element
        testBuffer.closeElement(element);

        // Extract the output from the rendering as a string.
        String actual = DOMUtilities.toString(testBuffer.getRoot());
        assertNotNull("The actual string should exist", actual);

        // Normalise expected - this requires that all input strings contain
        // some markup otherwise the test will fail on this string conversion!
        expected = DOMUtilities.provideDOMNormalizedString("<" + element + ">"
                                                           + expected +
                                                           "</" + element + ">");
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

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Apr-04	3986/1	claire	VBM:2004042106 Creating a default menu item group character separator

 ===========================================================================
*/
