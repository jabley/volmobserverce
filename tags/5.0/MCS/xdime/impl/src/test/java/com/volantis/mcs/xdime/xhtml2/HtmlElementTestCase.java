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
package com.volantis.mcs.xdime.xhtml2;

import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.context.MarinerRequestContextMock;
import com.volantis.mcs.xdime.XDIMEContextFactory;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEMode;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test the HtmlElement
 */
public class HtmlElementTestCase extends TestCaseAbstract {

    private MarinerPageContextMock pageContextMock;

    private MarinerRequestContextMock requestContextMock;

    protected void setUp() throws Exception {
        super.setUp();

        requestContextMock = new MarinerRequestContextMock(
                "requestContext", expectations);

        pageContextMock = new MarinerPageContextMock("pageContext", expectations);

        requestContextMock.expects.getMarinerPageContext().
                returns(pageContextMock).any();
    }

    /**
     * test the html element sets the mode to xdime 2
     * @throws XDIMEException
     */
    public void testHtmlModeForXDIME2() throws XDIMEException {

         XDIMEContextInternal xdimeContext =
                (XDIMEContextInternal) XDIMEContextFactory.getDefaultInstance()
                    .createXDIMEContext();

        HtmlElement html = new HtmlElement(xdimeContext);

        // In XDIME 2 the canvas is processed in the <html:body>.
        pageContextMock.expects.initialisedCanvas().returns(false);

        xdimeContext.setInitialRequestContext(requestContextMock);

        // todo: later: this should return a real project object so that we
        // can avoid using test specific code paths within the code.
        pageContextMock.expects.getCurrentProject().returns(null).any();

        XDIMEResult result = html.callOpenOnProtocol(xdimeContext, null);

        assertTrue("xdime mode should be XDIMEMode.XDIME2",
                html.getXDIMEMode() == XDIMEMode.XDIME2);
    }

    /**
     * test the html element sets the mode to xdime CP
     * @throws XDIMEException
     */
    public void testHtmlModeForXDIMECP() throws XDIMEException {

         XDIMEContextInternal xdimeContext =
                (XDIMEContextInternal) XDIMEContextFactory.getDefaultInstance()
                    .createXDIMEContext();

        HtmlElement html = new HtmlElement(xdimeContext);

        // In XDIME CP the canvas is processed in the containing <cdm:canvas>.
        pageContextMock.expects.initialisedCanvas().returns(true);

        xdimeContext.setInitialRequestContext(requestContextMock);

        XDIMEResult result = html.callOpenOnProtocol(xdimeContext, null);

        assertTrue("xdime mode should be XDIMEMode.XDIME1",
                html.getXDIMEMode() == XDIMEMode.XDIMECP);
    }
}
