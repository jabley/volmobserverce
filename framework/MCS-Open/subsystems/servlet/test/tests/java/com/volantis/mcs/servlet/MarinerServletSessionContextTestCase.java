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

package com.volantis.mcs.servlet;

import com.volantis.mcs.runtime.plugin.markup.MarkupPluginContainerMock;
import com.volantis.mcs.runtime.plugin.markup.MarkupFactoryMock;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import mock.javax.servlet.http.HttpSessionMock;

import javax.servlet.http.HttpSessionBindingEvent;

/**
 * This class tests MarinerServletSessionContext
 */
public class MarinerServletSessionContextTestCase
        extends TestCaseAbstract {

    /**
     * Construct a new MarinerServletSessionContextTestCase
     */
    public MarinerServletSessionContextTestCase(String name) {
        super(name);
    }

    /**
     * Test the method valueUnbound(HttpSessionBindingEvent).  It should call
     * {@link MarinerServletSessionContext#release}  We can test this by adding
     * a MarkupPlugin into the session and then calling valueUnbound which
     * will ultimately release the plugin.
     */
    public void testValueUnbound() throws Exception {


        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // Create a dynamic mock container with a sequence of expectations.
        ExpectationBuilder expectations = mockFactory.createOrderedBuilder();
        MarkupPluginContainerMock container = new MarkupPluginContainerMock(
                "container", expectations);

        HttpSessionMock httpSession = new HttpSessionMock(
                "httpSession", expectations);

        MarkupFactoryMock markupFactoryMock = new MarkupFactoryMock(
                "markupFactory", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // 1) When the session context is created it should create a markup
        //    plugin container.
        markupFactoryMock.expects.createMarkupPluginContainer()
                .returns(container);

        // 2) When the session is unbound it should release the markup plugins.
        container.expects.releasePlugins();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        MarinerServletSessionContext session =
                new MarinerServletSessionContext(null, markupFactoryMock);

        assertEquals("Markup plugin container", container,
                session.getMarkupPluginContainer());

        HttpSessionBindingEvent event =
                new HttpSessionBindingEvent(httpSession, "unbound");
        session.valueUnbound(event);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Made session context create its contents lazily and optimised PseudoStylePath

 21-Jun-05	8833/3	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 20-May-05	8277/5	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 17-May-05	8277/1	pduffin	VBM:2005051704 Added expectation builder to make it easier to use combinations of sequences and sets

 18-May-05	7950/1	allan	VBM:2005041317 Some testcases for smart server

 25-Jan-05	6712/1	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-Jun-04	4737/1	allan	VBM:2004062202 Restrict volantis initialization.

 19-Jul-03	812/1	adrian	VBM:2003071609 Support session scope markup plugins

 ===========================================================================
*/
