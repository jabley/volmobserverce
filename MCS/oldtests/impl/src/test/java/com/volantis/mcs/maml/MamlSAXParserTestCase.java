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
package com.volantis.mcs.maml;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.ProjectStack;
import com.volantis.mcs.context.TestEnvironmentContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.runtime.project.BaseURLTracker;
import com.volantis.mcs.runtime.project.ProjectManagerMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test Case for the {@link MamlSAXParser} class.
 */
public class MamlSAXParserTestCase extends TestCaseAbstract {

    /**
     * Instance of the class being tested
     */
    private MamlSAXParser parser;

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();
        // create a new MamlSAXParser instance
        parser = new MamlSAXParser();
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
        parser = null;
    }

    /**
     * Test for the {@link MamlSAXParser#setRequestContext} method
     * @throws Exception if an error occurs
     */
    public void testSetRequestContext() throws Exception {

        // create a dummy MarinerRequestContext
        MarinerRequestContext requestContext = new TestMarinerRequestContext();

        // Register a dummy EnvironmentContext against the request context
        ContextInternals.setEnvironmentContext(requestContext,
                                               new TestEnvironmentContext());

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final MarinerPageContextMock pageContextMock =
                new MarinerPageContextMock("pageContextMock",
                        expectations);

        final ProjectManagerMock projectManagerMock =
                new ProjectManagerMock("projectManagerMock", expectations);

        ProjectStack projectStack = new ProjectStack();

        // =====================================================================
        //   Set Expectations
        // =====================================================================
        pageContextMock.expects.getRequestURL(false).returns(null);
        pageContextMock.fuzzy.setBaseURLProvider(
                mockFactory.expectsInstanceOf(BaseURLTracker.class));
        pageContextMock.expects.getProjectManager()
                .returns(projectManagerMock).any();
        pageContextMock.expects.getProjectStack().returns(projectStack).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ContextInternals.setMarinerPageContext(requestContext, pageContextMock);

        // invoke the method that is being tested
        parser.setRequestContext(requestContext);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Aug-03	1253/1	doug	VBM:2003082202 Restructured MarlinContentHandler class hierarchy

 ===========================================================================
*/
