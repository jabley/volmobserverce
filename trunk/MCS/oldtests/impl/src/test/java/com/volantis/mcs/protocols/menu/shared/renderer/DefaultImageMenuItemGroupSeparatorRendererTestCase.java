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
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.protocols.assets.implementation.AssetResolverMock;
import com.volantis.mcs.protocols.menu.shared.MenuEntityCreation;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * This is a test case for {@link com.volantis.mcs.protocols.menu.shared.renderer.DefaultImageMenuItemGroupSeparatorRenderer},  It
 * tests the outcome of using this separator renderer.  The output is
 * simplistic as the menu renderers are split into small isolated pieces of
 * functionality.
 */
public class DefaultImageMenuItemGroupSeparatorRendererTestCase
        extends TestCaseAbstract {

    /**
     * A reference to an object that allows for easy creation of various
     * menu entities for use in tests.
     */
    private MenuEntityCreation entity;

    /**
     * Create a new instance of this test case.
     */
    public DefaultImageMenuItemGroupSeparatorRendererTestCase() {
    }

    /**
     * Create a new named instance of this test case.
     *
     * @param s The name of the test case.
     */
    public DefaultImageMenuItemGroupSeparatorRendererTestCase(String s) {
        super(s);
    }

    // JavaDoc inherited
    protected void setUp() throws Exception {
        super.setUp();
        entity = new MenuEntityCreation();
    }

    // JavaDoc inherited
    protected void tearDown() throws Exception {
        entity = null;
        super.tearDown();
    }

    /**
     * This tests a successful rendering of the separator.  Successful means
     * that the image asset exists and is written out.
     */
    public void testSuccessfulRender() throws Exception {
        // Create test buffer locator
        DOMOutputBuffer buffer = new TestDOMOutputBuffer();

        // Create the test items
        ImageAssetReference reference =
                entity.createImageAssetReference("separator");

        // Create test renderer object
        DefaultImageMenuItemGroupSeparatorRenderer renderer =
                new DefaultImageMenuItemGroupSeparatorRenderer(
                        reference, new TestDeprecatedImageOutput());

        // Create expected.
        String expected = 
                "<test-image " +
                    "src=\"separator\" " +
                    "tag-name=\"img\"" +
                "/>";

        // Render using the test buffer locator and test item
        renderer.render(buffer);

        // Check output
        checkOutput(expected, buffer);
    }

    /**
     * This tests a failed rendering of the separator.  Failed means that the
     * image asset url did not exist.
     */
    public void testFailedRender() throws Exception {
        // Create test buffer locator
        DOMOutputBuffer buffer = new TestDOMOutputBuffer();

        // Create the test items
        ImageAssetReference reference =
                entity.createImageAssetReference(null);

        // Create test renderer object
        DefaultImageMenuItemGroupSeparatorRenderer renderer =
                new DefaultImageMenuItemGroupSeparatorRenderer(
                        reference, new TestDeprecatedImageOutput());

        // Create expected
        String expected = "";

        // Render using the test buffer locator and test item
        renderer.render(buffer);

        // Check output
        checkOutput(expected, buffer);
    }

    /**
     * A utility method that takes the expected output and a DOM output buffer
     * and compares the two, doing various asserts in the process.
     *
     * @param expected The expected value contained within the buffer
     * @param buffer   The buffer object associated with the renderer
     */
    private void checkOutput(String expected,
                             DOMOutputBuffer buffer) throws Exception {
        // Normalise the expected String
        String normalisedExpected = 
                DOMUtilities.provideDOMNormalizedString(expected);

        // Check state of buffer
        assertNotNull("There should be a buffer (output)", buffer);
        assertFalse("Should be contents in the buffer", buffer.isEmpty());

        // Extract the output from the menu rendering as a string.
        String actual = DOMUtilities.toString(buffer.getRoot());
        assertNotNull("The actual string should exist", actual);

        // Compare state and expected
        assertEquals("Strings should match.  Expected = \"" +
                     normalisedExpected + "\", actual = \"" + actual + "\".",
                     normalisedExpected, actual);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-May-04	4153/1	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 26-Apr-04	3920/6	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 22-Apr-04	4004/3	claire	VBM:2004042204 Implemented remaining required WML renderers

 ===========================================================================
*/
