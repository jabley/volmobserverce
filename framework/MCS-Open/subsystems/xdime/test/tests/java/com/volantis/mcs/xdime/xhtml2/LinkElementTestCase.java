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

import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.EnvironmentContextMock;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.MarinerRequestContextMock;
import com.volantis.mcs.xdime.XDIMEAttributesImpl;
import com.volantis.mcs.xdime.XDIMEContextFactory;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.styling.sheet.CompiledStyleSheetMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.expression.ExpressionContextMock;
import com.volantis.xml.namespace.DefaultNamespacePrefixTracker;

import java.util.Iterator;

/**
 * Tests for LinkElement
 */
public class LinkElementTestCase extends TestCaseAbstract {
    /**
     * Html element which is used to store the information specified by the
     * link
     */
    private HtmlElement htmlElement;

    /**
     * the type of link relationship being tested
     */
    private String rel;

    /**
     * get a mariner request context mock
     * @return
     */
    private MarinerRequestContext getMarinerRequestContext() {
        MarinerRequestContextMock requestContext =
                new MarinerRequestContextMock("requestContext", expectations);

        MarinerPageContext pageContext = getMarinerPageContext();

        requestContext.expects.getMarinerPageContext().
                returns(pageContext).any();

        requestContext.expects.getEnvironmentContext().
                returns(getEnvironmentContext());

        return requestContext;
    }

    /**
     * Get an enviornment context which can provide an expression context
     * capable of resolving the mcs namespace
     * @return
     */
    private EnvironmentContext getEnvironmentContext() {
        EnvironmentContextMock environmentContext =
                new EnvironmentContextMock("environmentContext", expectations);

        ExpressionContextMock expressionContext =
                new ExpressionContextMock("expressionContext", expectations);

        environmentContext.expects.getExpressionContext().
                returns(expressionContext);

        DefaultNamespacePrefixTracker tracker =
                new DefaultNamespacePrefixTracker();
        tracker.startPrefixMapping("mcs",
                XDIMESchemata.XDIME2_MCS_NAMESPACE);

        expressionContext.expects.getNamespacePrefixTracker().returns(tracker);

        return environmentContext;
    }

    /**
     * get a mariner page context mock
     * @return
     */
    private MarinerPageContext getMarinerPageContext() {
        MarinerPageContextMock pageContext =
                new MarinerPageContextMock("pageContext", expectations);

        pageContext.expects.initialisedCanvas().returns(false);
        // todo: later: this should return a real project object so that we
        // can avoid using test specific code paths within the code.
        pageContext.expects.getCurrentProject().returns(null);

        if (!rel.equals("mcs:layout")) {
            CompiledStyleSheetMock compiledStyleSheet =
                    new CompiledStyleSheetMock("compiledStyleSheet",
                            expectations);

            pageContext.expects.retrieveThemeStyleSheet("test").
                    returns(compiledStyleSheet);
        }

        return pageContext;
    }

    /**
     * get the xdime attributes specified for the link element. this is
     * dependant on the value of rel
     * @return
     */
    private XDIMEAttributesImpl getXDIMEAttributes() {
        XDIMEAttributesImpl attributes =
                new XDIMEAttributesImpl(XHTML2Elements.LINK);

        attributes.setValue("", "rel", rel);
        attributes.setValue("", "href", "test");
        return attributes;
    }

    /**
     * get the xdime context internal containing an element stack of
     * -- html
     *   -- head
     *     -- title
     * @return
     * @throws XDIMEException
     */
    private XDIMEContextInternal getXDIMEContextInernal()
            throws XDIMEException{

        XDIMEContextInternal xdimeContext =
                (XDIMEContextInternal) XDIMEContextFactory.getDefaultInstance()
                    .createXDIMEContext();

        xdimeContext.setInitialRequestContext(
                getMarinerRequestContext());

        htmlElement = createHTMLElement(xdimeContext);

        xdimeContext.pushElement(htmlElement);
        xdimeContext.pushElement(createHeadElement(xdimeContext));
        xdimeContext.pushElement(createTitleElement(xdimeContext));

        return xdimeContext;
    }

    /**
     * create an html element - the callOpenOnProtocol needs to be called so
     * that the elment will be set to use XDIME2 mode
     * @param context
     * @return
     * @throws XDIMEException
     */
    private HtmlElement createHTMLElement(XDIMEContextInternal context)
            throws XDIMEException {
        HtmlElement html = new HtmlElement(context);

        XDIMEResult result = html.callOpenOnProtocol(context, null);

        return html;
    }

    /**
     * create a head element
     * @return
     * @param context
     */
    private HeadElement createHeadElement(XDIMEContextInternal context) {
        return new HeadElement(context);
    }

    /**
     * create a title element
     * @return
     * @param context
     */
    private TitleElement createTitleElement(XDIMEContextInternal context) {
        return new TitleElement(context);
    }

    /**
     * set up the link element for testing
     * @throws XDIMEException
     */
    private void setupLink() throws XDIMEException {
        XDIMEContextInternal xdimeContext = getXDIMEContextInernal();

        LinkElement link = new LinkElement(xdimeContext);

        XDIMEAttributesImpl attributes = getXDIMEAttributes();

        link.callOpenOnProtocol(xdimeContext, attributes);
    }

    /**
     * test the link element when it specifies a theme
     * @throws XDIMEException
     */
    public void testLinkTheme() throws XDIMEException {
        rel = "mcs:theme";
        setupLink();

        // todo: rewrite this test case to avoid the test only deprecated method
        int i=0;
        for(Iterator it =
                htmlElement.getThemeStyleSheets().
                    getCompiledStyleSheets().iterator(); it.hasNext(); i++) {
            it.next();
        }
        assertEquals("HtmlElement should have 1 style sheet", i, 1);
    }

    /**
     * test that layouts are specified in the html element correctly
     * having resolved the mcs namespace
     * @throws XDIMEException
     */
    public void testLinkLayout() throws XDIMEException {
        rel = "mcs:layout";
        setupLink();
        assertEquals("linkLayout should equal test", "test",
                htmlElement.getLayoutName());
    }
}
