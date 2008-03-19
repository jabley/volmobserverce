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

package com.volantis.impl.mcs.runtime.plugin.markup;

import com.volantis.mcs.application.MarinerApplicationMock;
import com.volantis.mcs.context.EnvironmentContextMock;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.MarinerRequestContextMock;
import com.volantis.mcs.context.MarinerSessionContextMock;
import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.runtime.VolantisMock;
import com.volantis.mcs.runtime.plugin.markup.MarkupPluginContainerMock;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test that the different instances of {@link MarkupPluginScope} obtain the
 * correct container from the contexts. 
 */
public class MarkupPluginScopeTestCase
        extends TestCaseAbstract {

    private ExpectationBuilder expectations;

    private MarkupPluginContainerMock applicationScopeContainer;
    private MarkupPluginContainerMock canvasScopeContainer;
    private MarkupPluginContainerMock sessionScopeContainer;

    private VolantisMock volantis;

    private MarinerPageContextMock pageContext;
    private MarinerSessionContextMock session;
    private MarinerApplicationMock application;
    private MarinerRequestContextMock requestContext;
    private EnvironmentContextMock environmentContext;

    protected MarinerRequestContext createContextObjects() {

        expectations = mockFactory.createUnorderedBuilder();

        applicationScopeContainer = new MarkupPluginContainerMock(
                "applicationScopeContainer", expectations);
        canvasScopeContainer = new MarkupPluginContainerMock(
                "canvasScopeContainer", expectations);
        sessionScopeContainer = new MarkupPluginContainerMock(
                "sessionScopeContainer", expectations);

        volantis = new VolantisMock("volantis", expectations);

        application = new MarinerApplicationMock(
                "application", expectations);

        requestContext = new MarinerRequestContextMock(
                "requestContext", expectations);

        pageContext = new MarinerPageContextMock("pageContext", expectations);

        session = new MarinerSessionContextMock(
                "session", expectations);

        environmentContext = new EnvironmentContextMock(
                "environment", expectations);

        // Connect application to volantis bean.
        application.expects.getVolantisBean()
                .returns(volantis)
                .any();

        // Connect request to application.
        requestContext.expects.getMarinerApplication()
                .returns(application)
                .any();

        // Connect page to application and request.
        pageContext.expects.getVolantisBean()
                .returns(volantis)
                .any();
        requestContext.expects.getMarinerPageContext()
                .returns(pageContext)
                .any();

        // Connect environment to request and session.
        environmentContext.expects.getCurrentSessionContext()
                .returns(session)
                .any();
        requestContext.expects.getEnvironmentContext()
                .returns(environmentContext)
                .any();

        return requestContext;
    }

    /**
     * Tests that the locator associated with
     * {@link MarkupPluginScope#APPLICATION} returns the application scope
     * container.
     */
    public void testGetApplication()
            throws Exception {
        ContainerLocator locator = MarkupPluginScope.APPLICATION.getLocator();
        MarinerRequestContext context = createContextObjects();

        volantis.expects.getMarkupPluginContainer()
                .returns(applicationScopeContainer)
                .any();

        assertSame("Expected to get application scope container",
                   applicationScopeContainer, locator.getContainer(context));
    }

    /**
     * Tests that the locator associated with
     * {@link MarkupPluginScope#CANVAS} returns the canvas scope
     * container.
     */
    public void testGetCanvas()
            throws Exception {
        ContainerLocator locator = MarkupPluginScope.CANVAS.getLocator();
        MarinerRequestContext context = createContextObjects();

        pageContext.expects.getMarkupPluginContainer()
                .returns(canvasScopeContainer)
                .any();

        assertSame("Expected to get canvas scope container",
                   canvasScopeContainer, locator.getContainer(context));
    }

    /**
     * Tests that the locator associated with
     * {@link MarkupPluginScope#SESSION} returns the canvas scope
     * container.
     */
    public void testGetSession()
            throws Exception {
        ContainerLocator locator = MarkupPluginScope.SESSION.getLocator();
        MarinerRequestContext context = createContextObjects();

        session.expects.getMarkupPluginContainer()
                .returns(sessionScopeContainer)
                .any();

        assertSame("Expected to get session scope container",
                   sessionScopeContainer, locator.getContainer(context));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 21-Jun-05	8833/3	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 25-Jan-05	6712/1	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 ===========================================================================
*/
