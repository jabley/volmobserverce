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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.protocols.AbstractMenuRenderer;
import com.volantis.mcs.protocols.AbstractMenuRendererTestAbstract;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.TestDeviceLayoutContext;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.protocols.layouts.TestPaneInstance;

/**
 * Test case for the {@link OpenwaveNumericShortcutMenuRenderer} class
 */
public class OpenwaveNumericShortCutMenuRendererTestCase
        extends AbstractMenuRendererTestAbstract {

    // javadoc inherited
    protected AbstractMenuRenderer createTestableMenuRenderer() {

        // create and initialise the MarinerPageContext
        TestMarinerPageContext pageContext = new TestMarinerPageContext();
        MarinerRequestContext requestContext = new TestMarinerRequestContext();

        pageContext.pushRequestContext(requestContext);
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        PaneInstance paneInstance = new TestPaneInstance() {
            public OutputBuffer getCurrentBuffer() {
                // return the output buffer that the base class has created
                return outputBuffer;
            }
        };
        pageContext.setFormatInstance(paneInstance);

        TestDeviceLayoutContext layoutContext = new TestDeviceLayoutContext();
        layoutContext.setFormatInstance(new Pane(new CanvasLayout()),
                NDimensionalIndex.ZERO_DIMENSIONS, paneInstance);
        pageContext.pushDeviceLayoutContext(layoutContext);
        protocol.setMarinerPageContext(pageContext);

        // create and initialize the MenuRenderContext
        MenuRendererContext rendererContext =
                new MenuRendererContext() {

                    // javadoc inherited
                    public void writeTitleAttribute(Element element,
                            MCSAttributes attributes) {
                        if (attributes.getTitle() != null) {
                            element.setAttribute("title",
                                    attributes.getTitle());
                        }
                    }
                };

        // factor and return the renderer
        return new OpenwaveNumericShortcutMenuRenderer(rendererContext,
                                                       pageContext);
    }

    // javadoc inherited
    protected String getExpectedOpenCloseMenuOutput() {
        return
                "<BLOCK style='white-space: nowrap'>" +
                    "<select title=\"TestTitle\"/>" +
                "</BLOCK>";

    }

    // javadoc inherited
    protected String getExpectedOpenCloseMenuGroupOutput() {
        // menu groups are ignored by the openwave menu renderer.
        return "";
    }

    // javadoc inherited
    protected String getExpectedWriteMenuItemOutput() {
        // return the expected string
        return "<option onpick=\"http://foobar.com\" " +
                "title=\"foobarTitle\">foobar</option>";
    }

    // javadoc inherited
    protected String getExpectedRenderMenuOutput() {
        // return the expected string
        return "<BLOCK style='white-space: nowrap'>" +
                   "<select title=\"TestTitle\">" +
                       "<option onpick=\"http://foo.com\" " +
                              "title=\"fooTitle\">foo</option>" +
                       "<option onpick=\"http://foobar.com\" " +
                              "title=\"foobarTitle\">foobar</option>" +
                       "<option onpick=\"http://bar.com\" " +
                              "title=\"barTitle\">bar</option>" +
                       "<option onpick=\"http://fred.com\" " +
                              "title=\"fredTitle\">fred</option>" +
                   "</select>" +
               "</BLOCK>";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/2	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 29-Sep-05	9600/2	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/2	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 20-Jul-04	4713/5	geoff	VBM:2004061004 Support iterated Regions (fix merge conflicts)

 29-Jun-04	4713/2	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 12-Jul-04	4783/2	geoff	VBM:2004062302 Implementation of theme style options: WML Family

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 17-Sep-03	1394/1	doug	VBM:2003090902 added support for openwave numeric shortcut menus

 ===========================================================================
*/
