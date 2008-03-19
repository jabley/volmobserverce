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

package com.volantis.mcs.protocols.renderer.shared.layouts;

import com.volantis.mcs.context.ApplicationContext;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestEnvironmentContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.integration.PageURLDetails;
import com.volantis.mcs.integration.PageURLRewriter;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.layouts.Fragment;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.RendererTestProtocol;
import com.volantis.mcs.protocols.TestDeviceLayoutContext;
import com.volantis.mcs.protocols.renderer.layouts.FragmentLinkWriter;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.mcs.runtime.PageGenerationCache;
import com.volantis.mcs.runtime.TestableVolantis;
import com.volantis.mcs.runtime.URLRewriterManager;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;
import com.volantis.mcs.runtime.layouts.RuntimeLayoutAdapter;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.styling.StylingFactory;

/**
 * Test the {@link DefaultFragmentLinkWriter}.
 *
 * @todo Replace with a unit test.  
 */
public class DefaultFragmentLinkWriterTestCase
        extends TestCaseAbstract {

    /**
     * Test device layout.
     */
    protected CanvasLayout canvasLayout;

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    /**
     * The device layout context to be used in the tests.
     */
    DeviceLayoutContext dlContext;

    private TestMarinerPageContext pageContext;
    private FormatRendererContext formatRendererContext;

    protected void setUp() throws Exception {
        super.setUp();

        canvasLayout = new CanvasLayout();

        // Activate the device layout.
        RuntimeDeviceLayout runtimeDeviceLayout =
                RuntimeDeviceLayoutTestHelper.activate(canvasLayout);

        dlContext = new MyDeviceLayoutContext();

        pageContext = new TestMarinerPageContext();
        pageContext.setDeviceLayout(runtimeDeviceLayout);

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();
        buffer.clear();
        pageContext.pushOutputBuffer(buffer);

        RendererTestProtocol protocol =
                new RendererTestProtocol(pageContext, null);
        pageContext.setProtocol(protocol);

        dlContext.setMarinerPageContext(pageContext);

        formatRendererContext = new FormatRendererContextImpl(
                pageContext, StylingFactory.getDefaultInstance());
    }

    /**
     * Extends DLC and overrides the methods that requrie protocol and
     * infrastructure interaction to use locally created objects
     */
    private class MyDeviceLayoutContext extends TestDeviceLayoutContext {
        // Javadoc inherited
        public OutputBuffer allocateOutputBuffer() {
            DOMOutputBuffer buffer = new DOMOutputBuffer();
            buffer.initialise();
            buffer.clear();
            return buffer;
        }

        // Javadoc inherited
        public RuntimeDeviceLayout getDeviceLayout() {
            return new RuntimeLayoutAdapter(
                    "Name",
                    new CanvasLayout() {
                // Javadoc inherited
                public String getDefaultFormFragmentName() {
                    return "default";
                }
            }, null, null);
        }
    }

    /**
     * Test the rendering of the fragment "link-class" style.
     *
     * todo XDIME-CP fix this
     */
    public void notestFragLinkClassStyle()
            throws Exception {

        // Set up the normal testing page and request contexts
        TestMarinerRequestContext requestContext = new TestMarinerRequestContext();
        pageContext.pushRequestContext(requestContext);
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        ContextInternals.setEnvironmentContext(requestContext,
                new TestEnvironmentContext());

        // Set up fragmentation specific prerequisites.
        // Set up an app context so we can enable fragmentation.
        ApplicationContext appContext = new ApplicationContext(requestContext);
        ContextInternals.setApplicationContext(requestContext, appContext);
        appContext.setFragmentationSupported(true);
        // Fragmentation needs a URL and rewriter for URL manipulation.
        pageContext.setRootPageURL(new MarinerURL());
        pageContext.setVolantis(new TestableVolantis() {
            public PageURLRewriter getLayoutURLRewriter() {
                return new PageURLRewriter() {
                    public MarinerURL rewriteURL(MarinerRequestContext context,
                                                 MarinerURL url,
                                                 PageURLDetails details) {
                        return new URLRewriterManager.DefaultURLRewriter().
                                mapToExternalURL(context, url);
                    }
                };
            }

        });
        // Fragmentation stores it's state in the page generation cache.
        pageContext.setPageGenerationCache(new PageGenerationCache());

        FragmentLinkWriter writer = new DefaultFragmentLinkWriter(pageContext);
        Fragment source = new Fragment(canvasLayout);
        Fragment destination = new Fragment(canvasLayout);

        // Render and check that we have the default fragment style.
        writer.writeFragmentLink(formatRendererContext, source, destination,
                false, false);
        compareStringToBuffer("<fragmentLink styleClass=\"fraglinks\"/>",
                pageContext.getCurrentOutputBuffer(),
                "Fragment link should have default 'fraglinks' style");

        // set the fragment link class style to 'test'
        source.setAttribute(FormatConstants.FRAGMENT_LINK_STYLE_CLASS_ATTRIBUTE, "test");

        // Re-render and check that we have the correct 'test' fragment style.
        ((DOMOutputBuffer) pageContext.getCurrentOutputBuffer()).clear();
        writer.writeFragmentLink(formatRendererContext, source, destination,
                false, false);
        compareStringToBuffer("<fragmentLink styleClass=\"test\"/>",
                pageContext.getCurrentOutputBuffer(),
                "Fragment link should have explicit 'test' style");

    }

    /**
     * Test the rendering of the fragment "link-class" style.
     *
     * todo XDIME-CP fix this
     */
    public void notestFragLinkClassStyle2() throws Exception {

        // Set up the normal testing page and request contexts
        TestMarinerRequestContext requestContext = new TestMarinerRequestContext();
        pageContext.pushRequestContext(requestContext);
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        ContextInternals.setEnvironmentContext(requestContext,
                new TestEnvironmentContext());

        // Set up fragmentation specific prerequisites.
        // Set up an app context so we can enable fragmentation.
        ApplicationContext appContext = new ApplicationContext(requestContext);
        ContextInternals.setApplicationContext(requestContext, appContext);
        appContext.setFragmentationSupported(true);
        // Fragmentation needs a URL and rewriter for URL manipulation.
        pageContext.setRootPageURL(new MarinerURL());
        pageContext.setVolantis(new TestableVolantis() {
            public PageURLRewriter getLayoutURLRewriter() {
                return new PageURLRewriter() {
                    public MarinerURL rewriteURL(MarinerRequestContext context,
                                                 MarinerURL url,
                                                 PageURLDetails details) {
                        return new URLRewriterManager.DefaultURLRewriter().
                                mapToExternalURL(context, url);
                    }
                };
            }
        });
        // Fragmentation stores it's state in the page generation cache.
        pageContext.setPageGenerationCache(new PageGenerationCache());

        FragmentLinkWriter writer = new DefaultFragmentLinkWriter(pageContext);
        Fragment source = new Fragment(canvasLayout);
        Fragment destination = new Fragment(canvasLayout);

        // Render and check that we have the default fragment style.
        writer.writeFragmentLink(formatRendererContext, source, destination,
                false, false);
        compareStringToBuffer("<fragmentLink styleClass=\"fraglinks\"/>",
                pageContext.getCurrentOutputBuffer(),
                "Fragment link should have default 'fraglinks' style");

        // set the fragment link class style to 'test'
        source.setAttribute(FormatConstants.FRAGMENT_LINK_STYLE_CLASS_ATTRIBUTE,
                            "test");

        // Re-render and check that we have the correct 'test' fragment style.
        ((DOMOutputBuffer) pageContext.getCurrentOutputBuffer()).clear();
        writer.writeFragmentLink(formatRendererContext, source, destination,
                false, false);
        compareStringToBuffer("<fragmentLink styleClass=\"test\"/>",
                pageContext.getCurrentOutputBuffer(),
                "Fragment link should have explicit 'test' style");

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
        DOMOutputBuffer buffer = (DOMOutputBuffer) actual;
        Element rootElement = domFactory.createElement();
        rootElement.setName("root");
        rootElement.addHead(buffer.getRoot());

        String normalizedExpected = null;
        if ("".equals(expected)) {
            normalizedExpected = "<root></root>";
        } else {
            normalizedExpected = DOMUtilities.provideDOMNormalizedString(
                    "<root>" + expected + "</root>");
        }

        final String output = DOMUtilities.toString(rootElement);

        assertEquals("Output should match expected value (" + output + ", " +
                normalizedExpected + ")", normalizedExpected, output);
    }


    /**
     * Compare a string containing an XML fragment to the contents of a
     * DOMOutputBuffer.
     * @param expected The expected content of the output buffer
     * @param actual The output buffer to compare against
     * @param message The assert message to fail with if they do not match
     * @throws Exception if an error occurs
     */
    protected void compareStringToBuffer(String expected,
                                         OutputBuffer actual,
                                         String message)
            throws Exception {
        DOMOutputBuffer buffer = (DOMOutputBuffer) actual;
        assertEquals(message,
                DOMUtilities.provideDOMNormalizedString(expected),
                DOMUtilities.toString(buffer.getRoot()));
    }

    public void testDUMMY() {
        
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10504/1	ianw	VBM:2005112312 Fixed pseudoElements in GUI and JIBX

 29-Nov-05	10484/1	ianw	VBM:2005112312 Fixed pseudoElements in GUI and JIBX

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 31-Aug-05	9391/1	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 ===========================================================================
*/
