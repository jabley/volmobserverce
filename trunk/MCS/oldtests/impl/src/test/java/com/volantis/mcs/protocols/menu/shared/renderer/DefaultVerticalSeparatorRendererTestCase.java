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
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultVerticalSeparatorRenderer;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.papi.LineBreakAttributes;

/**
 * This is a test case for {@link com.volantis.mcs.protocols.menu.shared.renderer.DefaultVerticalSeparatorRenderer},  It tests the
 * outcome of using this separator renderer.
 */
public class DefaultVerticalSeparatorRendererTestCase extends TestCaseAbstract {

    /**
     * Create a new instance of this test case.
     */
    public DefaultVerticalSeparatorRendererTestCase() {
    }

    /**
     * Create a new named instance of this test case.
     *
     * @param s The name of the test case.
     */
    public DefaultVerticalSeparatorRendererTestCase(String s) {
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
     * Test the render method.
     */
    public void testRender() throws Exception {
        SeparatorRenderer renderer = new DefaultVerticalSeparatorRenderer(
                new TestDeprecatedLineBreakOutput());

        // Create expected
        String expected = DOMUtilities.provideDOMNormalizedString("<br/>");

        // Create the test buffer
        TestDOMOutputBuffer testBuffer = new TestDOMOutputBuffer();

        // Render using the test buffer
        renderer.render(testBuffer);

        // Extract the output from the rendering as a string.
        String actual = DOMUtilities.toString(testBuffer.getRoot());
        assertNotNull("The actual string should exist", actual);

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

 01-Oct-04	5635/1	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 26-Apr-04	3920/5	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 22-Apr-04	4004/1	claire	VBM:2004042204 Implemented remaining required WML renderers

 ===========================================================================
*/
