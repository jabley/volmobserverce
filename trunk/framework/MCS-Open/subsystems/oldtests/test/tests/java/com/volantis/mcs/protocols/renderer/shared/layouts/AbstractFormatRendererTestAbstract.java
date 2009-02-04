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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.renderer.shared.layouts;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.StyledDOMTester;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.Layout;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.RendererTestProtocol;
import com.volantis.mcs.protocols.TestDeviceLayoutContext;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.layouts.LayoutAttributesFactory;
import com.volantis.mcs.protocols.layouts.LayoutAttributesFactoryImpl;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.renderer.layouts.FormatRenderer;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;
import com.volantis.mcs.runtime.layouts.RuntimeLayoutAdapter;
import com.volantis.styling.StylingFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Abstract base for extension by tests of
 * {@link AbstractFormatRenderer} specializations.
 *
 * @todo Remove when code coverage indicates that unit tests have same coverage.
 */
public abstract class AbstractFormatRendererTestAbstract
        extends TestCaseAbstract {
    
    /**
     * Test device layout.
     */
    protected Layout layout;

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    /**
     * The device layout context to be used in the tests.
     */
    DeviceLayoutContext dlContext;

    /**
     * A dummy test protocol that writes simplified output.
     */
    protected RendererTestProtocol protocol;

    /**
     * The duration buffer for the output.
     */
    protected StringBuffer durationBuffer;

    /**
     * The MarinerPageContext used in this test
     */
    protected TestMarinerPageContext pageContext;

    protected FormatRendererContext formatRendererContext;

    protected LayoutAttributesFactory layoutAttributesFactory;

    // Javadoc inherited from superclass
    public void setUp() throws Exception {
        super.setUp();
        setupImpl();
    }

    /**
     *  Setup which is private to this class.
     */
    protected void setupImpl() {
        layout = createDeviceLayout();

        // Activate the device layout.
        RuntimeDeviceLayout runtimeDeviceLayout =
                RuntimeDeviceLayoutTestHelper.activate(layout);

        durationBuffer = new StringBuffer();

        pageContext = new TestMarinerPageContext();
        pageContext.setDeviceLayout(runtimeDeviceLayout);

        dlContext = new MyDeviceLayoutContext(pageContext);

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();
        buffer.clear();
        pageContext.pushOutputBuffer(buffer);

        protocol = new RendererTestProtocol(pageContext, durationBuffer);

        pageContext.setProtocol(protocol);

        dlContext.setMarinerPageContext(pageContext);

        layoutAttributesFactory = new LayoutAttributesFactoryImpl();

        //create the format render context
        formatRendererContext = new FormatRendererContextImpl(
                pageContext, StylingFactory.getDefaultInstance());
    }

    protected Layout createDeviceLayout() {
        return new CanvasLayout();
    }

    /**
     * Resets the global test config to its initial state.
     */
    protected void resetConfig() {
        setupImpl();
    }

    /**
     * Extends DLC and overrides the methods that requrie protocol and
     * infrastructure interaction to use locally created objects
     */
    private class MyDeviceLayoutContext extends TestDeviceLayoutContext {

        public MyDeviceLayoutContext(MarinerPageContext pageContext) {
            super(pageContext);
        }

        // Javadoc inherited
        public OutputBuffer allocateOutputBuffer() {
            DOMOutputBuffer buffer = new DOMOutputBuffer();
            buffer.initialise();
            buffer.clear();
            return buffer;
        }

        // Javadoc inherited
        public RuntimeDeviceLayout getDeviceLayout() {
            Layout layout = createDeviceLayout();
            layout.setDefaultFormFragmentName("default");
            return new RuntimeLayoutAdapter("fred", layout, null, null);
        }
    }

    /**
     * Verifies that rendering the specified format instance with the
     * specified format renderer produces the expected output.
     * @param toRender The format instance to render
     * @param renderer The format renderer to use
     * @param expectedOutput The output expected from the rendering process
     * @throws RendererException if an exception occurs while rendering.
     */
    protected void checkRenderResults(FormatInstance toRender,
                                      FormatRenderer renderer,
                                      String expectedOutput)
            throws Exception {

        toRender.setDeviceLayoutContext(dlContext);
        renderer.render(formatRendererContext, toRender);

        compareStringToBuffer(expectedOutput,
                pageContext.getCurrentOutputBuffer());
    }

    /**
     * Compare a string containing an XML fragment to the contents of a
     * DOMOutputBuffer.
     * @param expected The expected content of the output buffer
     * @param actual The output buffer to compare against
     * @throws Exception if an error occurs
     */
    protected void compareStringToBuffer(String expected,
                                         OutputBuffer actual)
            throws Exception {

        StyledDOMTester tester = new StyledDOMTester();

        DOMOutputBuffer buffer = (DOMOutputBuffer) actual;
//        Element rootElement = domFactory.createElement();
//        rootElement.setName("root");
//        rootElement.addChildrenToHead(buffer.getRoot());
        Element rootElement = buffer.getRoot();
        rootElement.setName("root");

        String normalizedExpected = null;
        if ("".equals(expected)) {
            normalizedExpected = "<root></root>";
        } else {
            normalizedExpected = tester.normalize(
                    "<root>" + expected + "</root>");
        }

        final String output = tester.render(rootElement);

        assertEquals("Output should match expected value (" + output + ", " +
                normalizedExpected + ")", normalizedExpected, output);
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10504/1	ianw	VBM:2005112312 Fixed pseudoElements in GUI and JIBX

 29-Nov-05	10484/1	ianw	VBM:2005112312 Fixed pseudoElements in GUI and JIBX

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 28-Nov-05	10394/3	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 28-Nov-05	10394/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 29-Sep-05	9600/1	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 11-Jul-05	8862/2	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 17-Feb-05	6957/1	geoff	VBM:2005021103 R821: Branding using Projects: Prerequisites: use current project in PAPI phase

 06-Jan-05	6391/2	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 ===========================================================================
*/
