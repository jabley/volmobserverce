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
package com.volantis.mcs.context;


import com.volantis.mcs.servlet.MarinerServletRequestContext;
import com.volantis.mcs.testtools.application.AppContext;
import com.volantis.mcs.testtools.application.AppExecutor;
import com.volantis.mcs.testtools.application.AppManager;
import com.volantis.mcs.testtools.application.DefaultAppConfigurator;
import com.volantis.mcs.testtools.request.mocks.HttpServletRequestMock;
import com.volantis.testtools.config.ConfigValue;
import com.volantis.testtools.stubs.HttpServletResponseStub;

import javax.servlet.http.HttpServletResponse;

/**
 * Test case for MarinerServletRequestContext.
 */
public class MarinerServletRequestContextTestCase
        extends MarinerRequestContextTestAbstract {

    // javadoc inherited
    public MarinerServletRequestContextTestCase(String title) {
        super(title);
    }

    /**
     * Tests that an existing expression context is copied to a nested context.
     */
    public void testNestedContextsWithExpressionContext() throws Exception {
        AppManager mgr = new AppManager(volantis, servletContext);
        mgr.setAppConf(new DefaultAppConfigurator() {
            public void setUp(ConfigValue config) throws Exception {
                super.setUp(config);
            }
        });
        mgr.useAppWith(new AppExecutor() {
            public void execute(AppContext context) throws Exception {
                doNestedContextsWithExpression();
            }
        });
    }

    /**
     * Creates an initial MarinerServletRequestContext.
     *
     * @note rest of javadoc inherited
     */
    protected MarinerRequestContext
            createInitialRequestContext(EnvironmentContext initialEnvContext)
            throws Exception {
        final HttpServletRequestMock servletRequest =
                new HttpServletRequestMock();
        final HttpServletResponse servletResponse =
                new HttpServletResponseStub();
        return new MarinerServletRequestContext(servletContext,
                servletRequest,
                servletResponse,
                initialEnvContext);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	7950/1	allan	VBM:2005041317 Some testcases for smart server

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-Oct-04	5239/2	pcameron	VBM:2004072012 Fixed layout:getPaneInstance with a parameter and nested contexts

 07-Oct-04	5237/12	pcameron	VBM:2004072012 Fixed layout:getPaneInstance with a parameter and nested contexts

 ===========================================================================
*/
